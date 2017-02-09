package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.view.CalendarAdapter;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class CalendarListFragment extends AbstractClFragment{

	private ListView lvCalendar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_calendar_list, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		lvCalendar = (ListView)getView().findViewById(R.id.lsv_id_calendar);
	}

	@Override
	protected void initData(){
		loadCalendarList();
	}

	private void loadCalendarList(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(WfUrlConst.WF_CL_CAL_0001, jsonObject, false);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_CAL_0001.equals(url)){
            List<CalendarModel> lstCalendar = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
            if(!CCCollectionUtil.isEmpty(lstCalendar)){
                CalendarAdapter calendarAdapter = new CalendarAdapter(activity, lstCalendar);
                lvCalendar.setAdapter(calendarAdapter);
            }
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
