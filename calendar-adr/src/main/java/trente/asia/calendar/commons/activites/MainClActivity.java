package nguyenhoangviet.vpcorp.calendar.commons.activites;

import java.util.Date;

import com.bluelinelabs.logansquare.LoganSquare;

import android.os.Bundle;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.defines.ClConst;
import nguyenhoangviet.vpcorp.calendar.services.calendar.MonthlyFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.ScheduleDetailFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.ScheduleModel;
import nguyenhoangviet.vpcorp.calendar.services.user.ClLoginFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

public class MainClActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();
		prefAccUtil.set(ClConst.PREF_FILTER_TYPE, ClConst.PREF_FILTER_TYPE_USER);
		prefAccUtil.set(ClConst.PREF_ACTIVE_ROOM, "0");

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
			scheduleModel.startDate = CCDateUtil.makeDateCustom(dateStr, WelfareConst.WF_DATE_TIME_DATE);
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
