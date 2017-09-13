package trente.asia.thankscard.services.rank.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import trente.asia.thankscard.R;

/**
 * Created by VietNH on 20151204.
 */
public class TCTabLinearLayout extends LinearLayout{

	public Map<String, LinearLayout>	mTabs;
	public OnTabSelectedListener		mListener;
	public List<Integer>				listColor		= new ArrayList<>();
	public Map<String, Integer>			categoryColor	= new LinkedHashMap<String, Integer>();

	public interface OnTabSelectedListener{
		public void onTabSelected(String tabName);
	}

	public TCTabLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public TCTabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	// public TCTabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
	// super(context, attrs, defStyleAttr, defStyleRes);
	// }

	public TCTabLinearLayout(Context context){
		super(context);
	}

	public void initTabs(List<String> tabNames, OnTabSelectedListener listener, String defaultTabName){
		mListener = listener;
		mTabs = new HashMap<String, LinearLayout>();

		listColor.add(R.color.tc_category_thanks);
		listColor.add(R.color.tc_category_congrats);
		listColor.add(R.color.tc_category_good);

		LinearLayout tabsContainer = buildTabsContainer();
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int i = 0;
		for(String tabName : tabNames){
			LinearLayout lnrTab = buildTab(inflater, tabsContainer, tabName, i);
			mTabs.put(tabName, lnrTab);
			categoryColor.put(tabName, listColor.get(i).intValue());
			i++;
		}
		this.addView(tabsContainer, 0);
		this.switchToTab(defaultTabName != null ? defaultTabName : tabNames.get(0), false);
	}

	private void setOnTabClickListener(LinearLayout lnrTab, final String tabName){
		lnrTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				switchToTab(tabName, true);
			}
		});
	}

	private LinearLayout buildTabsContainer(){
		LinearLayout lnrTab = new LinearLayout(getContext());
		lnrTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		lnrTab.setOrientation(LinearLayout.HORIZONTAL);
		lnrTab.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		lnrTab.setVerticalGravity(Gravity.CENTER_VERTICAL);
		lnrTab.setBackgroundColor(Color.TRANSPARENT);
		// lnrTab.setPadding(6, 6, 6, 6);
		return lnrTab;
	}

	private LinearLayout buildTab(LayoutInflater inflater, LinearLayout tabsContainer, String tabName, int i){
		LinearLayout lnrTab = (LinearLayout)inflater.inflate(R.layout.tc_tab_linear_layout, null);
		lnrTab.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		TextView txtTabName = (TextView)lnrTab.findViewById(R.id.txt_tc_tab_linear_layout_name);
		txtTabName.setText(tabName);
		txtTabName.setTextColor(Color.TRANSPARENT);
		// txtTabName.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JennaSue.ttf"));
		setOnTabClickListener(lnrTab, tabName);

		WindowManager windowManager = (WindowManager)getContext().getSystemService(getContext().WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y / 15;

		switch(i){
		case 0:
			lnrTab.setBackgroundResource(R.drawable.tc_category_thanks);
			lnrTab.getLayoutParams().height = height;
			break;
		case 1:
			lnrTab.setBackgroundResource(R.drawable.tc_category_congrats);
			lnrTab.getLayoutParams().height = height;
			break;
		case 2:
			lnrTab.setBackgroundResource(R.drawable.tc_category_good);
			lnrTab.getLayoutParams().height = height;
			break;
		}
		tabsContainer.addView(lnrTab);
		return lnrTab;
	}

	private void switchToTab(String tabNameSelected, boolean triggerEvent){
		int tabColor = 0;
		for(String tabName : mTabs.keySet()){
			if(tabName.equals(tabNameSelected)){
				tabColor = categoryColor.get(tabName);
			}
		}

		for(String tabName : mTabs.keySet()){
			LinearLayout lnrTab = mTabs.get(tabName);
			if(tabName.equals(tabNameSelected)){
				lnrTab.findViewById(R.id.view_base_line).setBackgroundResource(tabColor);
				lnrTab.findViewById(R.id.lnr_tab).setBackgroundColor(Color.TRANSPARENT);
			}else{
				lnrTab.findViewById(R.id.view_base_line).setBackgroundResource(tabColor);
				lnrTab.findViewById(R.id.lnr_tab).setBackgroundColor(Color.TRANSPARENT);
			}
		}
		if(mListener != null && triggerEvent){
			mListener.onTabSelected(tabNameSelected);
		}
	}
}