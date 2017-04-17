package trente.asia.calendar.commons.views;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * Created by viet on 2/21/2017.
 */

public class NavigationHeader extends LinearLayout{

	public WfSlideMenuLayout	slideMenu;
	private ImageView			imgNavigation;
	private ImageView			imgRightBtn;

	public void setOnHeaderActionsListener(final OnAddBtnClickedListener listener){
		this.listener = listener;
		if(imgNavigation == null){
			imgNavigation = (ImageView)findViewById(R.id.img_navigator_header_left_btn);
			imgRightBtn = (ImageView)findViewById(R.id.img_navigator_header_right_btn);
		}

		imgNavigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				if(slideMenu != null){
					slideMenu.toggleMenu();
				}
			}
		});

		imgRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				listener.onAddBtnClick(null);
			}
		});
	}

	public void hideRightBtn(){
		findViewById(R.id.img_navigator_header_right_btn).setVisibility(View.INVISIBLE);
	}

	private OnAddBtnClickedListener	listener;

	public interface OnAddBtnClickedListener{

		public void onAddBtnClick(Date selectedDate);
	}

	public NavigationHeader(Context context){
		super(context);
	}

	public NavigationHeader(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public NavigationHeader(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	private TextView	txtHeaderTitle;
	private TextView	txtHeaderSubtitle;

	public void updateMainHeaderTitle(String title){
		if(txtHeaderTitle == null){
			txtHeaderTitle = (TextView)findViewById(R.id.txt_id_header_title);
		}

		// if(txtHeaderSubtitle == null){
		// txtHeaderSubtitle = (TextView)findViewById(R.id.txt_id_header_title_sub);
		// }
		if(!CCStringUtil.isEmpty(title)){
			txtHeaderTitle.setText(CCStringUtil.toUpperCase(title));
		}else{
			txtHeaderSubtitle = (TextView)findViewById(R.id.txt_id_header_title_sub);
			txtHeaderSubtitle.setTextSize(14);
			txtHeaderTitle.setVisibility(View.GONE);
		}
		// txtHeaderSubtitle.setText(subTitle);
	}
}
