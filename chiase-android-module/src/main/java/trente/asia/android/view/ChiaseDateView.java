package trente.asia.android.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.R;
import trente.asia.android.util.CAMsgUtil;

/**
 * Created by TrungND on 07/09/2014.
 */
public class ChiaseDateView extends EditText{

	private ChiaseDateView		chiase	= this;

	public String				mName;

	private DatePickerDialog	datePickerDialog;

	public ChiaseDateView(Context context, String name){
		super(context, null);
		mName = name;
	}

	public ChiaseDateView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		int attrsResourceIdArray[] = {R.attr.mName};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CAMsgUtil.convertArray2List(attrsResourceIdArray);
		mName = t.getString(list.indexOf(R.attr.mName));

		Calendar calendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
				Date date = CCDateUtil.makeDate(year, monthOfYear + 1, dayOfMonth);
				chiase.setText(CCFormatUtil.formatDate(date));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view){
				String dateString = CCStringUtil.toString(chiase.getText());
				if(!CCStringUtil.isEmpty(dateString)){
					Date date = CCDateUtil.makeDate(dateString);
					Calendar calendar = CCDateUtil.makeCalendar(date);
					datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				}else{
					Calendar calendar = Calendar.getInstance();
					datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				}
				datePickerDialog.show();
			}
		});
		this.setFocusable(false);
	}

	public String getmName(){
		return mName;
	}

	public void setmName(String mName){
		this.mName = mName;
	}
}
