package nguyenhoangviet.vpcorp.thankscard.services.posted;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.databinding.FragmentCroppingBinding;
import nguyenhoangviet.vpcorp.thankscard.fragments.AbstractTCFragment;
import nguyenhoangviet.vpcorp.thankscard.services.posted.listener.OnCroppingListener;
import nguyenhoangviet.vpcorp.thankscard.services.posted.view.TouchPad;
import nguyenhoangviet.vpcorp.thankscard.utils.TCUtil;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesSystemUtil;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * Created by tien on 9/6/2017.
 */

public class CroppingFragment extends AbstractTCFragment implements TouchPad.OnTouchPadListener, View.OnClickListener{
    private String imagePath;
    private FragmentCroppingBinding binding;
    private OnCroppingListener callback;
    private String cardUrl;

    public void setCallback(OnCroppingListener callback) {
        this.callback = callback;
    }

    public void setImagePath(String imagePath,String cardUrl) {
        this.imagePath = imagePath;
        this.cardUrl = cardUrl;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_cropping;
    }

    @Override
    public boolean hasBackBtn() {
        return false;
    }

    @Override
    public boolean hasSettingBtn() {
        return false;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        binding.btnOk.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
    }

    @Override
    public void buildBodyLayout() {
        binding = DataBindingUtil.bind(mRootView);
        binding.layoutPhoto.setImage(imagePath, TcConst.POSITION_LEFT);
        binding.touchPad.setCallback(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.cardPreview.getLayoutParams();
        PreferencesSystemUtil preference = new PreferencesSystemUtil(getContext());
        params.height = (int) Float.parseFloat(preference.get(TcConst.PREF_FRAME_HEIGHT));
        params.width = (int) Float.parseFloat(preference.get(TcConst.PREF_FRAME_WIDTH));
        log("width = " + params.width);
        log("height = " + params.height);
        Glide.with(getContext()).load(cardUrl).into(binding.cardPreview);
    }

    @Override
    public void onTouchMove(float moveX, float moveY) {
        binding.layoutPhoto.move(moveX, moveY);
    }

    @Override
    public void onTouchScale(float scale) {
        binding.layoutPhoto.scale(scale);
    }

    @Override
    public void onTouchScaleEnd() {
        binding.layoutPhoto.endScale();
    }

    @Override
    public void onTouchPadClick() {
    }

    private void log(String msg) {
        Log.e("CroppingFragment", msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                cropPicture();
                break;
            case R.id.btn_cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void cropPicture() {
        binding.cardPreview.setVisibility(View.INVISIBLE);
        binding.grayViewTop.setVisibility(View.INVISIBLE);
        binding.grayViewBottom.setVisibility(View.INVISIBLE);
        binding.getRoot().setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(binding.getRoot().getDrawingCache());
        ImageView imageView = binding.cardPreview;
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, location[0], location[1] - getStatusBarHeight(), imageView.getWidth(), imageView.getHeight());
        String imagePath = TCUtil.getFilesFolderPath() + "/" + System.currentTimeMillis() + ".png";
        TCUtil.saveFileToStorage(newBitmap, imagePath);
        if (callback != null) {
            callback.onCroppingCompleted(imagePath);
        }
        getFragmentManager().popBackStack();
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}

