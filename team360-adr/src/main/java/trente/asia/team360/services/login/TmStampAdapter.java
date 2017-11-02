package nguyenhoangviet.vpcorp.team360.services.login;

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
import nguyenhoangviet.vpcorp.team360.BuildConfig;
import nguyenhoangviet.vpcorp.team360.R;
import nguyenhoangviet.vpcorp.team360.services.model.StampModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by rakuishi on 6/22/14.
 */
public class TmStampAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<StampModel> stamps = new ArrayList<StampModel>();

    public void setStamps(List<StampModel> stamps) {
        this.stamps = stamps;
    }


    private static class ViewHolder {
        public ImageView hueImageView;
        public TextView hueTextView;
    }

    public TmStampAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return stamps.size();
    }

    public Object getItem(int position) {
        return stamps.get(position);
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


        holder.hueTextView.setText(stamps.get(position).stampWord);

        if (!CCStringUtil.isEmpty(stamps.get(position).stampPath)) {
            WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, holder.hueImageView, stamps.get(position).stampPath, R.drawable.wf_profile);
        }


        return convertView;
    }
}