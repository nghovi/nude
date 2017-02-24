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
import android.widget.Checkable;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.android.listener.CsOnCheckedChangeListener;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.views.MyCalendarLinearLayout;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarListener;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.view.CalendarAdapter;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * CalendarListFragment
 *
 * @author TrungND
 */
public class CalendarListFragment extends AbstractClFragment{

	private ListView					lvCalendar;
	private List<CalendarModel>			lstSelectedCalendar	= new ArrayList<>();
	private List<CalendarModel>			lstCalendar;
	private List<CalendarModel>			lstCalendarWithoutMyCalendar;

	private MyCalendarLinearLayout		lnrMyCalendar;

	private CalendarAdapter				calendarAdapter;
	private OnChangeCalendarListener	changeCalendarListener;

	public void setOnChangeCalendarListener(OnChangeCalendarListener onChangeCalendarListener){
		this.changeCalendarListener = onChangeCalendarListener;
	}

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
		lvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				CalendarModel calendarModel = (CalendarModel)parent.getItemAtPosition(position);
				boolean isCheck = getCheckedStatus(position);
				onClickCalendar(calendarModel, isCheck);
			}
		});

		getView().findViewById(R.id.btn_id_my_calendar).setOnClickListener(this);
		getView().findViewById(R.id.btn_id_all).setOnClickListener(this);
		lnrMyCalendar = (MyCalendarLinearLayout)getView().findViewById(R.id.lnr_id_my_calendar);
		lnrMyCalendar.initView();
		lnrMyCalendar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				boolean isChecked = lnrMyCalendar.isChecked();
				lnrMyCalendar.setChecked(!isChecked);
			}
		});
		lnrMyCalendar.setOnCheckedChangeListener(new CsOnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(Checkable view, boolean isChecked){
				onClickCalendar(lstCalendar.get(0), isChecked);
			}
		});
	}

	private void onClickCalendar(CalendarModel calendarModel, boolean isChecked){
		if(isChecked){
			lstSelectedCalendar.add(calendarModel);
		}else{
			lstSelectedCalendar.remove(calendarModel);
		}
		saveSelectedCalendarToPref();
		if(changeCalendarListener != null){
			changeCalendarListener.onChangeCalendarListener();
		}
	}

	private boolean getCheckedStatus(int position){
		CalendarModel calendarModel = lstCalendar.get(position);
		for(CalendarModel calendar : lstSelectedCalendar){
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
		lstCalendar = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		if(!CCCollectionUtil.isEmpty(lstCalendar)){
			buildSelectedCalendars();

			lnrMyCalendar.setMyCalendar(lstCalendar.get(0));
			lstCalendarWithoutMyCalendar = new ArrayList<>();
			for(int index = 0; index < lstCalendar.size(); index++){
				if(index != 0){
					lstCalendarWithoutMyCalendar.add(lstCalendar.get(index));
				}
			}

			if(!CCCollectionUtil.isEmpty(lstCalendarWithoutMyCalendar)){
				calendarAdapter = new CalendarAdapter(activity, lstCalendarWithoutMyCalendar);
				lvCalendar.setAdapter(calendarAdapter);

				for(CalendarModel calendarModel : lstSelectedCalendar){
					int position = calendarAdapter.findPosition4Id(calendarModel.key);
					lvCalendar.setItemChecked(position, true);
				}
			}
			if(lstSelectedCalendar.contains(lstCalendar.get(0))){
				lnrMyCalendar.setChecked(true);
			}
		}
	}

	private void buildSelectedCalendars(){
		List<String> selectedCalendarIds = Arrays.asList(prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING).split(","));
		for(CalendarModel calendarModel : lstCalendar){
			if(selectedCalendarIds.indexOf(calendarModel.key) >= 0){
				checkAndAddSelectedCalendar(calendarModel);
			}
		}
	}

	private void checkAndAddSelectedCalendar(CalendarModel calendarModel){
		for(CalendarModel calendar : lstSelectedCalendar){
			if(calendar.key.equals(calendarModel.key)){
				return;
			}
		}
		lstSelectedCalendar.add(calendarModel);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_id_my_calendar:
			selectMyCalendarOnly();
			break;
		case R.id.btn_id_all:
			selectAllCalendars();
			break;
		default:
			break;
		}
	}

	private void selectAllCalendars(){
		for(int i = 0; i < lstCalendar.size(); i++){
			CalendarModel calendarModel = lstCalendar.get(i);
			lvCalendar.setItemChecked(i, true);
			int position = calendarAdapter.findPosition4Id(calendarModel.key);
			lvCalendar.setItemChecked(position, true);
			checkAndAddSelectedCalendar(calendarModel);
		}
		saveSelectedCalendarToPref();
	}

	private void selectMyCalendarOnly(){
		for(int i = 0; i < lstCalendar.size(); i++){
			CalendarModel calendarModel = lstCalendar.get(i);
			int position = calendarAdapter.findPosition4Id(calendarModel.key);
			if(calendarModel.isMyself){
				checkAndAddSelectedCalendar(calendarModel);
				lvCalendar.setItemChecked(position, true);
			}else{
				lstSelectedCalendar.remove(calendarModel);
				lvCalendar.setItemChecked(position, false);
			}
		}
		saveSelectedCalendarToPref();
	}

	private void saveSelectedCalendarToPref(){
		String selectedCalendarIds = getSelectedCalendarIds();
		prefAccUtil.set(ClConst.SELECTED_CALENDAR_STRING, selectedCalendarIds);
	}

	private String getSelectedCalendarIds(){
		List<String> selectedCalendarIds = new ArrayList<>();
		for(CalendarModel calendarModel : lstSelectedCalendar){
			selectedCalendarIds.add(calendarModel.key);
		}
		return TextUtils.join(",", selectedCalendarIds);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
