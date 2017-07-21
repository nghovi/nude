package trente.asia.calendar.services.todo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.calendar.services.todo.view.TodoListAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfDialog;

/**
 * TodoListFragment
 *
 * @author VietNH
 */
public class TodoListFragment extends AbstractClFragment{

	private LinearLayout	lnrUnfinished;
	private LinearLayout	lnrFinished;
	private TodoListAdapter	adapter;
	private List<Todo>		todoList;
	private Button			btnShowFinished;
	private WfDialog		dlgDeleteConfirm;
	private LayoutInflater	inflater;
	private SwipeLayout		swipeLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(null, getString(R.string.todo_title), R.drawable.cl_action_add);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		lnrUnfinished = (LinearLayout)getView().findViewById(R.id.lnr_unfinished_todo_containter);
		lnrFinished = (LinearLayout)getView().findViewById(R.id.lnr_finished_todo_containter);
		btnShowFinished = (Button)getView().findViewById(R.id.btn_fragment_todo_list_show_finished);
		btnShowFinished.setOnClickListener(this);
		inflater = LayoutInflater.from(activity);
		todoList = new ArrayList<>();
		todoList.add(new Todo());
		todoList.add(new Todo());
		todoList.add(new Todo());
		todoList.add(new Todo());
		buildTodos();
	}

	@Override
	protected void initData(){
		// loadTodoList();
	}

	private void loadTodoList(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetDateString", "2017/02/22");
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(ClConst.API_TODO_LIST, jsonObject, true);

	}

	protected void successLoad(JSONObject response, String url){
		todoList = CCJsonUtil.convertToModelList(response.optString("todos"), Todo.class);
		buildTodos();
	}

	private void buildTodos(){
		lnrFinished.removeAllViews();
		lnrUnfinished.removeAllViews();
		for(Todo todo : todoList){
			buildTodoItem(todo);
		}
	}

	private void buildTodoItem(final Todo todo){
		if(todo.isFinish){
			View cell = inflater.inflate(R.layout.item_todo_finished, null);
			TextView txtDate = (TextView)cell.findViewById(R.id.txt_item_todo_date);
			TextView txtTitle = (TextView)cell.findViewById(R.id.txt_item_todo_title);
			RadioButton radioButton = (RadioButton)cell.findViewById(R.id.radio);
			radioButton.setChecked(true);
			txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME)));
			txtTitle.setText(todo.name);
			lnrFinished.addView(cell);
		}else{
			final SwipeLayout cell = (SwipeLayout)inflater.inflate(R.layout.item_todo_unfinished, null);
			cell.getSurfaceView().setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					gotoTodoDetail(todo);
				}
			});
			// set show mode
			cell.setShowMode(SwipeLayout.ShowMode.LayDown);

			// add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
			cell.addDrag(SwipeLayout.DragEdge.Left, cell.findViewById(R.id.bottom_wrapper));

			cell.addSwipeListener(new SwipeLayout.SwipeListener() {

				@Override
				public void onClose(SwipeLayout layout){
					// when the SurfaceView totally cover the BottomView.
				}

				@Override
				public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset){
					// you are swiping.
				}

				@Override
				public void onStartOpen(SwipeLayout layout){

				}

				@Override
				public void onOpen(SwipeLayout layout){
					// when the BottomView totally show.
					swipeLayout = layout;
				}

				@Override
				public void onStartClose(SwipeLayout layout){

				}

				@Override
				public void onHandRelease(SwipeLayout layout, float xvel, float yvel){
					// when user's hand released.
				}
			});

			TextView txtDate = (TextView)cell.findViewById(R.id.txt_item_todo_date);
			TextView txtTitle = (TextView)cell.findViewById(R.id.txt_item_todo_title);
			RadioButton radioButton = (RadioButton)cell.findViewById(R.id.radio);
			radioButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					finishTodo(todo);
				}
			});

			Button btnDelete = (Button)cell.findViewById(R.id.btn_id_delete);
			btnDelete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					showDeleteDialog(todo);
				}
			});

			txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME)));
			txtTitle.setText(todo.name);
			lnrUnfinished.addView(cell);
		}
	}

	private void finishTodo(Todo todo){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", todo.key);
			jsonObject.put("name", todo.name);
			jsonObject.put("note", todo.note);
			jsonObject.put("isFinish", true);
			jsonObject.put("limitDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, Calendar.getInstance().getTime()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, true);
		Toast.makeText(activity, "call api to update finish", Toast.LENGTH_LONG).show();
	}

	private void showDeleteDialog(Todo todo){
		if(dlgDeleteConfirm == null){
			dlgDeleteConfirm = new WfDialog(activity);
			dlgDeleteConfirm.setDialogTitleButton(getString(R.string.sure_to_delete), getString(R.string.chiase_common_ok), getString(R.string.wf_cancel), new View.OnClickListener() {

				@Override
				public void onClick(View v){
					callDeleteApi();
				}
			});
			dlgDeleteConfirm.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog){
					//// TODO: 7/20/17 hide again
					Toast.makeText(activity, "TODO HIDE DELETE BUTTON", Toast.LENGTH_LONG).show();
					if(swipeLayout != null){
						swipeLayout.close(true, true);
					}
				}
			});
		}
		dlgDeleteConfirm.show();
	}

	private void callDeleteApi(){
		Toast.makeText(activity, "todo call api delete", Toast.LENGTH_LONG).show();
		JSONObject jsonObject = new JSONObject();
		try{
			// jsonObject.put("key", key);
			// jsonObject.put("name", edtTitle.getText().toString());
			// jsonObject.put("note", edtContent.getText().toString());
			jsonObject.put("limitDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, Calendar.getInstance().getTime()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, true);
	}

	private void gotoTodoDetail(Todo todo){
		TodoDetailFragment fragment = new TodoDetailFragment();
		fragment.setTodo(todo);
		gotoFragment(fragment);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_todo;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_fragment_todo_list_show_finished:
			onClickShowFinishedTodoBtn();
			break;
		case R.id.img_id_header_right_icon:
			gotoTodoDetail(null);
			break;
		default:
			break;
		}
	}

	private void onClickShowFinishedTodoBtn(){
		int visibility = lnrFinished.getVisibility();
		if(visibility == View.VISIBLE){
			btnShowFinished.setText(getString(R.string.show_finished_task));
			lnrFinished.setVisibility(View.GONE);
		}else{
			btnShowFinished.setText(getString(R.string.hide_unfinished_task));
			lnrFinished.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){

	}

}
