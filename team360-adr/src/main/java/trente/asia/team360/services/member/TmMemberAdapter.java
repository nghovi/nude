package trente.asia.team360.services.member;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import trente.asia.team360.BuildConfig;
import trente.asia.team360.R;
import trente.asia.team360.databinding.ViewGridMemberBinding;
import trente.asia.team360.services.entity.UserEntity;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by rakuishi on 6/22/14.
 */
public class TmMemberAdapter extends BaseAdapter implements RealmChangeListener {

    private Context mContext;

    private RealmResults<UserEntity> mUsers;


    public TmMemberAdapter(Context context, RealmResults<UserEntity> users) {
        super();
        mContext = context;
        mUsers = users;
        mUsers.addChangeListener(this);
    }


    @Override
    public int getCount() {
        return CCCollectionUtil.size(this.mUsers);
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
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
        binding.setMember(mUsers.get(position));

        if (!CCStringUtil.isEmpty(mUsers.get(position).avatarPath)) {
            // WfPicassoHelper.loadImage(context, BuildConfig.HOST + activityModel.activityUserAvatarPath, viewHolder.imgAvatar, null);
            WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, binding.hueImageview, mUsers.get(position).avatarPath, R.drawable.wf_profile);
        }

        return convertView;
    }


    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }
}