package trente.asia.calendar.services.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;
import com.daimajia.swipe.SwipeLayout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.calendar.services.todo.view.TodoListAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * TodoListFragment
 *
 * @author VietNH
 */
public class TodoListTodayFragment extends AbstractClFragment{

	private LinearLayout	lnrUnfinished;
	private LinearLayout	lnrFinished;
	private TodoListAdapter	adapter;
	private List<Todo>		todoList;
	private WfDialog		dlgDeleteConfirm;
	private LayoutInflater	inflater;
	private SwipeLayout		swipeLayout;
	private Date			today;
	private ScrollView		scrollView;
	private Todo			selectedTodo;
	private int				footerItemId;
	private Date			selectedDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_todo_list_today, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		today = Calendar.getInstance().getTime();
		initHeader(R.drawable.wf_back_white, CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate), null);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		scrollView = (ScrollView)getView().findViewById(R.id.scr_todo);
		lnrUnfinished = (LinearLayout)getView().findViewById(R.id.lnr_unfinished_todo_containter);
		lnrFinished = (LinearLayout)getView().findViewById(R.id.lnr_finished_todo_containter);
		inflater = LayoutInflater.from(activity);
	}

	@Override
	protected void initData(){
		loadTodoList();
	}

	private void loadTodoList(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(ClConst.API_TODO_LIST, jsonObject, true);
	}

	protected void successLoad(JSONObject response, String url){
		try{
			todoList = LoganSquare.parseList(response.optString("todoList"), Todo.class);
			todoList = filterForToday(todoList);
			if(CCCollectionUtil.isEmpty(todoList)){
				getView().findViewById(R.id.txt_todo_empty).setVisibility(View.VISIBLE);
				scrollView.setVisibility(View.GONE);
			}else{
				getView().findViewById(R.id.txt_todo_empty).setVisibility(View.GONE);
				scrollView.setVisibility(View.VISIBLE);
				buildTodos();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private List<Todo> filterForToday(List<Todo> todoList){
		List<Todo> todayTodos = new ArrayList<>();
		String todayString = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate);
		for(Todo todo : todoList){
			if(todo.isFinish == false && !CCStringUtil.isEmpty(todo.limitDate) && todayString.equals(todo.limitDate.split(" ")[0])){
				todayTodos.add(todo);
			}
		}
		return todayTodos;
	}

	private void buildTodos(){
		lnrFinished.removeAllViews();
		lnrUnfinished.removeAllViews();
		for(Todo todo : todoList){
			buildTodoItem(todo);
		}
	}

	private void buildTodoItem(final Todo todo){

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
				showDeleteDialog(todo);
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
				finishTodo(todo, true);
			}
		});

		if(CCStringUtil.isEmpty(todo.limitDate)){
			txtTitle.setText(getString(R.string.no_deadline));
		}else{
			Date date = CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME);
			if(CCDateUtil.compareDate(today, date, false) >= 0){
				txtDate.setTextColor(Color.RED);
			}
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, today).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date))){
				txtDate.setText(getString(R.string.chiase_common_today));
			}else{
				txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MM_DD, date));
			}
		}
		txtTitle.setText(todo.name);
		cell.setTag(lnrUnfinished.getChildCount());
		lnrUnfinished.addView(cell);
	}

	private void finishTodo(Todo todo, boolean isFinish){
		this.selectedTodo = todo;
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", todo.key);
			jsonObject.put("name", todo.name);
			jsonObject.put("note", todo.note);
			jsonObject.put("isFinish", isFinish);
			jsonObject.put("limitDate", CCStringUtil.isEmpty(todo.limitDate) ? todo.limitDate : todo.limitDate.split(":")[0]);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, true);
	}

	private void showDeleteDialog(final Todo todo){
		if(dlgDeleteConfirm == null || !dlgDeleteConfirm.isShowing()){
			dlgDeleteConfirm = new WfDialog(activity);
			dlgDeleteConfirm.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog){
					if(swipeLayout != null){
						swipeLayout.close(true, true);
					}
				}
			});
			dlgDeleteConfirm.setDialogTitleButton(getString(R.string.sure_to_delete), getString(R.string.chiase_common_ok), getString(R.string.wf_cancel), new View.OnClickListener() {

				@Override
				public void onClick(View v){
					callDeleteApi(todo);
				}
			});
			dlgDeleteConfirm.show();
		}

	}

	private void callDeleteApi(Todo todo){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", todo.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_DELETE, jsonObject, true);
	}

	private void gotoTodoDetail(Todo todo){
		TodoDetailFragment fragment = new TodoDetailFragment();
		fragment.setTodo(todo);
		gotoFragment(fragment);
	}

	@Override
	public int getFooterItemId(){
		return footerItemId;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			gotoTodoDetail(null);
			break;
		default:
			break;
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(ClConst.API_TODO_DELETE.equals(url)){
			dlgDeleteConfirm.dismiss();
		}
		loadTodoList();
	}

	public void setFooterItemId(int footerItemId){
		this.footerItemId = footerItemId;
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}
}
