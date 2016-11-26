package trente.asia.thankscard.services.rank.view;

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

import trente.asia.thankscard.R;

/**
 * Created by viet on 2/19/2016.
 */
public class MonthYearPickerDialog extends DialogFragment{

	private int maxMonth = 12;
	private int maxYear = -1;
	private int minYear = 1970;
	private int minMonth = 1;

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

	private int									mSelectedYear	= 0;
	private int									mSelectedMonth	= 0;

	private DatePickerDialog.OnDateSetListener	listener;

	public void setListener(DatePickerDialog.OnDateSetListener listener){
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog));
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		Calendar cal = Calendar.getInstance();

		View dialog = inflater.inflate(R.layout.widget_date_picker_dialog, null);
		final NumberPicker monthPicker = (NumberPicker)dialog.findViewById(R.id.picker_month);
		final NumberPicker yearPicker = (NumberPicker)dialog.findViewById(R.id.picker_year);


		monthPicker.setMinValue(minMonth);
		monthPicker.setMaxValue(maxMonth);
		monthPicker.setValue(mSelectedMonth != 0 ? mSelectedMonth : cal.get(Calendar.MONTH) + 1);


		//Year picker setting
		int year = mSelectedYear != 0 ? mSelectedYear : cal.get(Calendar.YEAR);
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

	public void setMaxMonth(int maxMonth) {
		this.maxMonth = maxMonth;
	}

	public void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}
}
