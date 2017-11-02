package nguyenhoangviet.vpcorp.dailyreport.dialogs;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import nguyenhoangviet.vpcorp.dailyreport.R;

/**
 * Created by viet on 2/19/2016.
 */
public class MonthYearPickerDialog extends DialogFragment{

	public void setMaxMonth(int maxMonth){
		this.maxMonth = maxMonth;
	}

	private int	maxYear		= -1;
	private int	maxMonth	= 12;
	private int	minYear		= 2014;
	private int	minMonth	= 1;

	// private static final int MAX_YEAR = 20;

	public void setMinYear(int minYear){
		this.minYear = minYear;
	}

	public void setMinMonth(int minMonth){
		this.minMonth = minMonth;
	}

	public void setmSelectedYear(int mSelectedYear){
		this.mSelectedYear = mSelectedYear;
	}

	public void setmSelectedMonth(int mSelectedMonth){
		this.mSelectedMonth = mSelectedMonth;
	}

	public void setmSelectedTime(int year, int month){
		this.mSelectedYear = year;
		this.mSelectedMonth = month;
	}

	public int getmSelectedYear(){
		return mSelectedYear;
	}

	public int getmSelectedMonth(){
		return mSelectedMonth;
	}

	private int									mSelectedYear	= -1;
	private int									mSelectedMonth	= -1;

	private DatePickerDialog.OnDateSetListener	listener;

	public void setListener(DatePickerDialog.OnDateSetListener listener){
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light));
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		Calendar cal = Calendar.getInstance();

		View dialog = inflater.inflate(R.layout.widget_date_picker_dialog, null);
		final NumberPicker monthPicker = (NumberPicker)dialog.findViewById(R.id.picker_month);
		final NumberPicker yearPicker = (NumberPicker)dialog.findViewById(R.id.picker_year);

		monthPicker.setWrapSelectorWheel(false);

		int year = mSelectedYear > -1 ? mSelectedYear : cal.get(Calendar.YEAR);
		if(minYear > 0){
			yearPicker.setMinValue(minYear);
		}
		yearPicker.setMaxValue(maxYear > 0 ? maxYear : cal.get(Calendar.YEAR) + 1);
		yearPicker.setWrapSelectorWheel(false);
		yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal){
				setMonthRangeByYear(newVal, monthPicker);
			}
		});

		yearPicker.setValue(year);

		setMonthRangeByYear(year, monthPicker);
		monthPicker.setValue(mSelectedMonth != -1 ? mSelectedMonth : cal.get(Calendar.MONTH) + 1);

		builder.setView(dialog)
						// Add action buttons
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id){
								mSelectedYear = yearPicker.getValue();
								mSelectedMonth = monthPicker.getValue();
								listener.onDateSet(null, yearPicker.getValue(), mSelectedMonth, mSelectedYear);

							}
						}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id){
								MonthYearPickerDialog.this.getDialog().cancel();
							}
						});
		return builder.create();
	}

	private void setMonthRangeByYear(int year, NumberPicker monthPicker){
		if(year == minYear){
			monthPicker.setMinValue(minMonth);
			if(minYear == maxYear){
				monthPicker.setMaxValue(maxMonth);
			}else{
				monthPicker.setMaxValue(12);
			}
		}else if(minYear < year){
			monthPicker.setMinValue(1);
			if(year == maxYear){
				monthPicker.setMaxValue(maxMonth);
			}else{
				monthPicker.setMaxValue(12);
			}
		}
		monthPicker.setWrapSelectorWheel(false);
	}

	public void setMaxYear(int maxYear){
		this.maxYear = maxYear;
	}
}
