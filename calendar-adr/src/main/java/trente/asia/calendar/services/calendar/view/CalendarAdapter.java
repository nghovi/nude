package trente.asia.calendar.services.calendar.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * BoardAdapter.
 *
 * @author TrungND
 */
public class CalendarAdapter extends ArrayAdapter<CalendarModel>{

	private List<CalendarModel>	calendarList;
	private Context				mContext;

	public class CalendarViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtName;

		public CalendarViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtName = (TextView)view.findViewById(R.id.txt_id_name);
		}
	}

	public CalendarAdapter(Context context, List<CalendarModel> calendarList){
		super(context, R.layout.item_calendar_list, calendarList);
		this.mContext = context;
		this.calendarList = calendarList;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		CalendarModel model = this.calendarList.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_calendar_list, null);
		CalendarViewHolder holder = new CalendarViewHolder(convertView);

        holder.txtName.setText(model.name);
        if(!CCStringUtil.isEmpty(model.imagePath)){
            WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.imagePath, holder.imgAvatar, null);
        }

		return convertView;
	}

	public void add(CalendarModel item, int position){
		this.calendarList.add(position, item);
		this.notifyDataSetChanged();
	}
}
