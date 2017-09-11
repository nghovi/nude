package trente.asia.thankscard.services.posted;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.databinding.FragmentCroppingBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.posted.listener.OnCroppingListener;
import trente.asia.thankscard.services.posted.view.TouchPad;
import trente.asia.thankscard.utils.TCUtil;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

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
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, location[0], location[1], imageView.getWidth(), imageView.getHeight());
        String imagePath = TCUtil.getFilesFolderPath() + "/" + System.currentTimeMillis() + ".png";
        TCUtil.saveFileToStorage(newBitmap, imagePath);
        if (callback != null) {
            callback.onCroppingCompleted(imagePath);
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}

