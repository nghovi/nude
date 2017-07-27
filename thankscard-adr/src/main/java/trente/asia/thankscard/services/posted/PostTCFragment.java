package trente.asia.thankscard.services.posted;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import io.realm.RealmResults;
import trente.asia.android.util.AndroidUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.databinding.FragmentPostTcBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.fragments.dialogs.PostConfirmDialog;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.thankscard.services.mypage.model.StampCategoryModel;
import trente.asia.thankscard.services.mypage.model.StampModel;
import trente.asia.thankscard.services.posted.view.StickerViewPost;
import trente.asia.thankscard.services.posted.presenter.StampAdapter;
import trente.asia.thankscard.services.posted.presenter.StampCategoryAdapter;
import trente.asia.thankscard.services.posted.view.ChangeToNormalCardDialog;
import trente.asia.thankscard.services.posted.view.ChoosePictureDialog;
import trente.asia.thankscard.services.posted.view.LimitStickerDialog;
import trente.asia.thankscard.services.posted.view.TouchPad;
import trente.asia.thankscard.utils.TCUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by tien on 7/12/2017.
 */

public class PostTCFragment extends AbstractTCFragment implements View.OnClickListener, SelectDeptFragment.OnSelectDeptListener, SelectUserFragment.OnSelectUserListener, SelectCardFragment.OnSelectCardListener, StickerViewPost.OnStickerListener, TouchPad.OnTouchPadListener, StampCategoryAdapter.OnStampCategoryAdapterListener, StampAdapter.OnStampAdapterListener {

    public final int MAX_LETTER = 75;

    private List<Template> templates;
    private List<Template> photoTemplates;
    private FragmentPostTcBinding binding;
    private Template template;
    private List<DeptModel> departments;
    private DeptModel department;
    private UserModel member;
    private String message;
    private List<StickerViewPost> stickers = new ArrayList<>();
    private Uri mImageUri;
    private String mImagePath;
    private boolean canSendPhoto = false;
    private RealmResults<StampCategoryModel> stampCategories;
    private StampAdapter stampAdapter;
    private Realm mRealm;
    private float frameWidth;
    private float frameHeight;
    private boolean showLayoutSticker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        PreferencesSystemUtil preference = new PreferencesSystemUtil(getContext());
        frameWidth = Float.valueOf(preference.get(TcConst.PREF_FRAME_WIDTH));
        frameHeight = Float.valueOf(preference.get(TcConst.PREF_FRAME_HEIGHT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_tc, container, false);
            mRootView = binding.getRoot();

        }
        return mRootView;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_post_tc;
    }

    @Override
    public boolean hasBackBtn() {
        return true;
    }

    @Override
    public boolean hasSettingBtn() {
        return true;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public int getTitle() {
        return R.string.fragment_post_edit_title;
    }

    @Override
    public void buildBodyLayout() {
        template = new Template();
        template.templateId = prefAccUtil.get(TcConst.PREF_TEMPLATE_ID);
        template.templateUrl = prefAccUtil.get(TcConst.PREF_TEMPLATE_PATH);
        binding.rltSelectDept.setOnClickListener(this);
        binding.rltSelectUser.setOnClickListener(this);
        binding.lnrSelectCard.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.lnrSelectSticker.setOnClickListener(this);
        binding.lnrSelectPhoto.setOnClickListener(this);
        binding.mainLayout.setOnClickListener(this);
        binding.edtMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!canSendPhoto) {
                    message = editable.toString();
                    binding.txtCount.setText(String.valueOf(MAX_LETTER - message.length()));
                    binding.edtMessagePhoto.setText(message);
                }
            }
        });

        binding.edtMessagePhoto.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (canSendPhoto) {
                    message = editable.toString();
                    binding.txtCount.setText(String.valueOf(MAX_LETTER - message.length()));
                    binding.edtMessage.setText(message);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        requestAccountInfo();
        requestTemplate();
        buildTemplate();
        buildLayoutSticker();
    }

    private void buildLayoutSticker() {
        stampCategories = mRealm.where(StampCategoryModel.class).findAll();

        StampCategoryAdapter categoryAdapter = new StampCategoryAdapter(this);
        categoryAdapter.setStampCategories(stampCategories);
        binding.listStampCategories.setAdapter(categoryAdapter);
        binding.listStampCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        stampAdapter = new StampAdapter(this);
        binding.listStamps.setAdapter(stampAdapter);
        binding.listStamps.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false));

        if (!stampCategories.isEmpty()) {
            stampAdapter.setStamps(stampCategories.get(0).stamps);
        }
    }

    private void requestTemplate() {
        JSONObject param = new JSONObject();
        requestLoad(TcConst.API_GET_TEMPLATE, param, true);
    }

    private void requestAccountInfo() {
        JSONObject param = new JSONObject();
        requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, param, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (TcConst.API_GET_TEMPLATE.equals(url)) {
            requestTemplateSuccess(response);
        } else if (WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)) {
            departments = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
            department = new DeptModel(CCConst.NONE, getString(R.string.chiase_common_none));
            departments.add(0, department);
            for (DeptModel dept : departments) {
                if (CCCollectionUtil.isEmpty(dept.members)) {
                    dept.members = new ArrayList<>();
                }
                member = new UserModel(CCConst.NONE, getString(R.string.chiase_common_none));
                dept.members.add(0, member);
            }
        } else {
            super.successLoad(response, url);
        }
    }

    private void requestTemplateSuccess(JSONObject response) {
        templates = CCJsonUtil.convertToModelList(response.optString("templates"), Template.class);
        photoTemplates = CCJsonUtil.convertToModelList(response.optString("photos"), Template.class);
    }

    private void buildTemplate() {
        Picasso.with(getContext()).load(BuildConfig.HOST + template.templateUrl).resize((int) frameWidth, (int) frameHeight).into(binding.imgCard);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_select_dept:
                SelectDeptFragment fragment = new SelectDeptFragment();
                gotoFragment(fragment);
                fragment.setDepartments(departments, department);
                fragment.setCallback(this);
                break;
            case R.id.rlt_select_user:
                if (!WelfareConst.NONE.equals(department.key)) {
                    SelectUserFragment userFragment = new SelectUserFragment();
                    userFragment.setCallback(this);
                    userFragment.setDepartments(department.members, member);
                    gotoFragment(userFragment);
                }
                break;
            case R.id.lnr_select_card:
                if (canSendPhoto && (binding.layoutPhoto.hasImage() || !stickers.isEmpty())) {
                    showDialogChangeToNormalCard();
                } else {
                    showLayoutCards(templates);
                }
                break;
            case R.id.lnr_select_sticker:
                showLayoutSticker();
                break;
            case R.id.lnr_select_photo:
                showLayoutCards(photoTemplates);
                break;
            case R.id.btn_send:
                checkNewCard();
                break;
            case R.id.main_layout:
                if (showLayoutSticker) {
                    closeLayoutSticker();
                }
                break;
            default:
                break;
        }
    }

    private void showLayoutSticker() {
        binding.layoutSticker.setVisibility(View.VISIBLE);
        binding.rltSelectDept.setVisibility(View.GONE);
        binding.rltSelectUser.setVisibility(View.GONE);
        showLayoutSticker = true;
    }

    private void closeLayoutSticker() {
        binding.layoutSticker.setVisibility(View.GONE);
        binding.rltSelectDept.setVisibility(View.VISIBLE);
        binding.rltSelectUser.setVisibility(View.VISIBLE);
        showLayoutSticker = false;
    }

    private void showLayoutCards(List<Template> templates) {
        SelectCardFragment cardFragment = new SelectCardFragment();
        cardFragment.setCards(templates);
        cardFragment.setCallback(this);
        gotoFragment(cardFragment);
    }

    private void showDialogChangeToNormalCard() {
        final ChangeToNormalCardDialog dialog = new ChangeToNormalCardDialog();
        dialog.setListeners(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showLayoutCards(templates);
                dialog.dismiss();
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), null);
    }

    private void changeLayoutCard() {
        buildTemplate();
        if (canSendPhoto) {
            binding.lnrBody.setVisibility(View.VISIBLE);
            binding.rltMsg.setVisibility(View.GONE);
            binding.touchPad.setCallback(this);
            binding.edtMessagePhoto.setText(message);
            binding.edtMessagePhoto.setSelection(message.length());
            for (StickerViewPost sticker : stickers) {
                binding.rltMsg.removeView(sticker);
            }
        } else {
            binding.lnrBody.setVisibility(View.GONE);
            binding.rltMsg.setVisibility(View.VISIBLE);
            binding.layoutPhoto.clearImage();
            binding.edtMessage.setText(message);
            for (StickerViewPost sticker : stickers) {
                binding.lnrBody.removeView(sticker);
            }
        }
        stickers.clear();
    }

    private void chooseImage() {
        if (AndroidUtil.verifyStoragePermissions(getActivity())) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, WelfareConst.RequestCode.GALLERY);
        }
    }

    private void addSticker(StampModel stamp) {
        for (StickerViewPost sticker : stickers) {
            sticker.unselectSticker();
        }
        StickerViewPost stickerViewPost = new StickerViewPost(getContext());
        stickerViewPost.setStickerPath(BuildConfig.HOST + stamp.stampPath);
        stickerViewPost.key = stamp.key;
        stickerViewPost.setCallback(this);
        if (canSendPhoto) {
            binding.lnrBody.addView(stickerViewPost);
        } else {
            binding.rltMsg.addView(stickerViewPost);
        }
        stickers.add(stickerViewPost);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case WelfareConst.RequestCode.GALLERY:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    long date = System.currentTimeMillis();
                    String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
                    String desPath = TCUtil.getFilesFolderPath() + filename;
                    mImagePath = WelfareUtil.getImagePath(activity, uri, true, desPath);
                    Uri uri1 = AndroidUtil.getUriFromFileInternal(activity, new File(mImagePath));
                    cropImage(uri1);
                } else {
                    Toast.makeText(activity, "Gallery is error", Toast.LENGTH_LONG).show();
                }
                break;
            case WelfareConst.RequestCode.PHOTO_CROP:
                binding.layoutPhoto.setImage(mImageUri.getPath(), template.templateType);
                break;
        }
    }

    private void cropImage(Uri imageUri) {
        long date = System.currentTimeMillis();
        String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
        String filePath = TCUtil.getFilesFolderPath() + "/" + filename;
        File imageFile = new File(filePath);
        try {
            imageFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        mImageUri = Uri.fromFile(imageFile);
        int imageWidth;
        int imageHeight;
        if (TcConst.POSITION_LEFT.equals(template.templateType)) {
            imageWidth = (int) (frameWidth / 2 - 50);
            imageHeight = (int) (frameHeight - 50);
        } else {
            imageWidth = (int) (frameHeight * 2 / 3);
            imageHeight = (int) (frameHeight * 2 / 3);
            ;
        }
        WelfareUtil.startCrop(this, imageUri, mImageUri, imageWidth, imageHeight);
    }

    private void checkNewCard() {
        if (CCConst.NONE.equals(member.key)) {
            showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message1), getString(android.R.string.ok), null);
        } else if (CCStringUtil.isEmpty(message) || hasTooManyLetters(message)) {
            showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message2, String.valueOf(MAX_LETTER)), getString(android.R.string.ok), null);
        } else if (this.template == null) {
            showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message3), getString(android.R.string.ok), null);
        } else {
            showConfirmDialog();
        }
    }

    private boolean hasTooManyLetters(String message) {
        return message.length() > MAX_LETTER;
    }

    private void showConfirmDialog() {
        final PostConfirmDialog dialog = new PostConfirmDialog();
        dialog.setReceiverName(member.userName);
        dialog.show(getFragmentManager(), null);
        dialog.setListeners(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                requestPostNewCard(dialog.isSecret());
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void requestPostNewCard(boolean isSecret) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postDate", CCFormatUtil.formatDate(new Date()));
            jsonObject.put("categoryId", 1);
            jsonObject.put("templateId", template.templateId);

            UserModel userModel = prefAccUtil.getUserPref();
            jsonObject.put("posterId", userModel.key);

            jsonObject.put("receiverId", member.key);
            jsonObject.put("message", binding.edtMessage.getText().toString());
            jsonObject.put("isSecret", isSecret);

            JSONArray jsonStickers = new JSONArray();
            for (StickerViewPost sticker : stickers) {
                JSONObject jsonSticker = new JSONObject();
                jsonSticker.put("key", sticker.getKey());
                jsonSticker.put("locationX", sticker.getLocationX());
                jsonSticker.put("locationY", sticker.getLocationY());
                jsonSticker.put("scale", sticker.getScale());
                jsonSticker.put("degree", sticker.getDegree());
                jsonStickers.put(jsonSticker);
            }
            jsonObject.put("stickerListString", jsonStickers.toString());

            if (canSendPhoto) {
                jsonObject.put("templateType", "PH");
                jsonObject.put("photoLocationX", binding.layoutPhoto.getPhotoLocationX());
                jsonObject.put("photoLocationY", binding.layoutPhoto.getPhotoLocationY());
                jsonObject.put("photoScale", binding.layoutPhoto.getPhotoScale());
            } else {
                jsonObject.put("templateType", "NM");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        if (canSendPhoto && mImageUri != null) {
            HashMap<String, File> photo = new HashMap<>();
            photo.put("photoFile", new File(mImageUri.getPath()));
            requestUpload(TcConst.API_POST_NEW_CARD, jsonObject, photo, true);
        } else {
            requestUpdate(TcConst.API_POST_NEW_CARD, jsonObject, true);
        }
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (TcConst.API_POST_NEW_CARD.equals(url)) {
            requestPostNewCardSuccess(response);
        } else {
            super.successUpdate(response, url);
        }
    }

    @Override
    protected void successUpload(JSONObject response, String url) {
        if (TcConst.API_POST_NEW_CARD.equals(url)) {
            requestPostNewCardSuccess(response);
        } else {
            super.successUpload(response, url);
        }
    }

    private void requestPostNewCardSuccess(JSONObject response) {
        showAlertDialog(getString(R.string.fragment_posted_confirm_success_title), getString(R.string.fragment_posted_confirm_success_message), getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickOkButtonAfterShowingSuccessDialog();
            }
        });
    }

    private void onClickOkButtonAfterShowingSuccessDialog() {
        ((WelfareActivity) activity).isInitData = true;
        onClickBackBtn();
    }

    @Override
    public void onSelectDeptDone(DeptModel deptModel) {
        this.department = deptModel;
        binding.deptName.setText(department.deptName);
        member = department.members.get(0);
        binding.userName.setText(member.userName);
    }

    private void log(String msg) {
        Log.e("PostTc", msg);
    }

    @Override
    public void onSelectUserDone(UserModel userModel) {
        this.member = userModel;
        binding.userName.setText(member.userName);
        if (userModel.key.equals(myself.key)) {
            binding.btnSend.setEnabled(false);
        } else {
            binding.btnSend.setEnabled(true);
        }
    }

    @Override
    public void onSelectCardDone(Template card) {
        this.template = card;
        this.canSendPhoto = card.templateType != null;
        changeLayoutCard();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            if (canSendPhoto && binding.edtMessagePhoto != null) {
                binding.edtMessagePhoto.setText(message);
            }
            if (!canSendPhoto && binding.edtMessage != null) {
                binding.edtMessage.setText(message);
            }
        }
    }

    @Override
    public void onDeleteStickerClick(StickerViewPost sticker) {
        binding.rltMsg.removeView(sticker);
        stickers.remove(sticker);
    }

    @Override
    public void onStickerClick(float x, float y, StickerViewPost sticker) {
        for (StickerViewPost stickerViewPost : stickers) {
            if (!sticker.equals(stickerViewPost)) {
                if (x > stickerViewPost.lowX && x < stickerViewPost.highX && y > stickerViewPost.lowY && y < stickerViewPost.highY) {
                    stickerViewPost.selectSticker();
                } else {
                    stickerViewPost.unselectSticker();
                }
            }
        }
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
        if (binding.layoutPhoto.hasImage()) {
            final ChoosePictureDialog dialog = new ChoosePictureDialog();
            dialog.setListeners(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    chooseImage();
                    dialog.dismiss();
                }
            }, new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show(getFragmentManager(), null);
        } else {
            chooseImage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onStampClick(StampModel stamp) {
        closeLayoutSticker();
        if (stickers.size() >= 50) {
            showLimitStickerDialog();
        } else {
            addSticker(stamp);
        }
    }

    private void showLimitStickerDialog() {
        final LimitStickerDialog dialog = new LimitStickerDialog();
        dialog.setListeners(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }, null);
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onStampCategoryClick(StampCategoryModel stampCategory) {
        stampAdapter.setStamps(stampCategory.stamps);
    }

    @Override
    protected void onClickBackBtn() {
        if (showLayoutSticker) {
            closeLayoutSticker();
        } else {
            super.onClickBackBtn();
        }
    }
}
