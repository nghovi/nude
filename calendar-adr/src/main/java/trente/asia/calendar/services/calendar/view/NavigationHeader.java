package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 2/21/2017.
 */

public class NavigationHeader extends LinearLayout{

	public boolean	isUpdated	= false;

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

	public void updateHeaderTitles(String title, String subTitle){
		if(txtHeaderTitle == null){
			txtHeaderTitle = (TextView)findViewById(R.id.txt_id_header_title);
		}

		if(txtHeaderSubtitle == null){
			txtHeaderSubtitle = (TextView)findViewById(R.id.txt_id_header_title_sub);
		}

		txtHeaderTitle.setText(title);
		txtHeaderSubtitle.setText(subTitle);
	}
}
