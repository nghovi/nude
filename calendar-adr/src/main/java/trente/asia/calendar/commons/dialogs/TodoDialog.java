package trente.asia.calendar.commons.dialogs;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import trente.asia.calendar.R;
import trente.asia.calendar.services.todo.model.Todo;

/**
 * TodoDialog
 *
 * @author VietNH
 */
public class TodoDialog extends ClDialog{

	private View.OnClickListener	onDelete;
	private View.OnClickListener	onFinish;

	private Date					selectedDate;

	public TodoDialog(Context context, Todo todo, View.OnClickListener onDelete, View.OnClickListener onFinish){
		super(context);

		this.setContentView(R.layout.dialog_todo);
		this.onDelete = onDelete;
		this.onFinish = onFinish;

		((TextView)findViewById(R.id.txt_item_todo_title)).setText(todo.name);
		((TextView)findViewById(R.id.txt_dlg_todo_content)).setText(todo.note);
		findViewById(R.id.radio).setOnClickListener(this.onFinish);
		findViewById(R.id.btn_todo_dialog_delete).setOnClickListener(this.onDelete);
		findViewById(R.id.img_id_close).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				dismiss();
			}
		});

		Window window = this.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
	}

}
