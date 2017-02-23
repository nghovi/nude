package trente.asia.calendar.services.calendar.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.listener.CsOnCheckedChangeListener;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.views.UserListLinearLayout;
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

		public ImageView				imgAvatar;

		public UserListLinearLayout		lnrUserList;
		public TextView					txtName;
		public CheckableLinearLayout	lnrItem;
		public ImageView				imgCheck;
		public Boolean					isLoaded	= false;

		public CalendarViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtName = (TextView)view.findViewById(R.id.txt_id_name);
			lnrItem = (CheckableLinearLayout)view.findViewById(R.id.lnr_id_item);
			lnrUserList = (UserListLinearLayout)view.findViewById(R.id.lnr_fragment_pager_container_user_list);
			imgCheck = (ImageView)view.findViewById(R.id.img_checked);
		}
	}

	public CalendarAdapter(Context context, List<CalendarModel> calendarList){
		super(context, R.layout.item_calendar_list, calendarList);
		this.mContext = context;
		this.calendarList = calendarList;
	}

	public View getView(final int position, View convertView, final ViewGroup parent){

		final CalendarModel model = this.calendarList.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_calendar_list, null);

		final CalendarViewHolder holder = new CalendarViewHolder(convertView);

		holder.txtName.setText(model.calendarName);
		if(!CCStringUtil.isEmpty(model.imagePath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.imagePath, holder.imgAvatar, null);
		}
		// final View finalConvertView = convertView;
		holder.lnrItem.setOnCheckedChangeListener(new CsOnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(Checkable view, boolean isChecked){
				(((ListView)parent)).setItemChecked(position, isChecked);
				if(isChecked){
					holder.imgCheck.setVisibility(View.VISIBLE);
				}else{
					holder.imgCheck.setVisibility(View.GONE);
				}
			}
		});

		holder.lnrUserList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw(){
				if(holder.lnrUserList.getWidth() > 0 && !holder.isLoaded){
					holder.isLoaded = true;
					if(!CCCollectionUtil.isEmpty(model.calendarUsers)){
						holder.lnrUserList.setGravity(Gravity.LEFT);
						holder.lnrUserList.show(model.calendarUsers, (int)mContext.getResources().getDimension(R.dimen.margin_30dp));
					}
				}

				return true;
			}
		});

		return convertView;
	}

	public void buildItemLayout() {

	}

	public void add(CalendarModel item, int position){
		this.calendarList.add(position, item);
		this.notifyDataSetChanged();
	}

	public int findPosition4Code(String calendarId){
		int position = -1;
		for(int i = 0; i < calendarList.size(); i++){
			CalendarModel calendarModel = calendarList.get(i);
			if(calendarModel.key.equals(calendarId)){
				position = i;
				break;
			}
		}
		return position;
	}

}
