package nguyenhoangviet.vpcorp.dailyreport.dialogs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import nguyenhoangviet.vpcorp.android.view.ChiaseDateTimeView;

/**
 * Created by viet on 5/18/2016.
 */
public class DRDateTimePickerDialog extends ChiaseDateTimeView{

	private NumberPicker	minutePicker;
	private List<String>	mLstValue;

	private final int		NUMBER_MINUTES_1_HOUR	= 60;
	private int				mTimeInterval;
	private boolean			isShowTimePicker		= true;

	public interface OnDateTimeSetListener{

		public void onDateTimeSet(Calendar calendar);
	}

	public void setListener(final OnDateTimeSetListener listener){
		this.listener = listener;
		this.setDateTimeListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				DRDateTimePickerDialog.this.setCommonDateTime();
				Calendar c = DRDateTimePickerDialog.this.getDateTime();
				listener.onDateTimeSet(c);
				DRDateTimePickerDialog.this.alertDialog.dismiss();
			}
		});
	}

	public void setTimePickerVisible(boolean isShowTimePicker){
		this.isShowTimePicker = isShowTimePicker;
	}

	private OnDateTimeSetListener listener;

	public DRDateTimePickerDialog(Context context){
		super(context);
	}

	public void setTimePickerInterval(int timeInterval){

		this.mTimeInterval = timeInterval;
		mLstValue = new ArrayList<>();
		// for (int i = 0; i < NUMBER_MINUTES_1_HOUR; i += timeInterval) {
		// mLstValue.add(String.format("%02d", i));
		// }
		// final int valueNum = mLstValue.size();
		minutePicker = getMinuteSpinner(timePicker);
		if(null != minutePicker){
			minutePicker.setMinValue(0);

			// ToDo Trung: android version <= 4.0 -> exception
			// if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			// minutePicker.setMaxValue(valueNum - 1);
			// minutePicker.setWrapSelectorWheel(true);
			// } else {
			final NumberPicker hourPicker = getHourSpinner(timePicker);
			// minutePicker.setMaxValue(valueNum * 2 - 1);
			// NumberPicker seem not work on API < 15 when number of
			// displayed values < 5, so
			// for interval = 15m (displayed value = 0, 15, 30, 45) we
			// double the number of displayed values
			// ref: https://code.google.com/p/android/issues/detail?id=24630

			for(int i = 0; i < NUMBER_MINUTES_1_HOUR; i += timeInterval){
				mLstValue.add(String.format("%02d", i));
			}
			minutePicker.setMaxValue(mLstValue.size() - 1);

			// minutePicker.setWrapSelectorWheel(true);
			minutePicker.setWrapSelectorWheel(false);
			minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal){
					// if ((oldVal == valueNum - 1 && newVal == valueNum)
					// || (oldVal == valueNum * 2 - 1 && newVal == 0)) {
					// hourPicker.setValue(hourPicker.getValue() + 1);
					// } else if ((oldVal == valueNum && newVal == valueNum - 1)
					// || (oldVal == 0 && newVal == valueNum * 2 - 1)) {
					// hourPicker.setValue(hourPicker.getValue() - 1);
					// }
				}
			});
			// }
			minutePicker.setDisplayedValues(mLstValue.toArray(new String[mLstValue.size()]));
		}
	}

	@Override
	public void setCommonDateTime(){

		dateTime = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), convertValue2Minute());

	}

	@Override
	public void show(){
		Calendar calendar = Calendar.getInstance();
		if(dateTime != null){
			calendar = dateTime;
			datePicker.init(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), null);
		}
		if(isShowTimePicker){
			timePicker.setVisibility(View.VISIBLE);
			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		}else{
			timePicker.setVisibility(View.GONE);
		}
		alertDialog.show();
	}

	public void setCancelListener(final View.OnClickListener listener){
		alertDialog.findViewById(nguyenhoangviet.vpcorp.android.R.id.txt_id_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				listener.onClick(view);
				alertDialog.dismiss();
			}
		});
	}

	public int convertValue2Minute(){
		return Integer.parseInt(mLstValue.get(minutePicker.getValue()));
	}

	public int convertMinute2Value(Calendar calendar){
		int value = calendar.get(Calendar.MINUTE) / mTimeInterval;
		return value;
	}

	private NumberPicker getMinuteSpinner(TimePicker timPicker){
		try{

			NumberPicker numberPicker = null;

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
				Class<?> classForid = Class.forName("com.android.internal" + ".R$id");
				Field field = classForid.getField("minute");
				numberPicker = (NumberPicker)timePicker.findViewById(field.getInt(null));
			}else{
				Field f = timPicker.getClass().getDeclaredField("mMinuteSpinner"); // NoSuchFieldException
				f.setAccessible(true);
				numberPicker = (NumberPicker)f.get(timPicker);
			}

			return numberPicker; // IllegalAccessException
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	private NumberPicker getHourSpinner(TimePicker timPicker){
		try{
			Field f = timPicker.getClass().getDeclaredField("mHourSpinner");
			// NoSuchFieldException
			f.setAccessible(true);
			NumberPicker numberPicker = (NumberPicker)f.get(timPicker);
			return numberPicker; // IllegalAccessException
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

}
