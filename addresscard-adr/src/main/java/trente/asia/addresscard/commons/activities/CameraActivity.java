package trente.asia.addresscard.commons.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

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
import trente.asia.addresscard.commons.utils.Utils;
import trente.asia.addresscard.databinding.ActivityCameraBinding;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2,View.OnClickListener{

	private CameraBridgeViewBase	mOpenCvCameraView;
	ActivityCameraBinding			binding;
	Mat								mRgba;
	Mat								mRgbaF;
	Mat								mRgbaT;
	Bitmap							bitmap;
	String							detectedText	= "";
	String							cardPath		= "";
	String							logoPath		= "";

	int								width, height;

	private BaseLoaderCallback		mLoaderCallback	= new BaseLoaderCallback(this) {

														@Override
														public void onManagerConnected(int status){

															switch(status){
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
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
		mOpenCvCameraView = (JavaCameraView)findViewById(R.id.show_camera_activity_java_surface_view);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		width = displayMetrics.widthPixels;
		height = displayMetrics.heightPixels;
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				 new TakeImageTask().execute();
			}
		});

		binding.btnCancel.setOnClickListener(this);
		binding.btnFlash.setOnClickListener(this);
	}

	@Override
	protected void onPause(){
		super.onPause();
		stopCameraPreview();
	}

	private void stopCameraPreview(){
		if(mOpenCvCameraView != null){
			mOpenCvCameraView.disableView();
		}
	}

	private void startCameraPreview(){
		if(!OpenCVLoader.initDebug()){
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
		}else{
			mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		startCameraPreview();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(mOpenCvCameraView != null){
			mOpenCvCameraView.disableView();
		}
	}

	@Override
	public void onCameraViewStarted(int width, int height){
		mRgba = new Mat(height, width, CvType.CV_8UC4);
		mRgbaF = new Mat(this.width * width / height, this.width, CvType.CV_8UC4);
		mRgbaT = new Mat(width, width, CvType.CV_8UC4);
	}

	@Override
	public void onCameraViewStopped(){
		mRgba.release();
	}

	@Override
	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
		mRgba = inputFrame.rgba();
		Core.transpose(mRgba, mRgbaT);

		Core.flip(mRgbaT, mRgbaT, 0);

		Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size());

		try{
			CardContour card = OpenCvUtils.findRectangle(mRgbaF);
			mRgba = card.contourMat;
			rotatedRect = card.rect;
			outMat = mRgba.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		return mRgba;
	}

	RotatedRect	rotatedRect;
	Mat			outMat;

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.btn_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_flash:
			break;
		default:
			break;
		}
	}

	private class TakeImageTask extends AsyncTask<Void, Void, Void>{

		private ProgressDialog progressDialog = new ProgressDialog(CameraActivity.this);

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			progressDialog.setTitle("Saving picture..");
			progressDialog.show();
			stopCameraPreview();
		}

		@Override
		protected Void doInBackground(Void...params){
			takeCapture();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid){
			super.onPostExecute(aVoid);
			progressDialog.dismiss();
			if (detectedText.isEmpty()) {
				setResult(RESULT_CANCELED);
				Toast.makeText(CameraActivity.this, "Please take photo again !!", Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent();
				intent.putExtra("cardPath", cardPath);
				intent.putExtra("logoPath", logoPath);
				intent.putExtra("text", detectedText);
				setResult(RESULT_OK, intent);
			}

			finish();
		}
	}

	private void takeCapture(){
		synchronized(rotatedRect){
			synchronized(outMat){
				Mat matrix, rotated = new Mat(), cropped = new Mat();
				float angle = (float)rotatedRect.angle;
				Size rect_size = rotatedRect.size;
				if(rect_size.width <= 0 || rect_size.height <= 0){
					return;
				}
				if(rotatedRect.angle < -45.0){
					angle += 90.0;
					double swap = rect_size.width;
					rect_size.width = rect_size.height;
					rect_size.height = swap;
				}
				matrix = Imgproc.getRotationMatrix2D(rotatedRect.center, angle, 1.0);
				Imgproc.warpAffine(outMat, rotated, matrix, outMat.size(), Imgproc.INTER_CUBIC);

				Rect roi = new Rect((int)(rotatedRect.center.x - rect_size.width / 2),
                        (int)(rotatedRect.center.y - rect_size.height / 2), (int)rect_size.width, (int)rect_size.height);

				cropped = new Mat(rotated, roi);
				Mat output = cropped;

				bitmap = Bitmap.createBitmap(output.cols(), output.rows(), Bitmap.Config.ARGB_8888);
				org.opencv.android.Utils.matToBitmap(output, bitmap);

				TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

				Frame frame = new Frame.Builder().setBitmap(bitmap).build();
				SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
				for(int i = 0; i < textBlocks.size(); i++){
					TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
					detectedText += textBlock.getValue() + "\n";
				}

				String folderCamera = Utils.createFolder("Camera");
				cardPath = Utils.copyImageToStorage(folderCamera, System.currentTimeMillis() + ".png", bitmap, 1);
				Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
				logoPath = Utils.copyImageToStorage(folderCamera, "logo" + System.currentTimeMillis() + ".png", logoBitmap, 1);
			}
		}
	}

	@Override
	public void onBackPressed(){
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	private void log(String msg){
		Log.e("MainActivity", msg);
	}
}
