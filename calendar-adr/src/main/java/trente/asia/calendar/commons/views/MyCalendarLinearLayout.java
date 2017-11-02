package nguyenhoangviet.vpcorp.calendar.commons.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.view.layout.CheckableLinearLayout;
import nguyenhoangviet.vpcorp.calendar.BuildConfig;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.CalendarModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * MyCalendarLinearLayout
 *
 * @author TrungND
 */

public class MyCalendarLinearLayout extends CheckableLinearLayout{

	private ImageView	imgMyAvatar;
	private ImageView	imgMyCalendarCheck;

	private TextView	txtMyCalendarName;

	public MyCalendarLinearLayout(Context context){
		super(context);
//		initView();
	}

	public MyCalendarLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
//		initView();
	}

	public MyCalendarLinearLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
//		initView();
	}

	public void initView(){
		imgMyAvatar = (ImageView)this.findViewById(R.id.img_id_my_avatar);
		imgMyCalendarCheck = (ImageView)this.findViewById(R.id.img_id_my_calendar_checked);
		txtMyCalendarName = (TextView)this.findViewById(R.id.txt_id_my_calendar_name);
	}

	/*
	 * @see android.widget.Checkable#setChecked(boolean)
	 */
    @Override
	public void setChecked(boolean isChecked){
		this.isChecked = isChecked;
		for(Checkable c : checkableViews){
			c.setChecked(isChecked);
		}

		if(mOnCheckedChangeListener != null){
			mOnCheckedChangeListener.onCheckedChanged(this, isChecked);
		}
		if(isChecked){
			imgMyCalendarCheck.setVisibility(VISIBLE);
		}else{
			imgMyCalendarCheck.setVisibility(GONE);
		}

		refreshDrawableState();
	}

	public void setMyCalendar(CalendarModel calendarModel){
		txtMyCalendarName.setText(calendarModel.calendarName);
		if(!CCStringUtil.isEmpty(calendarModel.imagePath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + calendarModel.imagePath, imgMyAvatar, null);
		}
	}
}
