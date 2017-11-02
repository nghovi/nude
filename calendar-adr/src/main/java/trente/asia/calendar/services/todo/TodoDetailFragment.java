package nguyenhoangviet.vpcorp.calendar.services.todo;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import asia.chiase.core.util.CCFormatUtil;
import nguyenhoangviet.vpcorp.android.view.ChiaseEditText;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.defines.ClConst;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.AbstractClFragment;
import nguyenhoangviet.vpcorp.calendar.services.todo.model.Todo;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

/**
 * TodoDetailFragment
 *
 * @author VietNH
 */
public class TodoDetailFragment extends AbstractClFragment{

	private Todo				todo;
	private EditText			edtTitle;
	private EditText			edtContent;
	private ChiaseEditText		txtDeadline;
	private DatePickerDialog	datePickerDialog;
	private boolean isEditable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_todo_detail, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		if(todo != null){
			loadTodoDetail();
		}
	}

	public void setEditable (boolean isEditable){
		this.isEditable = isEditable;
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
		onClickBackBtn();
	}

	@Override
	public void initView(){
		super.initView();
		edtTitle = (EditText)getView().findViewById(R.id.txt_fragment_todo_detail_title);
		edtContent = (EditText)getView().findViewById(R.id.txt_fragment_todo_detail_content);
		txtDeadline = (ChiaseEditText)getView().findViewById(R.id.txt_deadline);
		if(todo != null && todo.isFinish == true || !isEditable){
			initHeader(R.drawable.wf_back_white, getString(R.string.todo_title), null);
			edtTitle.setFocusable(false);
			edtContent.setFocusable(false);
		}else{
			getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
			initHeader(R.drawable.wf_back_white, getString(R.string.todo_title), R.drawable.cl_action_save);
			txtDeadline.setOnClickListener(this);
			getView().findViewById(R.id.lnr_deadline).setOnClickListener(this);
		}

		Calendar calendar = Calendar.getInstance();
		if(todo != null && todo.limitDate != null){
			calendar.setTime(todo.limitDate);
		}
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + (month + 1) + "/" + (dayOfMonth);
				// txtDeadline.setTextColor(Color.BLACK);
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

		datePickerDialog.setCanceledOnTouchOutside(false);
	}

	private void buildLayout(JSONObject response){
		try{
			todo = LoganSquare.parse(response.optString("detail"), Todo.class);
			if(todo != null){
				edtTitle.setText(todo.name);
				edtContent.setText(todo.note);
				// txtDeadline.setTextColor(Color.BLACK);
				if(todo.limitDate == null){
					txtDeadline.setText(getString(R.string.no_deadline));
				}else{
					txtDeadline.setText(CCFormatUtil.formatDateCustom("yyyy/M/d", todo.limitDate));
				}
			}else{
			}
		}catch(IOException e){
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
		case R.id.txt_deadline:
			datePickerDialog.show();
			break;
		default:
			break;
		}
	}

	private void onClickSaveIcon(){
		String limitDate = txtDeadline.getText().toString();
		if(!limitDate.contains("/")){
			limitDate = null;
		}
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
