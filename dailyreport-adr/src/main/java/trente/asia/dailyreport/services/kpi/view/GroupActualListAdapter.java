package trente.asia.dailyreport.services.kpi.view;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.other.OthersFragment;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.utils.DRUtil;

/**
 * Created by viet on 5/13/2016.
 */
public class GroupActualListAdapter extends ArrayAdapter<GroupKpi>{

	Context	context;
	int		layoutId;

	public GroupActualListAdapter(Context context, int resource, List<GroupKpi> objects){
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.lnrContainer = (LinearLayout)convertView.findViewById(R.id.item_my_report_container);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_my_report_date);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		GroupKpi groupKpi = getItem(position);

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
		LinearLayout	lnrComment;
		LinearLayout	lnrFile;
		LinearLayout	lnrIcons;
		TextView		txtDate;
		TextView		txtGoalPeriod;
		TextView		txtGroupGoal;
		TextView		txtCheckedNum;
		TextView		txtLikedNum;
		TextView		txtLastComment;
		TextView		txtCommentedNum;
		public TextView	txtFileNum;

	}
}
