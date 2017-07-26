package trente.asia.calendar.services.todo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * TodoListFragment
 *
 * @author VietNH
 */
public class TodoDetailFragment extends AbstractClFragment{

	private Todo				todo;
	private EditText			edtTitle;
	private EditText			edtContent;
	private TextView			txtDeadline;
	private DatePickerDialog	datePickerDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_todo_detail, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		if (todo != null) {
			loadTodoDetail();
		}
	}

	private void loadTodoDetail(){
		String todoKey = todo == null ? null : todo.key;
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", todoKey);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(ClConst.API_TODO_DETAIL, jsonObject, true);
	}

	protected void successLoad(JSONObject response, String url){
		buildLayout(response);
	}

	protected void successUpdate(JSONObject response, String url){
		if(todo == null || todo.key == null){
			onClickBackBtn();
		}else{
			loadTodoDetail();
		}
	}

	@Override
	public void initView(){
		super.initView();
		edtTitle = (EditText)getView().findViewById(R.id.txt_fragment_todo_detail_title);
		edtContent = (EditText)getView().findViewById(R.id.txt_fragment_todo_detail_content);
		txtDeadline = (TextView)getView().findViewById(R.id.txt_deadline);
		if(todo != null && todo.isFinish == true){
			initHeader(R.drawable.wf_back_white, getString(R.string.todo_title), null);
			edtTitle.setEnabled(false);
			edtContent.setEnabled(false);
		}else{
			getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
			initHeader(R.drawable.wf_back_white, getString(R.string.todo_title), R.drawable.cl_action_save);
			getView().findViewById(R.id.lnr_deadline).setOnClickListener(this);
		}

		Calendar calendar = Calendar.getInstance();
		if(todo != null && !CCStringUtil.isEmpty(todo.limitDate)){
			Date date = CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME_DATE);
			calendar.setTime(date);
		}
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtDeadline.setTextColor(Color.BLACK);
				txtDeadline.setText(startDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog){
				txtDeadline.setTextColor(Color.GRAY);
				txtDeadline.setText(getString(R.string.deadline));
			}
		});
	}

	private void buildLayout(JSONObject response){
		try {
			todo = LoganSquare.parse(response.optString("detail"), Todo.class);
			if(todo != null){
				edtTitle.setText(todo.name);
				edtContent.setText(todo.note);
				txtDeadline.setTextColor(Color.BLACK);
				if(CCStringUtil.isEmpty(todo.limitDate)){
					txtDeadline.setText(getString(R.string.no_deadline));
				}else{
					txtDeadline.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME)));
				}
			}else{
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			onClickSaveIcon();
			break;
		case R.id.lnr_deadline:
			datePickerDialog.show();
			break;
		default:
			break;
		}
	}

	private void onClickSaveIcon(){
		String limitDate = txtDeadline.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try{
			String key = todo != null ? todo.key : null;
			jsonObject.put("key", key);
			jsonObject.put("name", edtTitle.getText().toString());
			jsonObject.put("note", edtContent.getText().toString());
			jsonObject.put("limitDate", limitDate);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, true);
	}

	public void setTodo(Todo todo){
		this.todo = todo;
	}
}
