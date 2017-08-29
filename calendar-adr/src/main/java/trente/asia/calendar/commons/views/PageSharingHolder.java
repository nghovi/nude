package trente.asia.calendar.commons.views;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.services.calendar.view.CalendarDayView;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/23/2017.
 */

public class PageSharingHolder{

	private Context			mContext;
	public int				selectedPagePosition;
	public boolean			isRefreshUserList	= true;
	public boolean			isLoadingSchedules	= false;

	private CalendarDayView	clickedDayView;

	public PageSharingHolder(){
	}

	public void setClickedDayView(CalendarDayView clickedDayView){
		this.clickedDayView = clickedDayView;
	}

	public void cancelPreviousClickedDayView(){
		if(this.clickedDayView != null){
			this.clickedDayView.setSelected(false);
		}
	}

	public Date getClickedDate(){
		if(clickedDayView != null){
			return clickedDayView.getDate();
		}
		return null;
	}
}
