package trente.asia.calendar.commons.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import trente.asia.calendar.R;

/**
 * Created by hviet on 7/25/17.
 */

public class UserFacilityView extends LinearLayout{

	private ImageButton	btnUser;
	private ImageButton	btnFacility;

	public interface OnTabClickListener{

		public void onBtnUserClicked();

		public void onBtnFacilityClicked();
	}

	public UserFacilityView(Context context){
		super(context);
	}

	public void initChildren(final OnTabClickListener onTabClickListener){
		btnUser = (ImageButton)findViewById(R.id.btn_user);
		btnFacility = (ImageButton)findViewById(R.id.btn_facility);
		btnUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				btnUser.setBackgroundResource(R.drawable.cl_tab_user_on);
				btnFacility.setBackgroundResource(R.drawable.cl_tab_facility_off);
				onTabClickListener.onBtnUserClicked();
			}
		});
		btnFacility.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				btnUser.setBackgroundResource(R.drawable.cl_tab_user_off);
				btnFacility.setBackgroundResource(R.drawable.cl_tab_facility_on);
				onTabClickListener.onBtnFacilityClicked();
			}
		});
	}

	public UserFacilityView(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
	}

	public UserFacilityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

}
