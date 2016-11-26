package trente.asia.dailyreport.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.report.model.ActionEntry;
import trente.asia.dailyreport.services.report.model.GoalEntry;
import trente.asia.dailyreport.services.report.model.Kpi;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.DRLikeListAdapter;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * QkChiaseDialog
 *
 * @author TrungND
 */
public class DRDialog extends ChiaseDialog{

	private static final int	MAX_CHARACTER_COUNT	= 9999;
	private static final int	MAX_CHARACTER_SALE	= 9999999;

	private Context				mContext;

	public DRDialog(Context context){
		super(context);
		this.mContext = context;
	}

	public DRDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public interface OnSendCommentListener{

		public void onClickSend(String comment);
	}

	public interface OnActionPlanCreationListener{

		public void onDone(ActionEntry actionEntry);

		public void onDelete(ActionEntry actionEntry);
	}

	public static TextWatcher getAmountTextWatcher(final EditText editText){
		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				editText.removeTextChangedListener(this);
				String reFormattedAmount = WelfareUtil.formatAmount(WelfareUtil.getAmount(editable.toString()));
				editText.setText(reFormattedAmount);
				editText.setSelection(reFormattedAmount.length());
				editText.addTextChangedListener(this);
			}
		};
		return textWatcher;
	}

	public class InputFilterMinMax implements InputFilter{

		private int min, max;

		public InputFilterMinMax(int min, int max){
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max){
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
			try{
				int input = Integer.parseInt(WelfareUtil.getAmount(dest.toString() + source.toString()));
				if(isInRange(min, max, input)) return null;
			}catch(NumberFormatException nfe){
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c){
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	// http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
	public class DecimalDigitsInputFilter implements InputFilter{

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero){
			mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero) + "}+(" + "(\\.[0-9]{0," + (digitsAfterZero) + "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
			Matcher matcher = mPattern.matcher((dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length())));
			if(!matcher.matches()) return "";
			return null;
		}

	}

	public DRDialog setDialogAddActionEntry(final List<Kpi> kpis, final ActionEntry actionEntry, final OnActionPlanCreationListener listener, final boolean isNewEntry){
		this.setContentView(R.layout.dialog_add_action_plan);

		// Get view
		ImageView imgClose = (ImageView)this.findViewById(R.id.dlg_add_actual_plan_close);
		final TextView txtUnit = (TextView)this.findViewById(R.id.dlg_add_actual_plan_unit);
		final EditText edtName = (EditText)this.findViewById(R.id.dlg_add_actual_plan_edt_name);
		final EditText edtPlan = (EditText)this.findViewById(R.id.dlg_add_actual_plan_edt_plan);
		final EditText edtActual = (EditText)this.findViewById(R.id.dlg_add_actual_plan_edt_actual);
		Button btnDone = (Button)this.findViewById(R.id.dlg_add_actual_plan_btn_done);
		Button btnDelete = (Button)this.findViewById(R.id.dlg_add_actual_plan_btn_clear);
		WfSpinner spnGoalEntry = (WfSpinner)this.findViewById(R.id.dlg_add_actual_plan_spn);

		edtName.setText(actionEntry.actionName);
		final TextWatcher actualTextWatcher = getAmountTextWatcher(edtActual);
		final TextWatcher planTextWatcher = getAmountTextWatcher(edtPlan);

		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DRDialog.this.dismiss();
				actionEntry.actionActual = WelfareUtil.getAmount(edtActual.getText().toString());
				actionEntry.actionPlan = WelfareUtil.getAmount(edtPlan.getText().toString());
				actionEntry.actionName = edtName.getText().toString();
				listener.onDone(actionEntry);
			}
		});

		if(isNewEntry){
			btnDelete.setVisibility(View.GONE);
		}else{
			btnDelete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					DRDialog.this.dismiss();
					if(!isNewEntry){
						listener.onDelete(actionEntry);
					}
				}
			});
		}

		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DRDialog.this.dismiss();
			}
		});

		// buildSpinner, unit
		int selectedPosition = getSelectedKpiPosition(kpis, actionEntry);
		spnGoalEntry.setupLayout("KPI", getSpinnerValuesKpi(kpis), selectedPosition, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				Kpi kpi = kpis.get(selectedPosition);
				txtUnit.setText(kpi.itemUnit);
				edtActual.removeTextChangedListener(actualTextWatcher);
				edtPlan.removeTextChangedListener(planTextWatcher);
				if(kpi.itemUnit.equals(Kpi.KPI_UNIT_TIME)){
					edtActual.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
					edtPlan.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
					edtActual.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					edtPlan.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
					edtActual.setText(actionEntry.actionActual);
					edtPlan.setText(actionEntry.actionPlan);
				}else{
					edtActual.addTextChangedListener(actualTextWatcher);
					edtPlan.addTextChangedListener(planTextWatcher);
					int maxChararter = MAX_CHARACTER_COUNT;
					if(kpi.itemUnit.equals(Kpi.KPI_UNIT_SALE)){
						maxChararter = MAX_CHARACTER_SALE;
					}
					edtActual.setFilters(new InputFilter[]{new InputFilterMinMax(0, maxChararter)});
					edtPlan.setFilters(new InputFilter[]{new InputFilterMinMax(0, maxChararter)});
					edtActual.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
					edtPlan.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
					edtActual.setText(WelfareUtil.formatAmount(actionEntry.actionActual));
					edtPlan.setText(WelfareUtil.formatAmount(actionEntry.actionPlan));
				}
				if(!kpi.key.equals(actionEntry.itemId)){
					edtActual.setText("");
					edtPlan.setText("");
					edtName.setText("");
				}
				actionEntry.itemId = kpi.key;
			}
		}, true);
		return this;
	}

	private int getSelectedKpiPosition(List<Kpi> kpis, ActionEntry actionEntry){
		if(kpis == null){
			kpis = new ArrayList<>();
		}

		for(int i = 0; i < kpis.size(); i++){
			if(actionEntry != null && kpis.get(i).key.equals(actionEntry.itemId)){
				return i;
			}
		}
		return 0;
	}

	public static List<String> getSpinnerValuesKpi(List<Kpi> kpis){
		List<String> kpiNames = new ArrayList<>();
		for(Kpi kpi : kpis){
			kpiNames.add(kpi.itemName);
		}
		return kpiNames;
	}

	public interface OnGoalEntryUpdateListener{

		public void onDone(GoalEntry goalEntry);

		public void onClear(GoalEntry goalEntry);
	}

	public DRDialog setDialogGoalEntries(final GoalEntry goalEntry, final OnGoalEntryUpdateListener listener){
		this.setContentView(R.layout.dialog_monthly_kpi_update);

		// Get view
		ImageView imgClose = (ImageView)this.findViewById(R.id.dlg_add_actual_plan_close);
		final TextView txtName = (TextView)this.findViewById(R.id.dlg_monthly_kpi_update_name);
		final TextView txtUnit = (TextView)this.findViewById(R.id.dlg_monthly_kpi_update_unit);

		final TextView txtMonthlyGoal = (TextView)this.findViewById(R.id.dlg_monthly_kpi_update_monthly_goal);
		final TextView txtRule = (TextView)this.findViewById(R.id.dlg_monthly_kpi_update_input_rule);

		final EditText edtPlan = (EditText)this.findViewById(R.id.dlg_add_actual_plan_edt_plan);
		final EditText edtActual = (EditText)this.findViewById(R.id.dlg_add_actual_plan_edt_actual);
		Button btnDone = (Button)this.findViewById(R.id.dlg_add_actual_plan_btn_done);
		Button btnClear = (Button)this.findViewById(R.id.dlg_add_actual_plan_btn_clear);
		final TextWatcher actualTextWatcher = getAmountTextWatcher(edtActual);
		final TextWatcher planTextWatcher = getAmountTextWatcher(edtPlan);

		if(goalEntry.goalUnit.equals(Kpi.KPI_UNIT_TIME)){
			edtActual.removeTextChangedListener(actualTextWatcher);
			edtPlan.removeTextChangedListener(planTextWatcher);
			edtActual.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
			edtPlan.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
			edtActual.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			edtPlan.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}else{
			edtActual.addTextChangedListener(actualTextWatcher);
			edtPlan.addTextChangedListener(planTextWatcher);
			int maxChararter = MAX_CHARACTER_COUNT;
			if(goalEntry.goalUnit.equals(Kpi.KPI_UNIT_SALE)){
				maxChararter = MAX_CHARACTER_SALE;
			}
			edtActual.setFilters(new InputFilter[]{new InputFilterMinMax(0, maxChararter)});
			edtPlan.setFilters(new InputFilter[]{new InputFilterMinMax(0, maxChararter)});
			edtActual.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
			edtPlan.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
		}

		txtName.setText(goalEntry.goalName);
		txtUnit.setText(goalEntry.goalUnit);
		txtMonthlyGoal.setText(WelfareUtil.formatAmount(goalEntry.goalValue));
		txtRule.setText(goalEntry.goalDescription);
		edtActual.setText(WelfareUtil.formatAmount(goalEntry.goalActual));
		edtPlan.setText(WelfareUtil.formatAmount(goalEntry.goalPlan));

		txtRule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				txtRule.setSingleLine(false);
				txtRule.setMaxLines(6);
				txtRule.setVerticalScrollBarEnabled(true);
			}
		});

		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DRDialog.this.dismiss();
				goalEntry.goalActual = WelfareUtil.getAmount(edtActual.getText().toString());
				goalEntry.goalPlan = WelfareUtil.getAmount(edtPlan.getText().toString());
				listener.onDone(goalEntry);
			}
		});

		btnClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				edtActual.setText("");
				edtPlan.setText("");
			}
		});

		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DRDialog.this.dismiss();
			}
		});
		return this;
	}

	public DRDialog setDialogConfirm(String strMessage, String strPositive, String strNegative, final View.OnClickListener listenerPositive, final View.OnClickListener listenerNegative){
		this.setContentView(R.layout.dialog_confirm);
		if(!CCStringUtil.isEmpty(strMessage)){
			TextView txtMessage = (TextView)this.findViewById(R.id.txt_id_msg);
			txtMessage.setText(strMessage);
		}

		TextView txtPositive = (TextView)this.findViewById(R.id.txt_id_positive);
		if(!CCStringUtil.isEmpty(strPositive)){
			txtPositive.setText(strPositive);
		}
		txtPositive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
				if(listenerPositive != null){
					listenerPositive.onClick(view);
				}
			}
		});

		TextView txtNegative = (TextView)this.findViewById(R.id.txt_id_negative);
		txtNegative.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				dismiss();
				listenerNegative.onClick(v);
			}
		});
		if(!CCStringUtil.isEmpty(strNegative)){
			txtNegative.setText(strNegative);
		}else{
			txtNegative.setVisibility(View.GONE);
		}

		return this;
	}

	public DRDialog setLikersDialog(Activity activity, ReportModel reportModel, String title, boolean isLikers){
		this.setContentView(R.layout.dialog_likers);
		TextView txtTitle = (TextView)this.findViewById(R.id.dialog_likers_title);
		txtTitle.setText(title);
		ImageView imgClose = (ImageView)this.findViewById(R.id.dialog_liker_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
			}
		});
		ListView lstFollowers = (ListView)this.findViewById(R.id.fragment_followers_list);
		DRLikeListAdapter drLikeListAdapter = new DRLikeListAdapter(activity, R.layout.item_user, reportModel, isLikers);
		lstFollowers.setAdapter(drLikeListAdapter);
		return this;
	}

	@Override
	public void show(){
		super.show();
	}

	@Override
	public void dismiss(){
		super.dismiss();
	}
}
