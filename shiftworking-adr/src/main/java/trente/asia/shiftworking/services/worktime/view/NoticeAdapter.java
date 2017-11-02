package nguyenhoangviet.vpcorp.shiftworking.services.worktime.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.shiftworking.BuildConfig;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.NoticeModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * WorkTimeAdapter.
 *
 * @author TrungND
 */
public class NoticeAdapter extends ArrayAdapter<NoticeModel>{

	private List<NoticeModel>	lstNotice;
	private Context				mContext;
	private LayoutInflater		mInflater;
	// private SwApiHolder mHolder;

	public class BoardViewHolder{

		public TextView		txtTitle;
		public TextView		txtContent;
		public ImageView	imgAvatar;
		public LinearLayout	lnrCheck;

		public BoardViewHolder(View view){
			txtTitle = (TextView)view.findViewById(R.id.txt_id_title);
			txtContent = (TextView)view.findViewById(R.id.txt_id_content);
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			lnrCheck = (LinearLayout)view.findViewById(R.id.lnr_id_check);
		}
	}

	public NoticeAdapter(Context context, List<NoticeModel> lstNotice){
		super(context, R.layout.item_work_notifice_list, lstNotice);
		this.mContext = context;
		this.lstNotice = lstNotice;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent){

		NoticeModel model = this.lstNotice.get(position);

		convertView = mInflater.inflate(R.layout.item_work_notifice_list, null);
		BoardViewHolder holder = new BoardViewHolder(convertView);

		if(!CCStringUtil.isEmpty(model.userAvatar)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.userAvatar, holder.imgAvatar, null);
		}

		holder.txtContent.setText(model.secondString);
		holder.txtTitle.setText(model.firstString);
		holder.lnrCheck.removeAllViews();
		if(!CCCollectionUtil.isEmpty(model.checks)){
			holder.lnrCheck.setVisibility(View.VISIBLE);
			int maxChecker = model.checks.size() > SwConst.MAX_CHECK_USER ? (SwConst.MAX_CHECK_USER - 1) : model.checks.size();
			for(int i = 0; i < maxChecker; i++){
				UserModel userModel = model.checks.get(i);
				View checkerView = mInflater.inflate(R.layout.item_notice_list_checker, null);
				ImageView imgToUserAvatar = (ImageView)checkerView.findViewById(R.id.img_id_avatar);
				if(!CCStringUtil.isEmpty(userModel.avatarPath)){
					WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + userModel.avatarPath, imgToUserAvatar, null);
				}
				holder.lnrCheck.addView(checkerView);
			}

			if(model.checks.size() > SwConst.MAX_CHECK_USER){
				View numberView = mInflater.inflate(R.layout.item_notice_list_number, null);
				TextView txtNumber = (TextView)numberView.findViewById(R.id.txt_id_number);
				txtNumber.setText("+" + String.valueOf(model.checks.size() - (SwConst.MAX_CHECK_USER - 1)));
				holder.lnrCheck.addView(numberView);
			}
		}

		return convertView;
	}

    public void clearAll(){
        this.lstNotice.clear();
        this.notifyDataSetChanged();
    }
}
