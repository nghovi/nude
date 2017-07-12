package trente.asia.thankscard.services.posted;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.databinding.FragmentPostTcBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 7/12/2017.
 */

public class PostTCFragment extends AbstractTCFragment{

	private List<Template>			templates;
	private FragmentPostTcBinding	binding;
	private Template template;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_tc, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_post_tc;
	}

	@Override
	public boolean hasBackBtn(){
		return true;
	}

	@Override
	public boolean hasSettingBtn(){
		return true;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public int getTitle(){
		return R.string.fragment_post_edit_title;
	}

	@Override
	public void buildBodyLayout(){
		template = new Template();
		template.templateId = prefAccUtil.get(TcConst.PREF_TEMPLATE_ID);
		template.templateUrl = prefAccUtil.get(TcConst.PREF_TEMPLATE_PATH);
	}

	@Override
	protected void initData(){
		super.initData();
		requestTemplate();
		buildTemplate(false);
	}

	private void requestTemplate(){
		JSONObject param = new JSONObject();
		requestLoad(TcConst.API_GET_TEMPLATE, param, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(TcConst.API_GET_TEMPLATE.equals(url)){
			requestTemplateSuccess(response);
		}else if(WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)){
			// departments = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			// loadNoticeData();
			// buildCategorySelectionButton();
			//
			// DeptModel deptModel = new DeptModel(CCConst.NONE, getString(R.string.chiase_common_none));
			// departments.add(0, deptModel);
			// // add none user to member list
			// for (DeptModel dept : departments) {
			// if (CCCollectionUtil.isEmpty(dept.members)) {
			// dept.members = new ArrayList<>();
			// }
			// dept.members.add(0, new UserModel(CCConst.NONE, getString(R.string.chiase_common_none)));
			// }
			//
			// buildSpinners();
		}else{
			super.successLoad(response, url);
		}
	}

	private void requestTemplateSuccess(JSONObject response){
		templates = CCJsonUtil.convertToModelList(response.optString("templates"), Template.class);
	}

	private void buildTemplate(boolean keepCurrentMessage){
        WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + template.templateUrl, binding.imgCard, null);
	}

}
