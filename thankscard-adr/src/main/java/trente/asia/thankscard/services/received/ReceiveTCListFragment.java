package trente.asia.thankscard.services.received;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;

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
public class ReceiveTCListFragment extends AbstractTCListFragment{

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_receive;
	}

	@Override
	public int getTitle(){
		return R.string.fragment_receive_tc_list_title;
	}

	@Override
	protected String getConstApi(){
		return TcConst.API_GET_RECEIVE_CARD_HISTORY;
	}

	@Override
	protected String getFilteredField(HistoryModel historyModel){
		return historyModel.receiverId;
	}

	@Override
	protected List<DeptModel> getDeptModelFilterValue(HistoryModel historyModel){
		// return historyModel.receiver_dept_list;
		return null;
	}

	@Override
	protected UserModel getUserModelFilterValue(HistoryModel historyModel){
		return createByUserId(historyModel.receiverId, historyModel.receiverId);
	}

	@Override
	protected int getDetailTCTitle(){
		return R.string.fragment_tc_detail_title_receive;
	}

	@Override
	protected void buildPostBtn(){
		Button btnPost = (Button)getView().findViewById(R.id.btn_fragment_month_post);
		btnPost.setVisibility(View.GONE);
	}

	@Override
	protected JSONObject getHistoryParam(){
		// String recDeptId = selectedDept != null && selectedDept.key != null && !selectedDept.key.equals(DeptModel.KEY_ALL) ? selectedDept.key :
		// null;
		String categoryId = selectedCategory == null ? TcConst.THANKS_CATEGORY_ID : selectedCategory.categoryId;
		String targetMonth = WelfareUtil.getYearMonthStr(year, month);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetMonth", targetMonth);
//			jsonObject.put("categoryId", categoryId);
			// jsonObject.put("recDeptId", recDeptId);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		return jsonObject;
	}
}
