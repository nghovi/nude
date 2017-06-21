package trente.asia.addresscard.commons.activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.utils.OpenCvUtils;
import trente.asia.addresscard.databinding.ActivityCameraBinding;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private CameraBridgeViewBase mOpenCvCameraView;
    ActivityCameraBinding binding;

    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;

    int width, height;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TakeImageTask().execute();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCameraPreview();
    }

    private void stopCameraPreview(){
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    private void startCameraPreview() {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(this.width * width / height, this.width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        Core.transpose(mRgba, mRgbaT);

        Core.flip(mRgbaT, mRgbaT, 0);

        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size());

        try {
            CardContour card = OpenCvUtils.findRectangle(mRgbaF);
            mRgba = card.contourMat;
            rotatedRect = card.rect;
            outMat = mRgba.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRgba;
    }
    RotatedRect rotatedRect;
    Mat outMat;

    private class TakeImageTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(CameraActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Saving picture..");
            progressDialog.show();
            stopCameraPreview();
        }

        @Override
        protected Void doInBackground(Void... params) {
            takeCapture();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            binding.rltCard.setVisibility(View.VISIBLE);
            binding.imageView.setImageBitmap(bitmap);
            binding.text.setText(detectedText);
        }
    }

    Bitmap bitmap;
    String detectedText = "";

    private void takeCapture() {
        synchronized (rotatedRect) {
            synchronized (outMat) {
                log("Start capture");

                long startTime = System.currentTimeMillis();
                Mat matrix, rotated = new Mat(), cropped = new Mat();
                float angle = (float)rotatedRect.angle;
                Size rect_size = rotatedRect.size;
                if (rect_size.width <= 0 || rect_size.height <= 0) {
                    return;
                }
                if (rotatedRect.angle < - 45.0) {
                    angle += 90.0;
                    double swap = rect_size.width;
                    rect_size.width = rect_size.height;
                    rect_size.height = swap;
                }
                matrix = Imgproc.getRotationMatrix2D(rotatedRect.center, angle, 1.0);
                Imgproc.warpAffine(outMat, rotated, matrix, outMat.size(), Imgproc.INTER_CUBIC);
//                Mat mDist32 = new Mat(rotated.rows(),rotated.cols(),CV_32SC1); // 32 bit signed 1 channel, use CV_32UC1 for unsigned
//                rotated.convertTo(mDist32,CV_32SC1,1,0);

//                Imgproc.getRectSubPix(rotated, rect_size, rotatedRect.center, cropped);
                Rect roi = new Rect((int)(rotatedRect.center.x - rect_size.width/2), (int)(rotatedRect.center.y - rect_size.height/2),
                        (int)rect_size.width, (int)rect_size.height);

                cropped = new Mat(rotated, roi);
                Mat output = cropped;

                bitmap = Bitmap.createBitmap(output.cols(), output.rows(), Bitmap.Config.ARGB_8888);
                org.opencv.android.Utils.matToBitmap(output, bitmap);

                TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                    detectedText += textBlock.getValue() + "\n";
                }

//                File file = new File(Environment.getExternalStorageDirectory(), "output.png");
//                try {
//                    OutputStream outputStream = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream);
//                    outputStream.flush();
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                log("Write success !!! Time = " + (System.currentTimeMillis() - startTime));
            }
        }
    }

    @Override
    public void onBackPressed() {
        binding.rltCard.setVisibility(View.GONE);
        startCameraPreview();
        detectedText = "";
        binding.imageView.setImageResource(R.drawable.ac_icon_card);
    }

    private void log(String msg) {
        Log.e("MainActivity", msg);
    }
}
