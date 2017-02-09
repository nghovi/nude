package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

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
		makeDummyData();
	}

    private void loadCalendarList(){
        JSONObject jsonObject = new JSONObject();
        requestLoad(WfUrlConst.WF_MSG_0001, jsonObject, false);
    }

	private void makeDummyData(){
		List<CalendarModel> lstCalendar = new ArrayList<>();
		lstCalendar.add(new CalendarModel("Calendar 01", ""));
		lstCalendar.add(new CalendarModel("Calendar 02", ""));

		CalendarAdapter calendarAdapter = new CalendarAdapter(activity, lstCalendar);
		lvCalendar.setAdapter(calendarAdapter);
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
