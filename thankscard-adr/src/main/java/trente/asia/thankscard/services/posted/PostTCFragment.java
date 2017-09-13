package trente.asia.thankscard.services.posted;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
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
import trente.asia.thankscard.services.posted.dialog.ChoosePictureDialog;
import trente.asia.thankscard.services.posted.listener.OnCroppingListener;
import trente.asia.thankscard.services.posted.dialog.CannotUseStickersDialog;
import trente.asia.thankscard.services.posted.view.StickerViewPost;
import trente.asia.thankscard.services.posted.adapter.StampAdapter;
import trente.asia.thankscard.services.posted.adapter.StampCategoryAdapter;
import trente.asia.thankscard.services.posted.dialog.LimitStickerDialog;
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

public class PostTCFragment extends AbstractTCFragment implements View.OnClickListener, SelectDeptFragment.OnSelectDeptListener, SelectUserFragment.OnSelectUserListener, SelectCardFragment.OnSelectCardListener, StickerViewPost.OnStickerListener, StampCategoryAdapter.OnStampCategoryAdapterListener, StampAdapter.OnStampAdapterListener, OnCroppingListener {

    public final int MAX_LETTER = 75;

    private List<Template> templates;
    private List<Template> photoTemplates;
    private List<Template> animationTemplates;
    private FragmentPostTcBinding binding;
    private Template template;
    private List<DeptModel> departments;
    private DeptModel department;
    private UserModel member;
    private String message;
    private List<StickerViewPost> stickers = new ArrayList<>();
    private String mImagePath;
    private boolean canSendPhoto = false;
    private List<StampCategoryModel> stampCategories = new ArrayList<>();
    private StampAdapter stampAdapter;
    private Realm mRealm;
    private boolean showLayoutSticker;
    private PreferencesSystemUtil preference;
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private boolean isBirthday = false;
    private boolean canUseStickers = false;
    private boolean hasPhoto = false;
    private View activityRootView;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private boolean openStickersFromBtn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        preference = new PreferencesSystemUtil(getContext());
        isBirthday = Boolean.parseBoolean(preference.get(TcConst.IS_BIRTHDAY));
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        validateButtons();

        float normalTextSize = Float.parseFloat(preference.get(TcConst.PREF_NORMAL_TEXT_SIZE));
        float photoTextSize = Float.parseFloat(preference.get(TcConst.PREF_PHOTO_TEXT_SIZE));

        binding.edtMessagePhoto.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoTextSize);
        binding.edtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, normalTextSize);
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Arial_BoldMT.ttf");
        binding.edtMessagePhoto.setTypeface(Typeface.MONOSPACE);
        binding.edtMessage.setTypeface(Typeface.MONOSPACE);

        if (department != null) {
            binding.deptName.setText(department.deptName);
        }

        if (member != null) {
            binding.userName.setText(member.userName);
        }

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
                    int textCount = MAX_LETTER - message.getBytes().length;
                    binding.txtCount.setText(String.valueOf(textCount));
                    binding.edtMessagePhoto.setText(message);
                    if (textCount < 0) {
                        binding.txtCount.setTextColor(Color.RED);
                    } else {
                        binding.txtCount.setTextColor(Color.BLACK);
                    }
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
                    int textCount = MAX_LETTER - message.getBytes().length;
                    binding.txtCount.setText(String.valueOf(textCount));
                    binding.edtMessage.setText(message);
                    if (textCount < 0) {
                        binding.txtCount.setTextColor(Color.RED);
                    } else {
                        binding.txtCount.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        binding.edtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.edtMessage.setBackgroundResource(R.drawable.edt_message_bgr);
                } else {
                    binding.edtMessage.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        binding.edtMessagePhoto.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.edtMessagePhoto.setBackgroundResource(R.drawable.edt_message_bgr);
                    binding.layoutSticker.setVisibility(View.GONE);
                } else {
                    binding.edtMessagePhoto.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        String date = DateFormat.format("yyyy/MM/dd", System.currentTimeMillis()).toString();
        binding.txtDateNormal.setText(date);
        binding.txtReceiverNormal.setText(getString(R.string.fragment_tc_detail_to, ""));
        binding.txtDatePhoto.setText(date);
        binding.txtReceiverPhoto.setText(getString(R.string.fragment_tc_detail_to, ""));

        setListenerToRootView();
        buildLayoutCard();
    }

    private void buildLayoutCard() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.imgCard.getLayoutParams();
        params.width = (int) Float.parseFloat(preference.get(TcConst.PREF_FRAME_WIDTH));
        params.height = (int) Float.parseFloat(preference.get(TcConst.PREF_FRAME_HEIGHT));
    }

    private void validateButtons() {
        int pointBronze = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_BRONZE));
        int pointSilver = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_SILVER));
        int totalPoint = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_TOTAL));

        if (isBirthday) {
            canUseStickers = true;
        } else {
            if (totalPoint < pointBronze) {
                canUseStickers = false;
            } else if (totalPoint < pointSilver) {
                canUseStickers = true;
            } else {
                canUseStickers = true;
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        requestAccountInfo();
        requestTemplate();
        buildTemplate();
        buildLayoutSticker();
    }

    public void setSelectedDepartment(DeptModel department) {
        this.department = department;
    }

    public void setSelectedUser(UserModel user) {
        this.member = user;
    }

    private void buildLayoutSticker() {
        int pointBronze = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_BRONZE));
        int pointSilver = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_SILVER));
        int pointGold = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_GOLD));
        int totalPoint = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_TOTAL));

        String serviceCode = "TC-B";
        if (totalPoint < pointBronze && !isBirthday) {
            return;
        } else if (totalPoint < pointSilver) {
            serviceCode = "TC-B";
        } else if (totalPoint < pointGold) {
            serviceCode = "TC-S";
        } else {
            serviceCode = "TC-G";
        }

        RealmResults<StampCategoryModel> categories = mRealm.where(StampCategoryModel.class).findAll();
        for (StampCategoryModel category : categories) {
            if ((!isBirthday && category.services.contains(serviceCode)) || (isBirthday && (category.services.contains("TC-B") || category.services.contains("TC-S") || category.services.contains("TC-G")))) {
                stampCategories.add(category);
            }
        }

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
            department = new DeptModel(CCConst.ALL, getString(R.string.chiase_common_all));
            department.members = new ArrayList<>();
            for (DeptModel dept : departments) {
                department.members.addAll(dept.members);
            }
            departments.add(0, department);
        } else {
            super.successLoad(response, url);
        }
    }

    private void requestTemplateSuccess(JSONObject response) {
        templates = CCJsonUtil.convertToModelList(response.optString("templates"), Template.class);
        photoTemplates = CCJsonUtil.convertToModelList(response.optString("photos"), Template.class);
        animationTemplates = CCJsonUtil.convertToModelList(response.optString("animations"), Template.class);
    }

    private void buildTemplate() {
        TCUtil.loadImageWithGlide(template.templateUrl, binding.imgCard);
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
                SelectUserFragment userFragment = new SelectUserFragment();
                userFragment.setCallback(this);
                userFragment.setDepartments(department.members, member);
                gotoFragment(userFragment);
                break;
            case R.id.lnr_select_card:
                showLayoutCards(templates, photoTemplates, animationTemplates);
                break;
            case R.id.lnr_select_sticker:
                if (canUseStickers) {
                    if (showLayoutSticker) {
                        closeLayoutSticker();
                    } else {
                        showLayoutSticker();

                    }
                } else {
                    new CannotUseStickersDialog().show(getFragmentManager(), null);
                }
                break;
            case R.id.lnr_select_photo:
                if (hasPhoto) {
                    showDialogChoosePicture();
                    return;
                }
                chooseImage();
                break;
            case R.id.btn_send:
                checkNewCard();
                break;
            case R.id.main_layout:
                if (showLayoutSticker) {
                    closeLayoutSticker();
                }
                hideSoftKeyboard();
                break;
            default:
                break;
        }
    }

    private void showDialogChoosePicture() {
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
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.edtMessage.getWindowToken(), 0);
    }

    private void showLayoutSticker() {
        hideSoftKeyboard();
        binding.layoutSticker.setVisibility(View.VISIBLE);
        binding.rltSelectDept.setVisibility(View.GONE);
        binding.rltSelectUser.setVisibility(View.GONE);
        showLayoutSticker = true;
        getYFromTop();
        openStickersFromBtn = true;
        log("showLayoutSticker");
    }

    private void closeLayoutSticker() {
        binding.layoutSticker.setVisibility(View.GONE);
        binding.rltSelectDept.setVisibility(View.VISIBLE);
        binding.rltSelectUser.setVisibility(View.VISIBLE);
        showLayoutSticker = false;
        getYFromTop();
    }

    private void getYFromTop() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        int[] location = new int[2];
                        binding.imgCard.getLocationOnScreen(location);
                        preference.set(TcConst.PREF_Y_FROM_TOP, String.valueOf(location[1]));
                        for (StickerViewPost sticker : stickers) {
                            sticker.invalidate();
                        }
                    }
                });
            }
        }, 50);
    }

    private void showLayoutCards(List<Template> templates, List<Template> photo, List<Template> animations) {
        SelectCardFragment cardFragment = new SelectCardFragment();
        cardFragment.setCards(templates, photo, animations);
        cardFragment.setShowConfirmDiaglog(canSendPhoto);
        cardFragment.setCallback(this);
        gotoFragment(cardFragment);
    }

    private void changeLayoutCard() {
        buildTemplate();
        if (canSendPhoto) {
            binding.lnrBody.setVisibility(View.VISIBLE);
            binding.rltMsg.setVisibility(View.GONE);
            binding.edtMessagePhoto.setText(message);
            binding.edtMessagePhoto.setSelection(message.length());

            for (StickerViewPost sticker : stickers) {
                binding.rltMsg.removeView(sticker);
                binding.lnrBody.removeView(sticker);
            }
        } else {
            binding.lnrBody.setVisibility(View.GONE);
            binding.rltMsg.setVisibility(View.VISIBLE);
            binding.edtMessage.setText(message);

            for (StickerViewPost sticker : stickers) {
                binding.lnrBody.removeView(sticker);
                binding.rltMsg.removeView(sticker);
            }
            hasPhoto = false;
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
        getYFromTop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case WelfareConst.RequestCode.GALLERY:
                Uri uri;
                if (data != null) {
                    uri = data.getData();
                    long date = System.currentTimeMillis();
                    String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
                    String desPath = TCUtil.getFilesFolderPath() + filename;
                    mImagePath = WelfareUtil.getImagePath(activity, uri, true, desPath);
                    CroppingFragment croppingFragment = new CroppingFragment();
                    croppingFragment.setImagePath(mImagePath, BuildConfig.HOST + template.layerUrl);
                    croppingFragment.setCallback(this);
                    gotoFragment(croppingFragment);
                } else {
                    Toast.makeText(activity, "Gallery is error", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void checkNewCard() {
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        final PostConfirmDialog dialog = new PostConfirmDialog();
        dialog.setReceiverName(member == null ? "null" : member.userName);
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
            if (member == null) {
                jsonObject.put("receiverId", null);
            } else {
                jsonObject.put("receiverId", member.key);
            }
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
                jsonObject.put("photoLocationX", "0");
                jsonObject.put("photoLocationY", "0");
                jsonObject.put("photoScale", "1");
            } else {
                jsonObject.put("templateType", "NM");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        if (canSendPhoto) {
            HashMap<String, File> photo = new HashMap<>();
            photo.put("photoFile", new File(mImagePath));
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
        if (member == null || !department.members.contains(member)) {
            binding.userName.setText(getString(R.string.tc_unselected));
            binding.txtReceiverNormal.setText(getString(R.string.fragment_tc_detail_to, ""));
            binding.txtReceiverPhoto.setText(getString(R.string.fragment_tc_detail_to, ""));
            binding.btnSend.setEnabled(false);
            binding.btnSend.setAlpha(0.5f);
            member = null;
        }
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
        binding.txtReceiverNormal.setText(getString(R.string.fragment_tc_detail_to, userModel.userName));
        binding.txtReceiverPhoto.setText(getString(R.string.fragment_tc_detail_to, userModel.userName));
        binding.btnSend.setEnabled(true);
        binding.btnSend.setAlpha(1);
    }

    @Override
    public void onSelectCardDone(Template card) {
        this.template = card;
        this.canSendPhoto = card.templateType != null;
        binding.layoutPhoto.setImageBitmap(null);
        if (canSendPhoto) {
            binding.lnrSelectPhoto.setVisibility(View.VISIBLE);
        } else {
            binding.lnrSelectPhoto.setVisibility(View.INVISIBLE);
        }
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
        binding.lnrBody.removeView(sticker);
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
    public void onDestroy() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onStampClick(StampModel stamp) {
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

    @Override
    public void onCroppingCompleted(String imagePath) {
        binding.layoutPhoto.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        mImagePath = imagePath;
        hasPhoto = true;
    }

    public void setListenerToRootView() {
        activityRootView = getView().findViewById(R.id.content);
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean isOpen = false;
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.mainLayout.getLayoutParams();
                if (heightDiff > WelfareUtil.dpToPx(100)) {
                    binding.rltSelectDept.setVisibility(View.GONE);
                    binding.rltSelectUser.setVisibility(View.GONE);
                    params.topMargin = (int) getResources().getDimension(R.dimen.tc_top_margin);
                    isOpen = true;
                    if (showLayoutSticker) {
                        binding.layoutSticker.setVisibility(View.GONE);
                        showLayoutSticker = false;
                    }

                    binding.mainLayout.setLayoutParams(params);
                    log("open keyboard");
                } else if (isOpen) {
                    binding.edtMessage.clearFocus();
                    binding.edtMessagePhoto.clearFocus();
                    binding.rltSelectDept.setVisibility(View.VISIBLE);
                    binding.rltSelectUser.setVisibility(View.VISIBLE);
                    params.topMargin = 0;
                    isOpen = false;
                    if (openStickersFromBtn) {
                        showLayoutSticker();
                        openStickersFromBtn = false;
                    }
                    log("close keyboard");
                }
            }
        };
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }
}
