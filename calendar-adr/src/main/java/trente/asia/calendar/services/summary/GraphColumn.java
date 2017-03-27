package trente.asia.calendar.services.summary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
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

	private static final int	BLOCK_WIDTH_PX	= WelfareUtil.dpToPx(60);
	private LinearLayout		lnrBlockContainer;
	private static double		MIN_HEIGHT_DP	= 20;
	private static double		MAX_HEIGHT_DP	= 150;
	private static int			MAX_WIDTH_PX	= WelfareUtil.dpToPx(60);

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
		setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		lnrBlockContainer = (LinearLayout)findViewById(R.id.lnr_block_container);
		addBlocks(getContext(), summaryModel, lnrBlockContainer, true);
		// TextView txtMonth = (TextView)findViewById(R.id.txt_graph_column_month);
		// txtMonth.setText(summaryModel.month);
	}

	public static void addBlocks(Context context, SummaryModel summaryModel, LinearLayout lnrBlockContainer, boolean isCalculateSize){
		for(CategoryModel categoryModel : summaryModel.categories){
			LinearLayout lnrBlock = new LinearLayout(context);
			int heightPx = getHeightPx(categoryModel);
			LayoutParams param;
			if(isCalculateSize){
				param = new LinearLayout.LayoutParams(BLOCK_WIDTH_PX, heightPx);
				lnrBlock.setGravity(Gravity.CENTER_HORIZONTAL);
			}else{
				// if(summaryModel.categories.size() < 8){
				param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
				// }else{
				// param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
				// }
				// lnrBlock.setWeightSum(1);
				lnrBlock.setGravity(Gravity.CENTER);
			}

			lnrBlock.setLayoutParams(param);
			lnrBlock.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));

			TextView txtHour = new TextView(context);
			txtHour.setText(categoryModel.hoursOfSchedule);
			txtHour.setTextColor(Color.WHITE);
			lnrBlock.addView(txtHour);
			lnrBlockContainer.addView(lnrBlock);
		}
	}

	public static int getHeightPx(CategoryModel categoryModel){
		int result = (int)(1.5 * Double.parseDouble(categoryModel.hoursOfSchedule) + MIN_HEIGHT_DP);
		result = result < MAX_HEIGHT_DP ? result : (int)MAX_HEIGHT_DP;
		return WelfareUtil.dpToPx(result);
	}
}
