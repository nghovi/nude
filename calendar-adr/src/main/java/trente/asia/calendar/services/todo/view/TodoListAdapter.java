package trente.asia.calendar.services.todo.view;

import java.util.List;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 5/13/2016.
 */
public class TodoListAdapter extends BaseSwipeAdapter{

	private List<Todo>					todos;
	Context								context;
	private OnTodoItemActionListener	onTodoItemActionListener;

	public interface OnTodoItemActionListener{

		void onClickDelete(Todo todo);

		void onClickRadioBtn(Todo todo);
	}

	public TodoListAdapter(Context context, List<Todo> objects, OnTodoItemActionListener onTodoItemActionListener){
		this.todos = objects;
		this.context = context;
		this.onTodoItemActionListener = onTodoItemActionListener;
	}

	@Override
	public int getSwipeLayoutResourceId(int position){
		return R.id.swipe;
	}

	@Override
	public View generateView(int position, ViewGroup parent){
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View convertView = mInflater.inflate(R.layout.item_todo_unfinished, null);

		return convertView;
	}

	@Override
	public void fillValues(int position, View convertView){
		final Todo todo = todos.get(position);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_todo_date);
		viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.txt_item_todo_title);
		viewHolder.radioButton = (RadioButton)convertView.findViewById(R.id.radio);
		viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onTodoItemActionListener.onClickRadioBtn(todo);

			}
		});
		viewHolder.btnDelete = (Button)convertView.findViewById(R.id.btn_id_delete);
		viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onTodoItemActionListener.onClickDelete(todo);
			}
		});

		viewHolder.txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, todo.limitDate));
		viewHolder.txtTitle.setText(todo.name);
	}

	@Override
	public int getCount(){
		return todos.size();
	}

	@Override
	public Object getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position){
		return position;
	}

	private class ViewHolder{

		public TextView		txtDate;
		public TextView		txtTitle;
		public RadioButton	radioButton;
		public Button		btnDelete;
	}
}
