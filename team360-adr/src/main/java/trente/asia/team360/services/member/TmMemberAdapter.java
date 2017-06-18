package trente.asia.team360.services.member;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.team360.BuildConfig;
import trente.asia.team360.R;
import trente.asia.team360.databinding.ViewGridMemberBinding;
import trente.asia.team360.services.model.StampModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by rakuishi on 6/22/14.
 */
public class TmMemberAdapter extends BaseAdapter {

    private Context mContext;

    private List<UserModel> users = new ArrayList<UserModel>();

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

//    public TmMemberAdapter(Context context) {
//        super(context, 0);
//    }

    public TmMemberAdapter(Context context) {
        super();
        mContext = context;
    }


    @Override
    public int getCount() {
        return CCCollectionUtil.size(this.users);
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            ViewGridMemberBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.view_grid_member, parent, false);
            convertView = binding.getRoot();
        }
        ViewGridMemberBinding binding = DataBindingUtil.getBinding(convertView);
        binding.setMember(users.get(position));


        if (!CCStringUtil.isEmpty(users.get(position).avatarPath)) {
            // WfPicassoHelper.loadImage(context, BuildConfig.HOST + activityModel.activityUserAvatarPath, viewHolder.imgAvatar, null);
            WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, binding.hueImageview, users.get(position).avatarPath, R.drawable.wf_profile);
        }




        return convertView;


    }
}