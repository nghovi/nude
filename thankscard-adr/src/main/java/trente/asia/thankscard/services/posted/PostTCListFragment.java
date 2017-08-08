package trente.asia.thankscard.services.posted;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.fragments.AbstractTCListFragment;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

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
//			jsonObject.put("categoryId", categoryId);
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
}
