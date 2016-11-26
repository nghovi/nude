package trente.asia.team360.services.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.team360.BuildConfig;
import trente.asia.team360.R;
import trente.asia.team360.services.model.StampModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by rakuishi on 6/22/14.
 */
public class TmMemberAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<UserModel> users = new ArrayList<UserModel>();

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    private String[] mHueArray = {
            "FF4040", "FFCF40", "9FFF40", "40FF6F",
            "40FFFF", "406FFF", "9F40FF", "FF40CF"
    };

    private static class ViewHolder {
        public ImageView hueImageView;
        public TextView hueTextView;
    }

    public TmMemberAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return users.size();
    }

    public Object getItem(int position) {
        return users.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.view_grid_member, null);
            holder = new ViewHolder();
            holder.hueImageView = (ImageView) convertView.findViewById(R.id.hue_imageview);
            holder.hueTextView = (TextView) convertView.findViewById(R.id.hue_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.hueTextView.setText(users.get(position).userName);

        if (!CCStringUtil.isEmpty(users.get(position).avatarPath)) {
            // WfPicassoHelper.loadImage(context, BuildConfig.HOST + activityModel.activityUserAvatarPath, viewHolder.imgAvatar, null);
            WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, holder.hueImageView, users.get(position).avatarPath, R.drawable.wf_profile);
        }


        return convertView;
    }
}