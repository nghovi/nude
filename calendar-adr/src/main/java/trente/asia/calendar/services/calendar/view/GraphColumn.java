package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.SummaryModel;

/**
 * Created by viet on 3/21/2017.
 */


public class GraphColumn extends LinearLayout{

	private LinearLayout lnrBlockContainer;

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
		lnrBlockContainer = (LinearLayout) findViewById(R.id.lnr_block_container);

		LayoutParams param = new LinearLayout.LayoutParams(ViewGroup
				.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		for (CategoryModel categoryModel: summaryModel.categoryModelList) {
			LinearLayout lnrBlock = new LinearLayout(getContext());
			lnrBlock.setBackgroundColor(Color.RED);
			lnrBlockContainer.addView(lnrBlock);
		}
		// LayoutInflater inflater = (LayoutInflater)getContext()
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View graphColumn = inflater.inflate(R.layout.graph_column, null);
		// addView(graphColumn);
	}
}
