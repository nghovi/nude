package nguyenhoangviet.vpcorp.thankscard.services.posted;

import java.util.List;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.fragments.AbstractTCFragment;
import nguyenhoangviet.vpcorp.thankscard.services.common.TCDetailFragment;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.HistoryModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * Created by viet on 2/15/2016.
 */
public class PostedConfirmFragment extends AbstractTCFragment{

	private HistoryModel historyModel;

	public void setDepts(List<DeptModel> depts){
		this.depts = depts;
	}

	private List<DeptModel> depts;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_post_confirm;
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
		return R.string.fragment_post_confirm_title;
	}

	@Override
	public void buildBodyLayout(){
		TCDetailFragment.buildTCFrame(getActivity(), getView().findViewById(R.id.page_posted_confirm_fragment), historyModel, true);

		getView().findViewById(R.id.btn_fragment_post_confirm_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				cancelNewCard();
			}
		});
		getView().findViewById(R.id.btn_fragment_post_confirm_submit).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				submitNewCard();
			}
		});
	}

	private void cancelNewCard(){
		onClickBackBtn();
	}

	private void submitNewCard(){
		requestPostNewCard();
	}

	private void requestPostNewCard(){
		// callPostApi(TcConst.API_POST_NEW_CARD, getParam(), new Api.OnApiSuccessObserver() {
		//
		// @Override
		// public void onSuccess(JSONObject response){
		// requestPostNewCardSuccess(response);
		// }
		//
		// @Override
		// public void onFailure(JSONObject repsone){
		// commonApiFailure(repsone);
		// }
		// }, loadingDialog);
	}

	private void requestPostNewCardSuccess(JSONObject response){
		showAlertDialog(getString(R.string.fragment_posted_confirm_success_title), getString(R.string.fragment_posted_confirm_success_message), getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which){
				backToPostedFragment();
			}
		});
	}

	private void backToPostedFragment(){
		onClickFooterItemMyPage();
	}

	// private JSONObject getParam(){
	// return getJsonBuilder().add("categoryId", historyModel.cateogryId).add("templateId", historyModel.template.templateId).add("receiverId",
	// historyModel.receiverId).add("message", historyModel.message).getJsonObj();
	// }

	public void setHistoryModel(HistoryModel historyModel){
		this.historyModel = historyModel;
	}
}
