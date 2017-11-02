package nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.android.view.ChiaseCheckableImageView;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * CommonMonthView
 *
 * @author TrungND
 * on 2016/10/17
 */
public class CommonMonthView extends LinearLayout{

	private Context					mContext;
	public ChiaseCheckableImageView	imgBack;
	public ChiaseCheckableImageView	imgNext;
	public Button					btnThisMonth;
	public TextView					txtMonth;

	public Date						workMonth;

	public CommonMonthView(Context context){
		super(context);
		this.mContext = context;
	}

	public CommonMonthView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public CommonMonthView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void initialization(){
		imgBack = (ChiaseCheckableImageView)this.findViewById(R.id.btn_id_back);
		imgNext = (ChiaseCheckableImageView)this.findViewById(R.id.btn_id_next);
		btnThisMonth = (Button)this.findViewById(R.id.img_id_this_month);
		txtMonth = (TextView)this.findViewById(R.id.txt_id_month);

		workMonth = WelfareUtil.makeMonthWithFirstDate();
	}
}
