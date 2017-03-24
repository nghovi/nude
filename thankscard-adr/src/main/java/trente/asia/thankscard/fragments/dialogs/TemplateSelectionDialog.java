package trente.asia.thankscard.fragments.dialogs;

import java.util.Calendar;
import java.util.List;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.thankscard.R;
import trente.asia.thankscard.services.common.TCDetailFragment;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 3/16/2016.
 */
public class TemplateSelectionDialog extends TCDialog{

	List<Template>			templates;
	Template				selectedTemplate;
	public static int		MARGIN_BOTTOM_DP	= 20;
	private List<DeptModel>	depts;

	public void setTemplates(List<Template> templates){
		this.templates = templates;
	}

	public void setSelectedTemplate(Template template){
		this.selectedTemplate = template;
	}

	public Template getSelectedTemplate(){
		return this.selectedTemplate;
	}

	@Override
	int getDialogLayoutId(){
		return R.layout.dialog_template_selection;
	}

	@Override
	void buildDialogLayout(View rootView){
		Button btnCancel = (Button)rootView.findViewById(R.id.btn_dialog_template_selection_cancel);
		btnCancel.setOnClickListener(this.btnNegativeListener);
		addTemplates(rootView);
	}

	private void addTemplates(View rootView){
		HistoryModel sampleCard = new HistoryModel(getString(R.string.fragment_post_template_sample_message), "user_idxxx", "user_namexxx", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME, Calendar.getInstance().getTime()));
		TableLayout lnrTable = (TableLayout)rootView.findViewById(R.id.lnr_fragment_post_template_table_container);
		if(templates != null){
			TableRow tableRow = new TableRow(getActivity());
			for(int i = 0; i < templates.size(); i++){
				final Template template = templates.get(i);
				if(i % 2 == 0){
					tableRow = new TableRow(getActivity());
					TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
					params.setMargins(0, 0, 0, WelfareUtil.dpToPx(MARGIN_BOTTOM_DP));
					tableRow.setLayoutParams(params);
					tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
					tableRow.setGravity(Gravity.CENTER_VERTICAL);
					lnrTable.addView(tableRow);
				}

				LayoutInflater inflater = getLayoutInflater(null);
				View lnrCardFrame = inflater.inflate(R.layout.item_thanks_card_frame, null);
				sampleCard.setTemplate(template);
				TCDetailFragment.buildTCFrame(getContext(), lnrCardFrame, sampleCard, false);
				lnrCardFrame.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						setSelectedTemplate(template);
						TemplateSelectionDialog.this.btnPositiveListener.onClick(v);
					}
				});
				tableRow.addView(lnrCardFrame);
			}
		}
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public void setDept(List<DeptModel> depts){
		this.depts = depts;
	}

	// public Template getSelectedTemplate() {
	// return adapter.getItem(adapter.getSelectedPosition());
	// }
	//
	// private List<Template> getDummy(){
	// List<Template> result = new ArrayList<>();
	// for(int i = 0; i < 3; i++){
	// Template template = new Template();
	// template.templateId = i;
	// template.name = "Template " + i;
	// result.add(template);
	// }
	// return result;
	// }
}
