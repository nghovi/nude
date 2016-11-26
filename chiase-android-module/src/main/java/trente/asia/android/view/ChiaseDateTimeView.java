package trente.asia.android.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import trente.asia.android.R;

/**
 * Created by TrungND on 16/01/2015.
 */
public class ChiaseDateTimeView{

	private final String	COMMON_DATE_TIME_FORMAT_24H	= "yyyy/MM/dd HH:mm";

	protected ChiaseDialog	alertDialog;

	protected Calendar		dateTime;

	protected DatePicker	datePicker;
	protected TimePicker	timePicker;

	public ChiaseDateTimeView(){

	}

	public ChiaseDateTimeView(Context context){
		alertDialog = new ChiaseDialog(context);
		alertDialog.setContentView(R.layout.chiase_date_time_view);

		alertDialog.findViewById(R.id.txt_id_set).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				setCommonDateTime();
			}
		});

		alertDialog.findViewById(R.id.txt_id_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				alertDialog.dismiss();
			}
		});

		datePicker = (DatePicker)alertDialog.findViewById(R.id.date_picker);
		try{
			datePicker.getCalendarView().setShowWeekNumber(false);
		}catch(UnsupportedOperationException e){
			e.printStackTrace();
		}

		timePicker = (TimePicker)alertDialog.findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true);
	}

	public void show(){
		alertDialog.show();
		Calendar calendar = Calendar.getInstance();
		if(dateTime != null){
			calendar = dateTime;
			datePicker.init(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), null);
		}

		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	public Calendar getDateTime(){
		this.dateTime.set(Calendar.SECOND, 0);
		this.dateTime.set(Calendar.MILLISECOND, 0);
		return dateTime;
	}

	public void setDateTime(Calendar dateTime){
		this.dateTime = dateTime;
		this.dateTime.set(Calendar.SECOND, 0);
		this.dateTime.set(Calendar.MILLISECOND, 0);
	}

	public String getDateTimeString(){
		if(dateTime == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(COMMON_DATE_TIME_FORMAT_24H);
		String dtString = format.format(dateTime.getTime());
		return dtString;
	}

	public void setCommonDateTime(){
		dateTime = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		alertDialog.dismiss();
	}

	public void setDateTimeListener(View.OnClickListener listener){
		alertDialog.findViewById(R.id.txt_id_set).setOnClickListener(listener);
	}

	public void setMaxDate(long maxDate){
		datePicker.setMaxDate(maxDate);
	}

	public void setMinDate(long minDate){
		datePicker.setMinDate(minDate);
	}

	// public void showWithCurrentHour(){
	// alertDialog.show();
	// Calendar calendar = Calendar.getInstance();
	// if(dateTime != null){
	// calendar = dateTime;
	// datePicker.init(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), null);
	// }
	//
	// timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
	// }

	public void refreshTimePicker(){
		if(dateTime != null){
			datePicker.init(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), null);

			timePicker.setCurrentHour(dateTime.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(dateTime.get(Calendar.MINUTE));
		}
	}
}
