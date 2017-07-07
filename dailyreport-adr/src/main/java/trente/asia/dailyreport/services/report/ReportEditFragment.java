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
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.model.ReportTemplate;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
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
	private ScrollView				scrollView;

	private EditText				edtCustomContent;

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
			Date reportDate = CCDateUtil.makeDateCustom(reportModel.reportDate, WelfareConst.WF_DATE_TIME);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, reportDate));
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestLoad(DRConst.API_REPORT_DETAIL, jsonObject, true);
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
		String reportTemplate = selectedReportTemplate == null || selectedReportTemplate.key.equals(ReportTemplate.TEMPLATE_NONE_KEY) ? null : selectedReportTemplate.key;

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("reportCustom", reportCustomContent);
			jsonObject.put("reportCustomTitle", reportModel.reportCustomTitle);
			jsonObject.put("key", reportModel.key);
			jsonObject.put("userId", reportUserKey);
			Date reportDate = CCDateUtil.makeDateCustom(reportModel.reportDate, WelfareConst.WF_DATE_TIME);
			jsonObject.put("reportDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, reportDate));
			jsonObject.put("reportContent", reportContent);
			jsonObject.put("reportUserKey", reportUserKey);
			jsonObject.put("reportStatus", reportStatus);
			jsonObject.put("reportTemplate", reportTemplate);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(DRConst.API_REPORT_UPDATE, jsonObject, true);
	}

	// }

	protected void successUpdate(JSONObject response, String url){
		Toast.makeText(activity, "Updated successfully", Toast.LENGTH_LONG).show();
		((WelfareActivity)activity).isInitData = true;
		onClickBackBtn();
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(DRConst.API_REPORT_DETAIL.equals(url)){
			ReportModel report = CCJsonUtil.convertToModel(response.optString("detail"), ReportModel.class);
			if(CCStringUtil.isEmpty(report.key)){
				report.reportDate = reportModel.reportDate;
				report.reportUser = prefAccUtil.getUserPref();
			}
			reportModel = report;
			reportTemplates = CCJsonUtil.convertToModelList(response.optString("templateList"), ReportTemplate.class);
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
		edtContent = null;
		edtCustomContent = null;
		txtDate = null;
		scrollView = null;
	}
}
