package trente.asia.team360.services.camera;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import trente.asia.team360.R;
import trente.asia.team360.common.fragments.AbstractTmFragment;
import trente.asia.team360.databinding.FragmentMemberViewBinding;
import trente.asia.team360.services.member.TmMemberViewFragment;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by takano-yasuhiro on 2017/06/19.
 */

public class TmCameraViewFragment extends AbstractTmFragment {

    private final static int RESULT_CAMERA = 1001;

    private Button btn;

    private ImageView img;

    private Uri photoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_camera_view, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img = (ImageView) view.findViewById(R.id.img_id_camera_view);

        btn = (Button) view.findViewById(R.id.btn_id_start_camera);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, RESULT_CAMERA);

                try {
                    ContentValues values = new ContentValues();
                    photoUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, RESULT_CAMERA);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            img.setImageBitmap(bitmap);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
                img.setImageBitmap(bitmap);


                TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                    Log.d("TAK", "Text: " + textBlock.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_common_footer_camera;
    }
}
