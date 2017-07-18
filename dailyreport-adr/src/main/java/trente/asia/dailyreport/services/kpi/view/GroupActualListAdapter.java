package trente.asia.dailyreport.services.kpi.view;

import java.util.List;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 5/13/2016.
 */
public class GroupActualListAdapter extends ArrayAdapter<GroupKpi>{

	private final OnGraphIconClickListener	listener;
	Context									context;
	int										layoutId;

	public GroupActualListAdapter(Context context, int resource, List<GroupKpi> objects, OnGraphIconClickListener listener){
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.listener = listener;
	}

	public interface OnGraphIconClickListener{

		public void onGraphIconClicked(GroupKpi groupKpi);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.lnrContainer = (LinearLayout)convertView.findViewById(R.id.item_my_report_container);
			viewHolder.txtGroupName = (TextView)convertView.findViewById(R.id.txt_group_name);
			viewHolder.imgChart = (ImageView)convertView.findViewById(R.id.img_item_group_actual_chart);

			viewHolder.txtPeriod = ((TextView)convertView.findViewById(R.id.group_actual_info_period));
			viewHolder.txtGoal = ((TextView)convertView.findViewById(R.id.group_actual_info_goal));
			viewHolder.txtTodayPerformance = ((TextView)convertView.findViewById(R.id.group_actual_info_today_performance));
			viewHolder.txtAchievementRate = ((TextView)convertView.findViewById(R.id.group_actual_info_achievement_rate));
			viewHolder.txtOverallPerformance = ((TextView)convertView.findViewById(R.id.txt_item_group_overall_performance));
			viewHolder.txtResult = ((TextView)convertView.findViewById(R.id.txt_item_group_actual_result));
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final GroupKpi groupKpi = getItem(position);
		viewHolder.txtGroupName.setText(groupKpi.name);
		String periodString = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(groupKpi.startDate, WelfareConst.WF_DATE_TIME)) + " ~ " + CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(groupKpi.endDate, WelfareConst.WF_DATE_TIME));
		viewHolder.txtPeriod.setText(periodString);
		viewHolder.txtGoal.setText(CCFormatUtil.formatAmount(groupKpi.goal) + " " + groupKpi.goalUnit);
		viewHolder.txtTodayPerformance.setText(CCFormatUtil.formatAmount(groupKpi.todayActualGroup) + " " + groupKpi.goalUnit);
		viewHolder.txtOverallPerformance.setText(CCFormatUtil.formatAmount(groupKpi.actual) + " " + groupKpi.goalUnit);
		viewHolder.txtAchievementRate.setText(CCFormatUtil.formatAmount(groupKpi.achievementRate) + "%");
		int toGoal = Integer.parseInt(groupKpi.toGoal);
		if(toGoal <= 0){ // achieved
			viewHolder.txtResult.setText("→" + context.getResources().getString(R.string.achieve_dialog_title));
		}else{
			viewHolder.txtResult.setText("→" + context.getResources().getString(R.string.fragmen_group_actual_needed_amount2, toGoal + groupKpi.goalUnit));
		}

		viewHolder.imgChart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				listener.onGraphIconClicked(groupKpi);
			}
		});

		return convertView;
	}

	public static void setReportStatsNumberColor(Context context, TextView txtNum, int num){
		if(num > 0){
			txtNum.setTextColor(ContextCompat.getColor(context, R.color.file_count));
		}else{
			txtNum.setTextColor(ContextCompat.getColor(context, R.color.file_count_zero));
		}
	}

	private class ViewHolder{

		LinearLayout	lnrContainer;
		ImageView		imgChart;
		TextView		txtGroupName;
		TextView		txtPeriod;
		TextView		txtGoal;
		TextView		txtTodayPerformance;
		TextView		txtAchievementRate;
		TextView		txtOverallPerformance;
		TextView		txtResult;
	}
}
