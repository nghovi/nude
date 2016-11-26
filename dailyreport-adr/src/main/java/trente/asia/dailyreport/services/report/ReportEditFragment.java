package trente.asia.dailyreport.services.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.dialogs.DRDialog;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.report.model.ActionEntry;
import trente.asia.dailyreport.services.report.model.GoalEntry;
import trente.asia.dailyreport.services.report.model.Kpi;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.model.ReportTemplate;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 2/15/2016.
 */
public class ReportEditFragment extends AbstractDRFragment implements WfSpinner.OnDRSpinnerItemSelectedListener{

	private static final int		ACTION_ENTRIES_SIZE_MAX	= 10;
	private List<ReportTemplate>	reportTemplates;
	private ReportTemplate			selectedReportTemplate;
	private ReportModel				reportModel;
	private EditText				edtContent;
	private TextView				txtDate;
	private LayoutInflater			inflater;
	private ScrollView				scrollView;
	private List<ActionEntry>		actionEntries			= new ArrayList<>();
	private List<Kpi>				kpis					= new ArrayList<>();

	private EditText				edtCustomContent;
	private ReportModel				reportModelRef;
	private Button					btnCopyActionEntries;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_report_edit;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void initData(){
		callReportDetailApi();
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_title_empty), null);

		btnSave = (Button)getView().findViewById(R.id.fragment_report_edit_btn_save);
		btnSend = (Button)getView().findViewById(R.id.fragment_report_edit_btn_send);
		spnTemplate = (WfSpinner)getView().findViewById(R.id.fragment_report_edit_spn);
		edtContent = (EditText)getView().findViewById(R.id.edt_id_content);
		edtCustomContent = (EditText)getView().findViewById(R.id.fragment_report_edit_edt_custom_content);
		txtDate = (TextView)getView().findViewById(R.id.fragment_report_edit_date);
		scrollView = (ScrollView)getView().findViewById(R.id.fragment_report_edit_scrollview);
		inflater = LayoutInflater.from(activity);
		// TextWatcher textWatcher = new TextWatcher() {
		//
		// @Override
		// public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
		//
		// }
		//
		// @Override
		// public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable editable){
		// if(CCStringUtil.isEmpty(edtContent.getText().toString()) || (!CCStringUtil.isEmpty(reportModel.reportCustomTitle) &&
		// CCStringUtil.isEmpty(edtCustomContent.getText().toString()))){
		// btnSave.setEnabled(false);
		// btnSend.setEnabled(false);
		// }else{
		// btnSave.setEnabled(true);
		// btnSend.setEnabled(true);
		// }
		// }
		// };

		// edtContent.addTextChangedListener(textWatcher);
		View.OnTouchListener onTouchListener = new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(v.getId() == R.id.edt_id_content || v.getId() == R.id.fragment_report_edit_edt_custom_content){
					scrollView.requestDisallowInterceptTouchEvent(true);
					switch(event.getAction() & MotionEvent.ACTION_MASK){
					case MotionEvent.ACTION_UP:
						scrollView.requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		};
		edtContent.setOnTouchListener(onTouchListener);
		// edtCustomContent.addTextChangedListener(textWatcher);
		edtCustomContent.setOnTouchListener(onTouchListener);
	}

	public void callReportDetailApi(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetReportId", reportModel.key);
			Date reportDate = CCDateUtil.makeDateCustom(reportModel.reportDate, WelfareConst.WL_DATE_TIME_1);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, reportDate));
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_REPORT_DETAIL, jsonObject, true);
	}

	private List<String> getSpinnerValuesTemplate(List<ReportTemplate> reportTemplates){
		List<String> values = new ArrayList<>();
		for(ReportTemplate reportTemplate : reportTemplates){
			values.add(reportTemplate.templateName);
		}
		return values;
	}

	private void sendReport(){
		updateReport(ReportModel.REPORT_STATUS_DONE);
	}

	private void saveReport(){
		updateReport(ReportModel.REPORT_STATUS_DRTT);
	}

	private void updateReport(String reportStatus){
		String reportCustomContent = edtCustomContent.getText().toString();
		String reportContent = edtContent.getText().toString();
		// boolean isCustomEmpty = (ReportModel.REPORT_STATUS_DONE.equals(reportStatus) && !CCStringUtil.isEmpty(reportModel.reportCustomTitle) &&
		// CCStringUtil.isEmpty(reportCustomContent));
		// boolean isContentEmpty = CCStringUtil.isEmpty(reportContent);
		// if(isContentEmpty || isCustomEmpty){
		// if(isContentEmpty){
		// edtContent.setText("");
		// alertDialog.setMessage(getString(R.string.msg_msg_report_content_null));
		// }else if(isCustomEmpty){
		// alertDialog.setMessage(getString(R.string.msg_msg_report_custome_null, reportModel.reportCustomTitle));
		// edtCustomContent.setText("");
		// }
		// alertDialog.show();
		// }else{
		String reportUserKey = reportModel.reportUser == null ? prefAccUtil.getUserPref().key : reportModel.reportUser.key;
		String actionEntriesListString = getActionEntriesListString();
		String goalEntriesString = getGoalEntriesListString();
		String reportTemplate = selectedReportTemplate == null || selectedReportTemplate.key.equals(ReportTemplate.TEMPLATE_NONE_KEY) ? null : selectedReportTemplate.key;

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("reportCustom", reportCustomContent);
			jsonObject.put("reportCustomTitle", reportModel.reportCustomTitle);
			jsonObject.put("key", reportModel.key);
			jsonObject.put("userId", reportUserKey);
			Date reportDate = CCDateUtil.makeDateCustom(reportModel.reportDate, WelfareConst.WL_DATE_TIME_1);
			jsonObject.put("reportDate", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, reportDate));
			jsonObject.put("reportContent", reportContent);
			jsonObject.put("reportUserKey", reportUserKey);
			jsonObject.put("reportStatus", reportStatus);
			jsonObject.put("reportTemplate", reportTemplate);
			jsonObject.put("actionEntriesString", actionEntriesListString);
			jsonObject.put("goalEntiriesString", goalEntriesString);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_REPORT_UPDATE, jsonObject, true);
	}

	// }

	protected void successUpdate(JSONObject response, String url){
		Toast.makeText(activity, "Updated successfully", Toast.LENGTH_LONG).show();
		((WelfareActivity)activity).isInitData = true;
		onClickBackBtn();
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_REPORT_DETAIL.equals(url)){
			ReportModel report = CCJsonUtil.convertToModel(response.optString("detail"), ReportModel.class);
			if(CCStringUtil.isEmpty(report.key)){
				report.reportDate = reportModel.reportDate;
				report.reportUser = prefAccUtil.getUserPref();
			}
			reportModel = report;

			reportModelRef = CCJsonUtil.convertToModel(response.optString("detailRef"), ReportModel.class);

			reportTemplates = CCJsonUtil.convertToModelList(response.optString("templateList"), ReportTemplate.class);
			kpis = CCJsonUtil.convertToModelList(response.optString("kpis"), Kpi.class);

			filterTemplateByStatus();
			buildReportDetail();
		}
	}

	private void filterTemplateByStatus(){
		Iterator<ReportTemplate> reportTemplateIterator = reportTemplates.iterator();
		while(reportTemplateIterator.hasNext()){
			ReportTemplate reportTemplate = reportTemplateIterator.next();
			if(reportTemplate.templateStatus == false){
				reportTemplateIterator.remove();
			}
		}
	}

	private void buildReportDetail(){
		updateHeader(reportModel.reportUser.userName);
		buildTemplateSpinner();
		txtDate.setText(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD));
		if(DRUtil.isNotEmpty(reportModel.reportContent)){
			edtContent.setText(reportModel.reportContent);
		}else if(selectedReportTemplate != null && DRUtil.isNotEmpty(selectedReportTemplate.templateContent)){
			edtContent.setText(selectedReportTemplate.templateContent);
		}else{
			edtContent.setText("");// to trigger btn status
		}

		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				saveReport();
			}
		});

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendReport();
			}
		});

		buildReportCustom();

		buildKPI();
	}

	private void buildReportCustom(){
		LinearLayout lnrReportCustom = (LinearLayout)getView().findViewById(R.id.fragment_report_edit_lnr_custom);
		if(!CCStringUtil.isEmpty(reportModel.reportCustomTitle)){
			lnrReportCustom.setVisibility(View.VISIBLE);
			TextView txtCustomTitle = (TextView)getView().findViewById(R.id.fragment_report_edit_txt_custom_title);
			txtCustomTitle.setText(reportModel.reportCustomTitle);
			edtCustomContent.setText(reportModel.reportCustom);
		}else{
			lnrReportCustom.setVisibility(View.GONE);
		}
	}

	private void buildTemplateSpinner(){
		selectedReportTemplate = getSelectedReportTemplate();
		spnTemplate.setupLayout(getString(R.string.spinner_name_template), getSpinnerValuesTemplate(reportTemplates), getSelectedTemplatePosition(), this, true);
	}

	private int getSelectedTemplatePosition(){
		if(reportTemplates == null || reportTemplates.size() == 0 || selectedReportTemplate == null){
			return 0;
		}
		for(int i = 0; i < reportTemplates.size(); i++){
			if(reportTemplates.get(i).key.equals(selectedReportTemplate.key)){
				return i;
			}
		}
		return 0;
	}

	private void buildKPI(){
		buildMonthlyGoals();
		buildActionPlans();
	}

	private void buildMonthlyGoals(){
		if(reportModel.goalEntries.size() > 0){
			LinearLayout lnrGoalEntriesContainer = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_monthly);
			buildGoalEntries(lnrGoalEntriesContainer);
		}else{
			LinearLayout lnrMonthlyGoal = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_monthly_goal);
			lnrMonthlyGoal.setVisibility(View.GONE);
		}
	}

	private void buildActionPlans(){
		if(kpis.size() > 0){
			final LinearLayout lnrActionEntriesHeader = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_actual_plan_header);
			final LinearLayout actionEntriesContainer = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_actual_plan);
			Button btnAddActualPlan = (Button)getView().findViewById(R.id.view_kpi_btn_add);
			btnCopyActionEntries = (Button)getView().findViewById(R.id.view_kpi_btn_copy);

			if(reportModel.actionEntries.size() > 0){
				buildOldActionEntries(actionEntriesContainer, lnrActionEntriesHeader);
			}else{
				// Now always show header
				// lnrActionEntriesHeader.setVisibility(View.GONE);
			}

			btnAddActualPlan.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					if(CCCollectionUtil.isEmpty(kpis)){
						alertDialog.setMessage(getString(R.string.dr_report_kpi_item_required));
						alertDialog.show();
					}else{
						if(actionEntries.size() >= ACTION_ENTRIES_SIZE_MAX){
							showDialogMaxEntriesWarning();
						}else{
							ActionEntry actionEntry = new ActionEntry();
							actionEntry.tmpIdx = actionEntries.size();
							showAddActionEntryDialog(actionEntry, actionEntriesContainer, lnrActionEntriesHeader, true);
						}
					}
				}
			});

			btnCopyActionEntries.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					copyActionPlans(actionEntriesContainer, lnrActionEntriesHeader);
				}
			});

			updateBtnCopyVisibility();
		}else{
			LinearLayout lnrActionPlan = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_action_plan);
			lnrActionPlan.setVisibility(View.GONE);
		}
	}

	private void updateBtnCopyVisibility(){
		if(reportModelRef != null && reportModelRef.actionEntries.size() > 0 && actionEntries.size() < ACTION_ENTRIES_SIZE_MAX){
			btnCopyActionEntries.setVisibility(View.VISIBLE);
		}else{
			btnCopyActionEntries.setVisibility(View.GONE);
		}
	}

	private void copyActionPlans(LinearLayout actionEntriesContainer, LinearLayout lnrActionEntriesHeader){
		for(ActionEntry actionEntry : reportModelRef.actionEntries){
			if(actionEntries.size() >= ACTION_ENTRIES_SIZE_MAX){
				return;
			}
			ActionEntry clonedActionEntry = ActionEntry.copyFrom(actionEntry);
			addActionPlan(clonedActionEntry, actionEntriesContainer, lnrActionEntriesHeader);
		}
	}

	private void buildGoalEntries(LinearLayout container){
		container.removeAllViews();
		for(int i = 0; i < reportModel.goalEntries.size(); i++){
			addGoalEntry(reportModel.goalEntries.get(i), container, i);
		}
	}

	private void buildOldActionEntries(LinearLayout container, LinearLayout header){
		container.removeAllViews();
		for(ActionEntry actionEntry : reportModel.actionEntries){
			addActionPlan(actionEntry, container, header);
		}
	}

	private void showAddActionEntryDialog(ActionEntry actionEntry, final LinearLayout lnrContainer, final LinearLayout header, final boolean isNew){
		DRDialog.OnActionPlanCreationListener listener = new DRDialog.OnActionPlanCreationListener() {

			@Override
			public void onDone(ActionEntry actualPlan){
				activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				if(isNew){
					addActionPlan(actualPlan, lnrContainer, header);
				}else{
					updateActualKpi(lnrContainer, actualPlan);
				}
				scrollView.post(new Runnable() {

					@Override
					public void run(){
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			}

			@Override
			public void onDelete(ActionEntry actionEntry){
				deleteActualPlan(lnrContainer, actionEntry, header);
			}
		};
		DRDialog dlgAddActualPlan = new DRDialog(activity);
		dlgAddActualPlan.setDialogAddActionEntry(kpis, actionEntry, listener, isNew).show();
	}

	private void updateActualKpi(final LinearLayout lnrContainer, ActionEntry actionEntry){
		View itemView = lnrContainer.getChildAt(getActionEntryIdx(actionEntry));
		buildActionPlanItemLayout(itemView, actionEntry);
	}

	private int getActionEntryIdx(ActionEntry actionEntry){
		for(int i = 0; i < actionEntries.size(); i++){
			if(actionEntries.get(i).tmpIdx == actionEntry.tmpIdx){
				return i;
			}
		}
		return -1;
	}

	public static void buildActionPlanItemLayout(View itemView, final ActionEntry actionEntry){
		TextView txtItem = (TextView)itemView.findViewById(R.id.item_kpi_name);
		// TextView txtAvg = (TextView)itemView.findViewById(R.id.item_kpi_avg);
		TextView txtPlan = (TextView)itemView.findViewById(R.id.item_kpi_edt_plan);
		TextView txtActual = (TextView)itemView.findViewById(R.id.item_kpi_edt_actual);

		txtItem.setText(actionEntry.actionName);
		if(actionEntry.actionActual.contains(".") || actionEntry.actionPlan.contains(".")){// ITEM UNIT TIME
			txtPlan.setText(actionEntry.actionPlan);
			txtActual.setText(actionEntry.actionActual);
		}else{
			txtPlan.setText(WelfareUtil.formatAmount(actionEntry.actionPlan));
			txtActual.setText(WelfareUtil.formatAmount(actionEntry.actionActual));
		}
	}

	private void deleteActualPlan(LinearLayout container, ActionEntry actionEntry, LinearLayout header){
		int actionEntryIdx = getActionEntryIdx(actionEntry);
		if(actionEntries.size() > 0){
			actionEntries.remove(actionEntryIdx);
			container.removeViewAt(actionEntryIdx);
		}

		if(actionEntries.size() <= 0){
			header.setVisibility(View.GONE);
		}
		updateBtnCopyVisibility();
	}

	private void addGoalEntry(final GoalEntry goalEntry, final LinearLayout lnrContainer, final int index){
		View itemView = inflater.inflate(R.layout.item_goal_entry, null);

		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				showGoalEntriesDialog(goalEntry, lnrContainer, index);
			}
		});
		buildGoalEntryItemLayout(itemView, goalEntry);
		lnrContainer.addView(itemView);
	}

	private void buildGoalEntryItemLayout(View itemView, GoalEntry goalEntry){
		TextView txtItem = (TextView)itemView.findViewById(R.id.item_kpi_name);
		TextView txtGoal = (TextView)itemView.findViewById(R.id.item_kpi_goal);
		TextView txtSum = (TextView)itemView.findViewById(R.id.item_kpi_sum);
		TextView txtPlan = (TextView)itemView.findViewById(R.id.item_kpi_month_txt_plan);
		TextView txtActual = (TextView)itemView.findViewById(R.id.item_kpi_month_txt_actual);

		txtItem.setText(goalEntry.goalName);

		if(Kpi.KPI_UNIT_TIME.equals(goalEntry.goalUnit)){// ITEM UNIT TIME
			txtGoal.setText(goalEntry.goalValue);
			txtSum.setText(goalEntry.actualSum);
			txtPlan.setText(goalEntry.goalPlan);
			txtActual.setText(goalEntry.goalActual);
		}else{
			txtGoal.setText(WelfareUtil.formatAmount(goalEntry.goalValue));
			txtSum.setText(WelfareUtil.formatAmount(goalEntry.actualSum));
			txtPlan.setText(WelfareUtil.formatAmount(goalEntry.goalPlan));
			txtActual.setText(WelfareUtil.formatAmount(goalEntry.goalActual));
		}

	}

	private void showGoalEntriesDialog(final GoalEntry goalEntry, final LinearLayout lnrConainer, final int index){
		DRDialog.OnGoalEntryUpdateListener listener = new DRDialog.OnGoalEntryUpdateListener() {

			@Override
			public void onDone(GoalEntry goalEntry){
				updateGoalEntriesLayout(lnrConainer, goalEntry, index);
			}

			@Override
			public void onClear(GoalEntry kpi){
				updateGoalEntriesLayout(lnrConainer, goalEntry, index);
			}
		};
		final DRDialog dlgMonthlyKpiUpdate = new DRDialog(activity);
		dlgMonthlyKpiUpdate.setDialogGoalEntries(goalEntry, listener).show();
	}

	private void updateGoalEntriesLayout(LinearLayout lnrContainer, GoalEntry goalEntry, int index){
		View itemView = lnrContainer.getChildAt(index);
		buildGoalEntryItemLayout(itemView, goalEntry);
	}

	private void addActionPlan(final ActionEntry actionEntry, final LinearLayout lnrConainer, final LinearLayout header){
		View itemView = inflater.inflate(R.layout.item_action_entry, null);
		actionEntries.add(actionEntry);
		actionEntry.tmpIdx = actionEntries.size();
		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				showAddActionEntryDialog(actionEntry, lnrConainer, header, false);
			}
		});
		buildActionPlanItemLayout(itemView, actionEntry);
		lnrConainer.addView(itemView);
		header.setVisibility(View.VISIBLE);
		updateBtnCopyVisibility();
	}

	private void showDialogMaxEntriesWarning(){
		DRDialog dlgMaxEntries = new DRDialog(activity);
		dlgMaxEntries.setDialogConfirm(getString(R.string.dlr_max_entries), getString(android.R.string.ok), null, null, null).show();
	}

	public String getActionEntriesListString(){
		Gson gson = new Gson();
		for(ActionEntry actionEntry : actionEntries){
			actionEntry.kpiItem = actionEntry.itemId;
		}
		return gson.toJson(actionEntries);
	}

	public String getGoalEntriesListString(){
		Gson gson = new Gson();
		return gson.toJson(reportModel.goalEntries);
	}

	private ReportTemplate getSelectedReportTemplate(){
		if(reportTemplates == null){
			reportTemplates = new ArrayList<>();
		}

		reportTemplates.add(0, ReportTemplate.getTemplateNone(getString(R.string.str_none)));

		if(reportModel.reportTemplate == null){
			return reportTemplates.get(0);
		}

		for(ReportTemplate reportTemplate : reportTemplates){
			if(reportTemplate.key.equals(reportModel.reportTemplate)){
				return reportTemplate;
			}
		}
		return reportTemplates.get(0);
	}

	private WfSpinner		spnTemplate;
	private LinearLayout	lnrContents;
	private Button			btnSave;
	private Button			btnSend;

	public void setReportModel(ReportModel reportModel){
		this.reportModel = reportModel;
	}

	@Override
	public void onItemSelected(int selectedPosition){
		ReportTemplate newTemplate = reportTemplates.get(selectedPosition);
		if(!newTemplate.key.equals(selectedReportTemplate.key)){
			showTemplageChangeConfirmDialog(newTemplate);
		}
	}

	private void showTemplageChangeConfirmDialog(final ReportTemplate newTemplate){
		final DRDialog dlgTemplateChangeConfirm = new DRDialog(activity);
		dlgTemplateChangeConfirm.setCanceledOnTouchOutside(false);
		dlgTemplateChangeConfirm.setDialogConfirm(getString(R.string.fragment_report_edit_change_template_confirm), getString(android.R.string.ok), getString(android.R.string.cancel), new View.OnClickListener() {

			@Override
			public void onClick(View v){
				updateTemplate(newTemplate);
			}
		}, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				int oldSelectedPosition = getSelectedTemplatePosition();
				spnTemplate.setSelectedPosition(oldSelectedPosition);
			}
		}).show();
	}

	private void updateTemplate(ReportTemplate newTemplate){
		selectedReportTemplate = newTemplate;
		if(selectedReportTemplate.key.equals(reportModel.reportTemplate)){
			edtContent.setText(reportModel.reportContent);
		}else if(selectedReportTemplate != null){
			edtContent.setText(selectedReportTemplate.templateContent);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		btnCopyActionEntries = null;
		edtContent = null;
		edtCustomContent = null;
		txtDate = null;
		scrollView = null;
	}
}
