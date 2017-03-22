package trente.asia.calendar.services.summary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 3/21/2017.
 */

public class GraphColumn extends LinearLayout{

	private static final int	BLOCK_WIDTH_PX	= WelfareUtil.dpToPx(40);
	private LinearLayout		lnrBlockContainer;
	private double				MIN_HEIGHT_DP	= 20;
	private double				MAX_HEIGHT_DP	= 150;

	public GraphColumn(Context context){
		super(context);
	}

	public GraphColumn(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
	}

	public GraphColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public void initLayout(SummaryModel summaryModel){
		lnrBlockContainer = (LinearLayout)findViewById(R.id.lnr_block_container);

		for(CategoryModel categoryModel : summaryModel.categories){
			LinearLayout lnrBlock = new LinearLayout(getContext());
			int heightPx = getHeightPx(categoryModel);
			LayoutParams param = new LinearLayout.LayoutParams(BLOCK_WIDTH_PX, heightPx);
			lnrBlock.setLayoutParams(param);
			lnrBlock.setGravity(Gravity.CENTER_HORIZONTAL);
			lnrBlock.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));

			TextView txtHour = new TextView(getContext());
			txtHour.setText(categoryModel.hoursOfSchedule);
			txtHour.setTextColor(Color.WHITE);
			lnrBlock.addView(txtHour);
			lnrBlockContainer.addView(lnrBlock);
		}

		TextView txtMonth = (TextView)findViewById(R.id.txt_graph_column_month);
		txtMonth.setText(summaryModel.month);
	}

	private int getHeightPx(CategoryModel categoryModel){
		int result = (int)(1.5 * Integer.parseInt(categoryModel.hoursOfSchedule) + MIN_HEIGHT_DP);
		result = result < MAX_HEIGHT_DP ? result : (int)MAX_HEIGHT_DP;
		return WelfareUtil.dpToPx(result);
	}
}
