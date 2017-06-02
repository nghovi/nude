package trente.asia.welfare.adr.view;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.welfare.adr.R;

/**
 * Created by viet on 7/13/2016.
 */
public class WfSpinner extends LinearLayout implements AdapterView.OnItemSelectedListener{

	public WfSpinner(Context context){
		super(context);
	}

	public WfSpinner(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public WfSpinner(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public WfSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
		listener.onItemSelected(i);
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView){

	}

	public interface OnDRSpinnerItemSelectedListener{

		void onItemSelected(int selectedPosition);
	}

	private TextView						txtName;
	private Spinner							spinner;
	private OnDRSpinnerItemSelectedListener	listener;
	private ArrayAdapter					adapter;

	public void setupLayout(String spinnerName, List<String> values, int selectedIndex, OnDRSpinnerItemSelectedListener onDRSpinnerItemSelectedListener, boolean isTriggerEvent){
		txtName = (TextView)this.getChildAt(0);
		if (CCStringUtil.isEmpty(spinnerName)) {
			txtName.setVisibility(View.GONE);
		} else {
			txtName.setText(spinnerName);
		}
		this.spinner = (Spinner)this.getChildAt(1);
		this.listener = onDRSpinnerItemSelectedListener;

		// int defaultPositionItem = values.indexOf(selectedValue);
		buildSpinner(values, selectedIndex, isTriggerEvent);
	}

	private void buildSpinner(List<String> values, int defaultPosition, boolean triggerEvent){

		adapter = new ArrayAdapter<String>(getContext(), R.layout.item_wf_spinner, values);
		adapter.setDropDownViewResource(R.layout.item_wf_spinner_dropdown);
		spinner.setAdapter(adapter);
		spinner.setSelection(defaultPosition);
		if(triggerEvent){
			spinner.setOnItemSelectedListener(WfSpinner.this);
		}else{
			spinner.post(new Runnable() {

				public void run(){
					spinner.setOnItemSelectedListener(WfSpinner.this);
				}
			});
		}
	}

	public void setSelectedPosition(int position){
		spinner.setSelection(position);
	}

	// public int getPositionOf(String value){
	// return adapter.getPosition(value);
	// }

}
