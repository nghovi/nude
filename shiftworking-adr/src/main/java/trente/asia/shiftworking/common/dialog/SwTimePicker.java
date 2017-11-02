package nguyenhoangviet.vpcorp.shiftworking.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.NumberPicker;

import java.io.StringBufferInputStream;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnTimePickerListener;
import nguyenhoangviet.vpcorp.shiftworking.databinding.DialogTimePickerBinding;

/**
 * Created by tien on 9/20/2017.
 */

public class SwTimePicker extends DialogFragment{

	private static final int		NUM_MAX			= 4;
	private static final int		TIME_INTERVAL	= 15;
	private OnTimePickerListener	callback;
	private boolean					startTime;
	private int						hour;
	private int						minute;

	public void setStartTime(boolean startTime){
		this.startTime = startTime;
	}

	;

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
		builder.setTitle(R.string.sw_time_picker);
		final DialogTimePickerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_time_picker, null, false);
		builder.setView(binding.getRoot());
		if(startTime){
			binding.hourPicker.setMaxValue(23);
		}else{
			binding.hourPicker.setMaxValue(47);
		}
		binding.hourPicker.setMinValue(0);
		binding.hourPicker.setValue(hour);
		String[] displayValues = new String[NUM_MAX];
		for(int i = 0; i < NUM_MAX; i++){
			displayValues[i] = String.valueOf(i * TIME_INTERVAL);
		}
		binding.minutePicker.setMinValue(0);
		binding.minutePicker.setMaxValue(NUM_MAX - 1);
		binding.minutePicker.setDisplayedValues(displayValues);
		binding.minutePicker.setValue(minute / TIME_INTERVAL);
		builder.setNegativeButton(R.string.chiase_common_cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogInterface, int i){
			}
		});
		builder.setPositiveButton(R.string.chiase_common_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogInterface, int i){
				if(callback != null){
					callback.onTimePickerCompleted(binding.hourPicker.getValue(), binding.minutePicker.getValue() * TIME_INTERVAL);
				}
			}
		});

		return builder.create();
	}
}
