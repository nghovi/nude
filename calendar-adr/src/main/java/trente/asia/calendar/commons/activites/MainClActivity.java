package trente.asia.calendar.commons.activites;

import android.os.Bundle;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.MonthlyFragment;
import trente.asia.calendar.services.calendar.ScheduleDetailFragment;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.user.ClLoginFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

public class MainClActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();

		if(!CCStringUtil.isEmpty(userModel.key)){
			Bundle mExtras = getIntent().getExtras();
			if(mExtras != null){
				showFragment(mExtras);
			}else{
				addFragment(new MonthlyFragment());
			}
		}else{
			addFragment(new ClLoginFragment());
		}
	}

	private void showFragment(Bundle mExtras){
		String serviceCode = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE);
		if(WelfareConst.NotificationType.CL_NOTI_NEW_SCHEDULE.equals(serviceCode)){
			String key = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY);
			String dateStr = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_PARENT_KEY);
			ScheduleDetailFragment scheduleDetailFragment = new ScheduleDetailFragment();
			ScheduleModel scheduleModel = new ScheduleModel();
			scheduleModel.startDate = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME, CCDateUtil.makeDateCustom(dateStr, WelfareConst.WF_DATE_TIME_DATE));
			scheduleModel.endDate = scheduleModel.startDate;
			scheduleModel.key = key;
			scheduleDetailFragment.setSchedule(scheduleModel);
			scheduleDetailFragment.isClickNotification = true;
			addFragment(scheduleDetailFragment);
		}else{
			addFragment(new MonthlyFragment());
		}
	}
}
