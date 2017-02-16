package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

	private static final String	SELECTED_CALENDAR_STRING	= "SELECTED_CALENDAR_STRING";
	private ListView			lvCalendar;
	private List<CalendarModel>	selectedCalendars			= new ArrayList<>();
	private List<CalendarModel>	calendars;

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
		lvCalendar.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				onClickCalendar(view, position);
			}
		});
	}

	private void onClickCalendar(View lnrCalendar, int position){
		boolean check = getCheckedStatus(position);
		// lvCalendar.setItemChecked(position, !check);
		CalendarModel calendar = calendars.get(position);
		if(check){
			lnrCalendar.findViewById(trente.asia.welfare.adr.R.id.img_checked).setVisibility(View.GONE);
			selectedCalendars.remove(calendar);
		}else{
			lnrCalendar.findViewById(trente.asia.welfare.adr.R.id.img_checked).setVisibility(View.VISIBLE);
			selectedCalendars.add(calendar);
		}

		String selectedCalendarIds = getSelectedCalendarIds();
		prefAccUtil.set(SELECTED_CALENDAR_STRING, selectedCalendarIds);
	}

	private String getSelectedCalendarIds(){
		List<String> selectedCalendarIds = new ArrayList<>();
		for(CalendarModel calendarModel : selectedCalendars){
			selectedCalendarIds.add(calendarModel.key);
		}
		return TextUtils.join(",", selectedCalendarIds);
	}

	private boolean getCheckedStatus(int position){
		CalendarModel calendarModel = calendars.get(position);
		for(CalendarModel calendar : selectedCalendars){
			if(calendar.key.equals(calendarModel.key)){
				return true;
			}
		}
		return false;
	}

	@Override
	protected void initData(){
		loadCalendarList();
	}

	private void loadCalendarList(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", myself.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CL_CAL_0001, jsonObject, false);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_CAL_0001.equals(url)){
			onLoadCalendarsSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onLoadCalendarsSuccess(JSONObject response){
		calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		buildSelectedCalendars();
		if(!CCCollectionUtil.isEmpty(calendars)){
			CalendarAdapter calendarAdapter = new CalendarAdapter(activity, calendars);
			lvCalendar.setAdapter(calendarAdapter);
			for(CalendarModel calendarModel : selectedCalendars){
				int position = calendarAdapter.findPosition4Code(calendarModel.key);
				lvCalendar.setItemChecked(position, true);
			}
		}
	}

	private void buildSelectedCalendars(){
		List<String> selectedCalendarIds = Arrays.asList(prefAccUtil.get(SELECTED_CALENDAR_STRING).split(","));
		for(CalendarModel calendarModel : calendars){
			if(selectedCalendarIds.indexOf(calendarModel.key) >= 0){
				checkAndAddSelectedCalendar(calendarModel);
			}
		}
	}

	private void checkAndAddSelectedCalendar(CalendarModel calendarModel){
		for(CalendarModel calendar : selectedCalendars){
			if(calendar.key.equals(calendarModel.key)){
				return;
			}
		}
		selectedCalendars.add(calendarModel);
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
