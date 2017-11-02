package nguyenhoangviet.vpcorp.thankscard.services.posted;

import android.os.Bundle;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.activities.MainActivity;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.fragments.AbstractTCListFragment;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.HistoryModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 2/15/2016.
 */

public class PostTCListFragment extends AbstractTCListFragment{

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_posted;
	}

	@Override
	public int getTitle(){
		return R.string.fragment_post_tc_list_title;
	}

	@Override
	protected String getConstApi(){
		return TcConst.API_GET_POST_CARD_HISTORY;
	}

	@Override
	protected String getFilteredField(HistoryModel historyModel){
		return historyModel.posterId;
	}

	@Override
	protected List<DeptModel> getDeptModelFilterValue(HistoryModel historyModel){
		// return historyModel.poster_dept_list;
		return null;
	}

	@Override
	protected JSONObject getHistoryParam(){
		String posDeptId = selectedDept != null && selectedDept.key != null && !selectedDept.key.equals(DeptModel.KEY_ALL) ? selectedDept.key : null;
		String categoryId = selectedCategory == null ? TcConst.THANKS_CATEGORY_ID : selectedCategory.categoryId;
		String targetMonth = WelfareUtil.getYearMonthStr(year, month);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetMonth", targetMonth);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	protected UserModel getUserModelFilterValue(HistoryModel historyModel){
		return createByUserId(historyModel.posterId, historyModel.posterId);
	}

	@Override
	protected int getDetailTCTitle(){
		return R.string.fragment_tc_detail_title_pos;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (((MainActivity) activity).loadData) {
			initData();
			((MainActivity) activity).loadData = false;
		}
	}
}
