package nguyenhoangviet.vpcorp.shiftworking.services.worktime.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.shiftworking.BuildConfig;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.WorkingTimeDailyModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareFormatUtil;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;
import nguyenhoangviet.vpcorp.welfare.adr.view.SelectableRoundedImageView;

/**
 * WorkerAdapter.
 *
 * @author TrungND
 */
public class WorkerAdapter extends ArrayAdapter<WorkingTimeDailyModel>{

	private List<WorkingTimeDailyModel>	lstNotice;

	public class BoardViewHolder{

		public SelectableRoundedImageView	avatar;
		public TextView						txtUserName;
		public TextView						txtProjectLocation;
		public TextView						txtShiftTime;
		public TextView						txtWorkTime;

		public BoardViewHolder(View view){
			avatar = (SelectableRoundedImageView)view.findViewById(R.id.img_id_avatar);
			txtUserName = (TextView)view.findViewById(R.id.txt_id_user_name);
			txtProjectLocation = (TextView)view.findViewById(R.id.txt_id_project_location);
			txtShiftTime = (TextView)view.findViewById(R.id.txt_id_shift_time);
			txtWorkTime = (TextView)view.findViewById(R.id.txt_id_work_time);

		}
	}

	public WorkerAdapter(Context context, List<WorkingTimeDailyModel> lstNotice){
		super(context, R.layout.item_worker_list, lstNotice);
		this.lstNotice = lstNotice;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		WorkingTimeDailyModel model = this.lstNotice.get(position);

		LayoutInflater mInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_worker_list, null);
		BoardViewHolder holder = new BoardViewHolder(convertView);

		if(model.user != null && !CCStringUtil.isEmpty(model.user.avatarPath)){
            if(model.bitmap != null){
                holder.avatar.setImageBitmap(model.bitmap);
            }else{
                WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + model.user.avatarPath, holder.avatar, null, model);
            }
		}

		holder.txtUserName.setText(model.user.userName);
		if(model.project != null){
			holder.txtProjectLocation.setText(model.project.projectLocation);
		}

		if(!CCStringUtil.isEmpty(model.startShift)){
            holder.txtShiftTime.setText(WelfareFormatUtil.connect2String(model.startShift, model.endShift, "-"));
		}

		if(!CCStringUtil.isEmpty(model.startCheckin)){
            holder.txtWorkTime.setText(WelfareFormatUtil.connect2String(model.startCheckin, model.endCheckin, "-"));
		}

		return convertView;
	}

    public void clearAll(){
        this.lstNotice.clear();
        this.notifyDataSetChanged();
    }
}
