package nguyenhoangviet.vpcorp.calendar.commons.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.services.todo.model.Todo;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

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
		Calendar c = Calendar.getInstance();
		c.setTime(todo.limitDate);
		String lang = Locale.getDefault().getLanguage();
		String dayStr = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if(lang.equals("ja")){
			dayStr += "日";
		}else{
			dayStr += getDayOfMonthSuffix(Integer.parseInt(dayStr));
		}
		((TextView)findViewById(R.id.txt_item_todo_date)).setText(dayStr);
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

	String getDayOfMonthSuffix(final int n){
		if(n >= 11 && n <= 13){
			return "th";
		}
		switch(n % 10){
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}

}
