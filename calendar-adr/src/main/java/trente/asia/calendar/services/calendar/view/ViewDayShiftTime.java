package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;

/**
 * Created by viet on 2/24/2017.
 */

public class ViewDayShiftTime extends LinearLayout {
    public ViewDayShiftTime(Context context) {
        super(context);
    }

    public ViewDayShiftTime(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDayShiftTime(Context context, AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showCategoriesSigns(CalendarDayModel calendarDayModel) {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout shift = (LinearLayout) getChildAt(i);
            LinearLayout lnrSingsContainer = (LinearLayout) shift.getChildAt(0);
            TextView txtTime = (TextView) shift.getChildAt(1);
            List<String> categoryColors = getCategoryColors(txtTime.getText()
                    , calendarDayModel.schedules);

            for (String colorCode : categoryColors) {
                TextView textView = new TextView(getContext());
                textView.setText("\u2022");
                textView.setTextColor(Color.parseColor(colorCode));
                lnrSingsContainer.addView(textView);
            }
        }
    }

    private List<String> getCategoryColors(CharSequence text,
                                           List<ScheduleModel> schedules) {
        List<String> result = new ArrayList<>();
        for (ScheduleModel schedule : schedules) {
            String categoryColorCode = "#" + schedule.categoryId;
            if (result.indexOf(categoryColorCode) == -1) {
                result.add(categoryColorCode);
            }
        }
        return result;
    }
}
