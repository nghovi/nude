package trente.asia.calendar.commons.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.DailySummaryPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * QkChiaseDialog
 *
 * @author TrungND
 */
public class ClDailySummaryDialog extends ChiaseDialog{

	private Context						mContext;
	private ViewPager					viewPagerSchedule;
	private TextView					txtActiveDate;
	private DailySummaryPagerAdapter	pagerAdapter;

	private final int					ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	private final Date					TODAY		= Calendar.getInstance().getTime();

	public ClDailySummaryDialog(Context context, List<ScheduleModel> lstSchedule){
		super(context);
		this.setContentView(R.layout.dialog_common_daily_summary);
		this.mContext = context;

		txtActiveDate = (TextView)this.findViewById(R.id.txt_id_active_date);
		viewPagerSchedule = (ViewPager)this.findViewById(R.id.view_pager_id_schedule);

		pagerAdapter = new DailySummaryPagerAdapter(mContext);
		viewPagerSchedule.setAdapter(pagerAdapter);
		viewPagerSchedule.setCurrentItem(ACTIVE_PAGE);
		setActiveDate(ACTIVE_PAGE);

		viewPagerSchedule.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				setActiveDate(position);
			}
		});
	}

	private void setActiveDate(int position){
		Date activeDate = CsDateUtil.addDate(TODAY, position - ACTIVE_PAGE);
		txtActiveDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeDate));
	}
}
