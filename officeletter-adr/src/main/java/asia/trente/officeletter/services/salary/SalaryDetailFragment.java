package asia.trente.officeletter.services.salary;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import asia.trente.officeletter.commons.utils.OLUtils;
import asia.trente.officeletter.databinding.FragmentSalaryDetailBinding;
import asia.trente.officeletter.services.salary.adapter.ItemValueAdapter;
import asia.trente.officeletter.services.salary.model.ItemValueModel;
import asia.trente.officeletter.services.salary.model.SalaryGroupModel;
import asia.trente.officeletter.services.salary.model.SalaryModel;
import trente.asia.android.util.AndroidUtil;
import trente.asia.android.util.DownloadFileManager;
import trente.asia.android.util.OpenDownloadedFile;
import trente.asia.android.view.ChiaseDownloadFileDialog;

/**
 * Created by tien on 8/22/2017.
 */

public class SalaryDetailFragment extends AbstractOLFragment implements View.OnClickListener{

	private FragmentSalaryDetailBinding	binding;
	private ItemValueAdapter			adapter	= new ItemValueAdapter();
	private int							salaryId;
	private SalaryModel					salaryModel;

	public void setSalaryId(int salaryId){
		this.salaryId = salaryId;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_salary_detail, container, false);
			binding = DataBindingUtil.bind(mRootView);
		}
		return mRootView;
	}

	@Override
	protected int getFooterItemId(){
		return R.id.lnr_view_common_footer_salary;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, "Salary Detail", R.drawable.ic_download);
		binding.rlvGroupSalary.setAdapter(adapter);
		binding.rlvGroupSalary.setLayoutManager(new LinearLayoutManager(getContext()));
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
	}

	@Override
	protected void initData(){
		super.initData();
		loadGroupItem();
	}

	private void loadGroupItem(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", salaryId);
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(OLConst.API_OL_SALARY_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(OLConst.API_OL_SALARY_DETAIL.equals(url)){
			try{
				salaryModel = LoganSquare.parse(response.optString("salary"), SalaryModel.class);
				List<ItemValueModel> itemValues = new ArrayList<>();
				for(SalaryGroupModel salaryGroup : salaryModel.salaryGroups){
					itemValues.add(new ItemValueModel(salaryGroup.groupName));
					for(ItemValueModel itemValue : salaryGroup.itemValues){
						itemValue.groupName = salaryGroup.groupName;
						itemValues.add(itemValue);
					}
				}
				initHeader(R.drawable.wf_back_white, salaryModel.salaryMonth.substring(0, 7), R.drawable.ic_download);
				adapter.setItemValues(itemValues);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			OLUtils.downloadFile(activity, salaryModel.attachment.fileName, host + salaryModel.attachment.fileUrl);
			break;
		}
	}



	private void log(String msg){
		Log.e("SalaryDetailFragment", msg);
	}
}
