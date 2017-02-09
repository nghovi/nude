package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/9/2017.
 */

public class HorizontalUserListView extends LinearLayout{

	public HorizontalUserListView(Context context){
		super(context);
	}

	public HorizontalUserListView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void inflateWith(List<UserModel> users){
		LinearLayout lnrUserList = (LinearLayout)findViewById(R.id.lnr_view_horizontal_user_list);
		lnrUserList.removeAllViews();
		LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(100,100);
		for(UserModel userModel : users){
			ImageView imgAvatar = new ImageView(getContext());
			imgAvatar.setPadding(20, 20, 20, 20);
			imgAvatar.setLayoutParams(vp);
			WfPicassoHelper.loadImage(getContext(), "https://tr-fukuri.jp/assets/images/fukuri_logo3.png", imgAvatar, null);
			lnrUserList.addView(imgAvatar);
		}
	}
}
