package trente.asia.calendar.services.summary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.SummaryModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 3/21/2017.
 */

public class GraphColumn extends LinearLayout{

	private static final int	BLOCK_WIDTH_PX	= WelfareUtil.dpToPx(40);
	private LinearLayout		lnrBlockContainer;

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

		for(CategoryModel categoryModel : summaryModel.categoryModelList){
			LinearLayout lnrBlock = new LinearLayout(getContext());
			int heightPx = getHeightPx(categoryModel);
			LayoutParams param = new LinearLayout.LayoutParams(BLOCK_WIDTH_PX, heightPx);
			lnrBlock.setLayoutParams(param);
			lnrBlock.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));
			lnrBlockContainer.addView(lnrBlock);
		}
		// LayoutInflater inflater = (LayoutInflater)getContext()
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View graphColumn = inflater.inflate(R.layout.graph_column, null);
		// addView(graphColumn);
	}

	private int getHeightPx(CategoryModel categoryModel){
		int x = 50;
		int result = (int)(1.5 * x + 50);
		return WelfareUtil.dpToPx(result);
	}
}
