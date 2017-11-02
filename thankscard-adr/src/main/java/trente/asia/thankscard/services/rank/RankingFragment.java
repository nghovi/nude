package nguyenhoangviet.vpcorp.thankscard.services.rank;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import nguyenhoangviet.vpcorp.thankscard.BuildConfig;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.fragments.AbstractTCFragment;
import nguyenhoangviet.vpcorp.welfare.adr.dialog.WfProfileDialog;
import nguyenhoangviet.vpcorp.thankscard.services.rank.model.RankModel;
import nguyenhoangviet.vpcorp.thankscard.services.rank.view.RankingListAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 2/15/2016.
 */
public class RankingFragment extends AbstractTCFragment implements DatePickerDialog.OnDateSetListener,View.OnClickListener{

	private Date				searchMonth;
	private ListView			lsvRanking;
	private List<RankModel>		lstRanking	= new ArrayList<>();
	private RankingListAdapter	adapter;

	private TextView			txtDateTitle;
	private TextView			txtDateValue;
	private Button				btnBack;
	private Button				btnNext;
	private WfProfileDialog		mDlgProfile;

	public boolean hasBackBtn(){
		return false;
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_ranking;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_ranking;
	}

	@Override
	public int getTitle(){
		return R.string.fragment_ranking_title;
	}

	@Override
	public boolean hasSettingBtn(){
		return true;
	}

	@Override
	public void buildBodyLayout(){
		lsvRanking = (ListView)getView().findViewById(R.id.lsv_id_ranking);
		txtDateTitle = (TextView)getView().findViewById(R.id.txt_id_date_title);
		txtDateValue = (TextView)getView().findViewById(R.id.txt_id_date_value);
		btnBack = (Button)getView().findViewById(R.id.btn_view_common_pager_back);
		btnNext = (Button)getView().findViewById(R.id.btn_view_common_pager_next);

		searchMonth = CCDateUtil.makeDateWithFirstday(Calendar.getInstance().getTime());
		buildSelectMonthBtn();
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);

		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);
		lsvRanking.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				RankModel model = (RankModel)parent.getItemAtPosition(position);
				mDlgProfile.show(BuildConfig.HOST, model.userName, model.avatarPath);
			}
		});
	}

	private void buildSelectMonthBtn(){
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v){
				Calendar searchMonthCalendar = CCDateUtil.makeCalendar(searchMonth);
				showMonthPickerDialog(RankingFragment.this, searchMonthCalendar.get(Calendar.YEAR), searchMonthCalendar.get(Calendar.MONTH) + 1);

			}
		};
		txtDateTitle.setOnClickListener(listener);
		txtDateValue.setOnClickListener(listener);
	}

	@Override
	public void initData(){
		loadRankingInfo();
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
		searchMonth = CCDateUtil.makeDate(year, monthOfYear, 1);
		loadRankingInfo();
	}

	private void loadRankingInfo(){
		String formatSearchMonth = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, searchMonth);
		Calendar searchMonthCalendar = CCDateUtil.makeCalendar(searchMonth);
		txtDateValue.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_HYPHEN_MM, searchMonthCalendar.getTime()));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetMonth", formatSearchMonth);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestLoad(TcConst.API_GET_RANKING, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(TcConst.API_GET_RANKING.equals(url)){
			List<RankModel> lstPost = CCJsonUtil.convertToModelList(response.optString("postRankings"), RankModel.class);
			List<RankModel> lstReceive = CCJsonUtil.convertToModelList(response.optString("recvRankings"), RankModel.class);
			lstRanking.clear();
			lstRanking.add(new RankModel(getString(R.string.page_ranking_post)));
			if(!CCCollectionUtil.isEmpty(lstPost)){
				lstRanking.addAll(lstPost);
			}

			lstRanking.add(new RankModel(getString(R.string.page_ranking_receive)));
			if(!CCCollectionUtil.isEmpty(lstReceive)){
				lstRanking.addAll(lstReceive);
			}

			if(adapter == null){
				adapter = new RankingListAdapter(activity, lstRanking);
				lsvRanking.setAdapter(adapter);
			}else{
				// adapter.addAll(lstRanking);
				adapter.notifyDataSetChanged();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_view_common_pager_back:
			searchMonth = WelfareUtil.addMonth(searchMonth, -1);
			loadRankingInfo();
			break;

		case R.id.btn_view_common_pager_next:
			searchMonth = WelfareUtil.addMonth(searchMonth, 1);
			loadRankingInfo();
			break;

		default:
			break;
		}
	}
}
