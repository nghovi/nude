package trente.asia.thankscard.services.posted;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.fragments.dialogs.PostConfirmDialog;
import trente.asia.thankscard.fragments.dialogs.TemplateSelectionDialog;
import trente.asia.thankscard.services.common.model.Category;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.thankscard.services.mypage.model.NoticeModel;
import trente.asia.thankscard.utils.TCUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 2/15/2016.
 */
public class ThanksCardEditFragment extends AbstractTCFragment {

    public static final String RECEIVE_USER_ID = "RECEIVE_USER_ID";
    public static final String MESSAGE = "MESSAGE_FROM_OLD_POST";
    public final int MAX_LETTER = 75;

    private HistoryModel mHistoryModel;
    private Button btnConfirm;
    private NoticeModel noticeModel;

    private WfSpinner spnDeparment;
    private WfSpinner spnUserModel;
    private WfSpinner spnCategory;
    private DeptModel selectedDept;
    private UserModel selectedUser;
    private List<DeptModel> departments;
    private EditText edtMessage;

    private Category selectedCategory;
    private List<Category> categories;
    private Template template;
    private List<Template> templates;
    // private CategorySelectionDialog categorySelectiondialog;
    private TemplateSelectionDialog templateSelectionDialog;

    public void setSelectedDept(DeptModel selectedDept) {
        this.selectedDept = selectedDept;
    }

    public void setSelectedUser(UserModel selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setNoticeModel(NoticeModel noticeModel) {
        this.noticeModel = noticeModel;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_tc_edit;
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
        return R.id.lnr_view_common_footer_posted;
    }

    @Override
    public int getTitle() {
        return R.string.fragment_post_edit_title;
    }

    @Override
    public void buildBodyLayout() {
        spnDeparment = (WfSpinner) getView().findViewById(R.id.spn_fragment_post_edit_departure);
        spnUserModel = (WfSpinner) getView().findViewById(R.id.spn_fragment_post_edit_user);
        spnCategory = (WfSpinner) getView().findViewById(R.id.spn_fragment_post_category);

        // getSelectedUserModel();
        if (mHistoryModel == null) {
            mHistoryModel = new HistoryModel("", myself.key, myself.userName, TCUtil.getDateString(Calendar.getInstance().getTime(), TcConst.TC_DATE_FORMAT));
        } else {
            // edtMessage.setText(mHistoryModel.message);
        }
        if (CCStringUtil.isEmpty(mHistoryModel.receiverId)) {
            mHistoryModel.receiverId = selectedUser != null ? selectedUser.loginUserId : null;
        }

        buildSubmitBtn();
        template = new Template();
        template.templateId = prefAccUtil.get(TcConst.PREF_TEMPLATE_ID);
        template.templateUrl = prefAccUtil.get(TcConst.PREF_TEMPLATE_PATH);
    }

    @Override
    public void initData() {
        requestAccountInfo();
        requestTemplate();
    }

    private void loadNoticeData() {
        if (noticeModel != null) {
            for (DeptModel deptModel : departments) {
                if (TcConst.THANKS_NOTICE_TYPE_MANUAL.equals(noticeModel.noticeType)) {
                    if (deptModel.key.equals(noticeModel.noticeDept)) {
                        selectedDept = deptModel;
                        break;
                    }
                } else if (TcConst.THANKS_NOTICE_TYPE_BIRTH.equals(noticeModel.noticeType)) {
                    if (!CCCollectionUtil.isEmpty(deptModel.members)) {
                        for (UserModel userModel : deptModel.members) {
                            if (userModel.key.equals(noticeModel.noticeUser)) {
                                selectedUser = userModel;
                                selectedDept = deptModel;
                                selectedCategory = new Category(TcConst.CONGRAT_CATEGORY_ID, "Congrats!!", R.drawable.tc_category_congrats);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void requestAccountInfo() {
        JSONObject param = new JSONObject();
        requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, param, true);
    }

    private void buildTemplate(boolean keepCurrentMessage) {
        ImageView imgTemplate = (ImageView) getView().findViewById(R.id.img_fragment_post_edit_template);
        imgTemplate.setScaleType(ImageView.ScaleType.FIT_XY);
        edtMessage = (EditText) getView().findViewById(R.id.edt_fragment_post_edit_message);
        if (template != null && !CCStringUtil.isEmpty(template.templateUrl)) {
            imgTemplate.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            WfPicassoHelper.loadImage2(activity, BuildConfig.HOST, imgTemplate, template.templateUrl);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getView().findViewById(R.id.rlt_msg_background).setBackground(null);
            }
        } else {
            imgTemplate.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            getView().findViewById(R.id.rlt_msg_background).setBackgroundColor(getResources().getColor(R.color.tc_fragment_background));
        }
        final TextView txtCount = (TextView) getView().findViewById(R.id.txt_fragment_post_edit_count);
        edtMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtCount.setText(String.valueOf(MAX_LETTER - s.length()));
            }
        });
        Bundle data = getArguments();
        String currentMessage = edtMessage.getText().toString();
        if (keepCurrentMessage || !CCStringUtil.isEmpty(currentMessage)) {
            mHistoryModel.message = currentMessage;
        }
        // else if(data != null && data.getString(MESSAGE) != null){
        // mHistoryModel.message = data.getString(MESSAGE);
        // }
        edtMessage.setText(mHistoryModel.message);
    }

    private List<String> getDeptModelDisplayedValue(List<DeptModel> drDeptModels) {
        List<String> values = new ArrayList<>();
        for (DeptModel drDeptModel : drDeptModels) {
            values.add(drDeptModel.deptName);
        }
        return values;
    }

    private void onDeptSelected(int selectedPosition) {
        selectedDept = departments.get(selectedPosition);
        updateUserSpinner();
    }

    private void updateUserSpinner() {
        selectedUser = getSelectedUser(selectedDept.members, selectedUser);
        int selectedIndex = WelfareUtil.findUserInList(selectedDept.members, selectedUser);
        spnUserModel.setupLayout(getString(R.string.fragment_post_edit_user), getUserDisplayedValue(selectedDept.members), selectedIndex, new WfSpinner.OnDRSpinnerItemSelectedListener() {

            @Override
            public void onItemSelected(int selectedPosition) {
                selectedUser = selectedDept.members.get(selectedPosition);
                // check user is myself
                if (selectedUser.key.equals(myself.key)) {
                    btnConfirm.setEnabled(false);
                } else {
                    btnConfirm.setEnabled(true);
                }
            }
        }, true);
        if (selectedDept.members == null || selectedDept.members.size() == 0) {
            selectedUser = null;
        }
    }

    private List<String> getUserDisplayedValue(List<UserModel> userModels) {
        List<String> values = new ArrayList<>();
        if (userModels != null) {
            for (UserModel userModel : userModels) {
                values.add(userModel.userName);
            }
        }
        return values;
    }

    private void buildSpinners() {
        int selectedIndex = WelfareUtil.findDeptInList(departments, selectedDept);
        spnDeparment.setupLayout(getString(R.string.fragment_post_edit_dept), getDeptModelDisplayedValue(departments), selectedIndex,

                new WfSpinner.OnDRSpinnerItemSelectedListener() {

                    @Override
                    public void onItemSelected(int selectedPosition) {
                        onDeptSelected(selectedPosition);
                    }
                }, true);
    }

    private UserModel getSelectedUser(List<UserModel> users, UserModel previous) {
        if (previous == null || users == null || users.size() <= 0) {
            return null;
        }
        for (UserModel userModel : users) {
            if (userModel.key.equals(previous.key)) {
                return userModel;
            }
        }
        return null;
    }

    private UserModel createByUserId(String userId) {
        UserModel userModel = new UserModel();
        userModel.key = userId;
        return userModel;
    }

    private void constructThanksCard() {
        if (selectedUser != null) {
            mHistoryModel.receiverId = selectedUser.key;
            mHistoryModel.categoryId = String.valueOf(selectedCategory.categoryId);
            mHistoryModel.message = edtMessage.getText().toString();
        }
    }

    private void buildSubmitBtn() {
        btnConfirm = (Button) getView().findViewById(R.id.btn_fragment_post_edit_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkNewCard();
            }
        });
    }

    private void showConfirmDialog() {
        final PostConfirmDialog dialog = new PostConfirmDialog();
        dialog.setReceiverName(selectedUser.userName);
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

    private void log(String msg) {
        Log.e("Card Edit", msg);
    }

    private void buildCategorySelectionButton() {
        spnCategory.setupLayout(getString(R.string.fragment_post_edit_category), Category.getCategoryNameList(), 0,
                new WfSpinner.OnDRSpinnerItemSelectedListener() {

                    @Override
                    public void onItemSelected(int selectedPosition) {
                        selectedCategory = Category.getCategoryList().get(selectedPosition);
                    }
                }, true);

        categories = Category.getCategoryList();
    }

    private void buildTemplateSelectionButton() {
        if (templateSelectionDialog == null) {
            templateSelectionDialog = new TemplateSelectionDialog();
            templateSelectionDialog.setTemplates(this.templates);
            templateSelectionDialog.setDept(departments);
            View.OnClickListener okListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    templateSelectionDialog.dismiss();
                    onTemplateSelected();
                }
            };
            View.OnClickListener cancelListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    templateSelectionDialog.dismiss();
                }
            };
            templateSelectionDialog.setListeners(okListener, cancelListener);
        }

        TextView txtTemplate = (TextView) getView().findViewById(R.id.txt_fragment_post_edit_template);
        txtTemplate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                templateSelectionDialog.show(getFragmentManager(), null);
            }
        });
    }

    private void onTemplateSelected() {
        template = templateSelectionDialog.getSelectedTemplate();
        boolean keepCurrentMessage = true;
        buildTemplate(keepCurrentMessage);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (TcConst.API_POST_GET_RECEIVER_DEPARTMENT_INFO.equals(url)) {
            // requestReceiverDeptModelInfoSuccess(response);
        } else if (TcConst.API_POST_NEW_CARD.equals(url)) {
            requestPostNewCardSuccess(response);
        } else {
            super.successUpdate(response, url);
        }
    }

    private void requestTemplate() {
        JSONObject param = new JSONObject();
        requestLoad(TcConst.API_GET_TEMPLATE, param, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (TcConst.API_GET_TEMPLATE.equals(url)) {
            requestTemplateSuccess(response);
        } else if (WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)) {
            departments = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
            loadNoticeData();
            buildCategorySelectionButton();

            DeptModel deptModel = new DeptModel(CCConst.NONE, getString(R.string.chiase_common_none));
            departments.add(0, deptModel);
            // add none user to member list
            for (DeptModel dept : departments) {
                if (CCCollectionUtil.isEmpty(dept.members)) {
                    dept.members = new ArrayList<>();
                }
                dept.members.add(0, new UserModel(CCConst.NONE, getString(R.string.chiase_common_none)));
            }

            buildSpinners();
        } else {
            super.successLoad(response, url);
        }
    }

    private void requestTemplateSuccess(JSONObject response) {
        templates = CCJsonUtil.convertToModelList(response.optString("templates"), Template.class);
        buildTemplateSelectionButton();
        buildTemplate(false);
    }

    private void checkNewCard() {
        constructThanksCard();
        if (CCConst.NONE.equals(mHistoryModel.receiverId)) {
            showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message1), getString(android.R.string.ok), null);
        } else if (CCStringUtil.isEmpty(mHistoryModel.message) || hasTooManyLetters(mHistoryModel.message)) {
            showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message2, String.valueOf(MAX_LETTER)), getString(android.R.string.ok), null);
        } else if (this.template == null) {
            showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message3), getString(android.R.string.ok), null);
        } else {
            showConfirmDialog();
        }
    }

    private void requestPostNewCard(boolean isSecret) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postDate", CCFormatUtil.formatDate(new Date()));
            jsonObject.put("categoryId", mHistoryModel.categoryId);
            jsonObject.put("templateId", template.templateId);

            UserModel userModel = prefAccUtil.getUserPref();
            jsonObject.put("posterId", userModel.key);

            jsonObject.put("receiverId", mHistoryModel.receiverId);
            jsonObject.put("message", mHistoryModel.message);
            jsonObject.put("isSecret", isSecret);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        requestUpdate(TcConst.API_POST_NEW_CARD, jsonObject, true);
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

    private boolean hasTooManyLetters(String message) {
        return message.length() > MAX_LETTER;
    }

    // private void gotoConfirmFragment(){
    // PostedConfirmFragment postedConfirmFragment = new PostedConfirmFragment();
    // postedConfirmFragment.setDepts(departments);
    // mHistoryModel.setTemplate(this.template);
    // postedConfirmFragment.setHistoryModel(mHistoryModel);
    // ((MainActivity)getActivity()).addFragment(postedConfirmFragment);
    // }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        edtMessage = null;
        spnUserModel = null;
        spnDeparment = null;
        spnCategory = null;
        templateSelectionDialog = null;
    }

    public void setDepartments(List<DeptModel> departments) {
        this.departments = departments;
    }

    public void setCategory(Category category) {
        this.selectedCategory = category;
    }
}
