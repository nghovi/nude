package trente.asia.dailyreport.dialogs;

import java.util.Calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by viet on 5/16/2016.
 */
public class DRDatePickerDialog extends DialogFragment{

	private int										year, month, day;

	android.app.DatePickerDialog.OnDateSetListener	listener;

	public void setListener(android.app.DatePickerDialog.OnDateSetListener listener){
		this.listener = listener;
	}

	public void setCurrentDate(int year, int month, int day){
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final Calendar c = Calendar.getInstance();
		return new android.app.DatePickerDialog(getActivity(), listener, year, month, day);
	}
}
