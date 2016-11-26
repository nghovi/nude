package trente.asia.android.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

/**
 * Created by CuongNV on 10/3/2014.
 */
public class CustomTimePickerDialog extends TimePickerDialog{

	// private final static int TIME_PICKER_INTERVAL = 30;
	private TimePicker				timePicker;
	private final OnTimeSetListener	callback;

	public Integer					startHour;

	public Integer					endHour;

	public Integer					intervalMinute;

	public boolean					is24h;

	public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView, Integer startHour, Integer endHour, Integer intervalMilute){
		super(context, callBack, hourOfDay, minute / intervalMilute, is24HourView);
		this.callback = callBack;

		this.is24h = is24HourView;
		this.startHour = startHour;
		this.endHour = endHour;
		this.intervalMinute = intervalMilute;
	}

	@Override
	public void onClick(DialogInterface dialog, int which){
		if(callback != null && timePicker != null){
			timePicker.clearFocus();
			callback.onTimeSet(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute() * intervalMinute);
		}
	}

	@Override
	protected void onStop(){
	}

	@Override
	public void onAttachedToWindow(){
		super.onAttachedToWindow();
		try{
			Class<?> classForid = Class.forName("com.android.internal.R$id");
			Field timePickerField = classForid.getField("timePicker");
			this.timePicker = (TimePicker)findViewById(timePickerField.getInt(null));
			// set hour
			Field hour = classForid.getField("hour");
			NumberPicker mHourSpinner = (NumberPicker)timePicker.findViewById(hour.getInt(null));
			mHourSpinner.setMinValue(startHour);
			mHourSpinner.setMaxValue(endHour);
			List<String> displayedHourValues = new ArrayList<String>();
			for(int i = startHour; i <= endHour; i++){
				displayedHourValues.add(String.format("%02d", i));
			}
			mHourSpinner.setDisplayedValues(displayedHourValues.toArray(new String[0]));

			// set time
			Field minute = classForid.getField("minute");
			NumberPicker mMinuteSpinner = (NumberPicker)timePicker.findViewById(minute.getInt(null));
			mMinuteSpinner.setMinValue(0);
			mMinuteSpinner.setMaxValue((60 / intervalMinute) - 1);
			List<String> displayedMinuteValues = new ArrayList<String>();
			for(int i = 0; i < 60; i += intervalMinute){
				displayedMinuteValues.add(String.format("%02d", i));
			}
			mMinuteSpinner.setDisplayedValues(displayedMinuteValues.toArray(new String[0]));
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
