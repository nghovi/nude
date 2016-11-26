package trente.asia.shiftworking.services.worktime.view;

import java.util.List;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.worktime.listener.ItemWorkTimeClickListener;
import trente.asia.shiftworking.services.worktime.model.WorkingTimeModel;

/**
 * WorkTimeAdapter.
 *
 * @author TrungND
 */
public class WorkTimeAdapter extends BaseSwipeAdapter{

	private List<WorkingTimeModel>		lstWorkingTime;
	private Context						mContext;
	private ItemWorkTimeClickListener	listener;

	public class BoardViewHolder{

		public TextView		txtWorkingType;
		public TextView		txtWorkTime;
		public Button		btnModify;
		public Button		btnDelete;
		public SwipeLayout	swipeLayout;

		public BoardViewHolder(View view){
			txtWorkingType = (TextView)view.findViewById(R.id.txt_id_working_type);
			txtWorkTime = (TextView)view.findViewById(R.id.txt_id_work_time);
			btnModify = (Button)view.findViewById(R.id.btn_id_modify);
			btnDelete = (Button)view.findViewById(R.id.btn_id_delete);
			swipeLayout = (SwipeLayout)view.findViewById(R.id.swipe);
		}
	}

	public WorkTimeAdapter(Context context, List<WorkingTimeModel> lstWorkingTime, ItemWorkTimeClickListener listener){
		this.mContext = context;
		this.lstWorkingTime = lstWorkingTime;
		this.listener = listener;
	}

	@Override
	public int getSwipeLayoutResourceId(int position){
		return R.id.swipe;
	}

	@Override
	public View generateView(int position, ViewGroup parent){
		final WorkingTimeModel model = this.lstWorkingTime.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View convertView = mInflater.inflate(R.layout.item_work_checkin_list, null);
		final BoardViewHolder holder = new BoardViewHolder(convertView);

		holder.txtWorkTime.setText(model.timeLog);
		holder.txtWorkingType.setText(model.workingTypeName);
		holder.btnModify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(listener != null){
                    holder.swipeLayout.close(true);
					listener.onItemModifyListener(model);
				}
			}
		});

		holder.btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(listener != null){
					listener.onItemDeleteListener(model);
				}
			}
		});

		return convertView;
	}

	@Override
	public void fillValues(int position, View convertView){

	}

	@Override
	public int getCount(){
		return this.lstWorkingTime.size();
	}

	@Override
	public Object getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position){
		return position;
	}
}
