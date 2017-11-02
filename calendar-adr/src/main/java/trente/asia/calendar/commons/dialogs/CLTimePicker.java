package nguyenhoangviet.vpcorp.calendar.commons.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import nguyenhoangviet.vpcorp.calendar.commons.OnTimePickerListener;
import nguyenhoangviet.vpcorp.calendar.R;

/**
 * Created by chi on 10/23/2017.
 */

public class CLTimePicker extends DialogFragment{

	private static final int		NUM_MAX			= 12;
	private static final int		TIME_INTERVAL	= 5;
	private OnTimePickerListener	callback;
	private boolean					startTime;
	private int						hour;
	private int						minute;
	private NumberPicker			hourPicker;
	private NumberPicker			minutePicker;

	public void setStartTime(boolean startTime){
		this.startTime = startTime;
	}

	public void setSelectedHour(int hour){
		this.hour = hour;
	}

	public void setSelectedMinute(int minute){
		this.minute = minute;
	}

	public void setCallback(OnTimePickerListener callback){
		this.callback = callback;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		// builder.setTitle(R.string.cl_time_picker);
		View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_time_picker, null);
		builder.setView(content);
		hourPicker = (NumberPicker)content.findViewById(R.id.hour_picker);
		minutePicker = (NumberPicker)content.findViewById(R.id.minute_picker);
		hourPicker.setMaxValue(23);
		hourPicker.setMinValue(0);
		hourPicker.setValue(hour);
		String[] displayValues = new String[NUM_MAX];
		for(int i = 0; i < NUM_MAX; i++){
			displayValues[i] = String.valueOf(i * TIME_INTERVAL);
		}
		minutePicker.setMinValue(0);
		minutePicker.setMaxValue(NUM_MAX - 1);
		minutePicker.setDisplayedValues(displayValues);
		minutePicker.setValue(minute / TIME_INTERVAL);
		builder.setNegativeButton(R.string.chiase_common_cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogInterface, int i){
			}
		});
		builder.setPositiveButton(R.string.chiase_common_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogInterface, int i){
				if(callback != null){
					callback.onTimePickerCompleted(hourPicker.getValue(), minutePicker.getValue() * TIME_INTERVAL);
				}
			}
		});

		return builder.create();
	}
}
