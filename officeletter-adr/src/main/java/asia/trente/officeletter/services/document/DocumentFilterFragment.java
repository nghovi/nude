package asia.trente.officeletter.services.document;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import asia.trente.officeletter.databinding.FragmentDocumentFilterBinding;
import asia.trente.officeletter.services.document.view.MonthYearPickerDialog;

/**
 * Created by tien on 8/23/2017.
 */

public class DocumentFilterFragment extends AbstractOLFragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener{

	private FragmentDocumentFilterBinding	binding;
	private OnDocumentFilterListener		callback;
	private String							startMonth;
	private String							endMonth;
	private boolean							isStartMonth			= true;
	private MonthYearPickerDialog			monthYearPickerDialog	= new MonthYearPickerDialog();

	public void setFilterCondition(String startMonth, String endMonth){
		this.startMonth = startMonth;
		this.endMonth = endMonth;
	}

	public void setCallback(OnDocumentFilterListener callback){
		this.callback = callback;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_document_filter, container, false);
			binding = DataBindingUtil.bind(mRootView);
		}
		return mRootView;
	}

	@Override
	protected int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.ol_document_filter), R.drawable.cs_row_check);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		binding.btnClear.setOnClickListener(this);
		binding.lnrStartMonth.setOnClickListener(this);
		binding.lnrEndMonth.setOnClickListener(this);
		binding.startMonth.setText(startMonth);
		binding.endMonth.setText(endMonth);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		callback = null;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			getFragmentManager().popBackStack();
			if(callback != null){
				callback.onDocumentFilterDone(startMonth, endMonth);
			}
			break;
		case R.id.btn_clear:
			binding.startMonth.setText(R.string.chiase_common_none);
			binding.endMonth.setText(R.string.chiase_common_none);
			break;
		case R.id.lnr_start_month:
			monthYearPickerDialog.setmSelectedTime(getYear(startMonth), getMonth(startMonth));
			monthYearPickerDialog.setMaxMonth(getMonth(endMonth));
			monthYearPickerDialog.setMaxYear(getYear(endMonth));
			monthYearPickerDialog.setListener(this);
			monthYearPickerDialog.show(getFragmentManager(), null);
			isStartMonth = true;
			break;
		case R.id.lnr_end_month:
			monthYearPickerDialog.setmSelectedTime(getYear(endMonth), getMonth(endMonth));
			monthYearPickerDialog.setMaxMonth(12);
			monthYearPickerDialog.setMaxYear(getYear(null));
			monthYearPickerDialog.setListener(this);
			monthYearPickerDialog.show(getFragmentManager(), null);
			isStartMonth = false;
			break;
		}
	}

	private int getYear(String time){
		if(time == null){
			return Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR);
		}
		return Integer.parseInt(time.substring(0, 4));
	}

	private int getMonth(String time){
		if(time == null){
			return Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH) + 1;
		}
		return Integer.parseInt(time.substring(5, 7));
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
		String monthText = year + month < 10 ? "0" + month : "" + month;
		if(isStartMonth){
			binding.startMonth.setText(monthText);
			startMonth = monthText;
		}else{
			binding.endMonth.setText(year + "." + monthText);
			endMonth = monthText;
		}
	}

	public interface OnDocumentFilterListener{

		void onDocumentFilterDone(String startMonth, String endMonth);
	}

}
