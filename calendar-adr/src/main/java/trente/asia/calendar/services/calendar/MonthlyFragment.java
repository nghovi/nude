package trente.asia.calendar.services.calendar;

import android.graphics.Color;
import android.widget.TextView;

import java.util.Date;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends PageContainerFragment {

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_monthly;
    }

    @Override
    protected void initView() {
        super.initView();
        lnrUserList.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void setActiveDate(int position) {
        Date activeDate = CsDateUtil.addMonth(TODAY, position -
                INITIAL_POSITION);
        prefAccUtil.set(ClConst.PREF_ACTIVE_DATE, CCFormatUtil
				.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeDate));

        String title = CCFormatUtil.formatDateCustom(WelfareConst
				.WL_DATE_TIME_12, activeDate);
        TextView txtHeaderTitle = (TextView) getView().findViewById(R.id
				.txt_id_header_title);
        txtHeaderTitle.setText(title);
    }

    @Override
    SchedulesPagerAdapter initPagerAdapter() {
        return new MonthlyCalendarPagerAdapter(getChildFragmentManager(), changeCalendarUserListener);
    }
}
