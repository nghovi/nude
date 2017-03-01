package trente.asia.calendar.commons.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * UserListLinearLayout
 *
 * @author TrungND
 */

public class UserListLinearLayout extends LinearLayout {

    private Context mContext;
    private boolean isGravityLeft = false;

    private List<UserModel> lstUser;

    public void setGravityLeft(boolean gravityLeft) {
        isGravityLeft = gravityLeft;
    }

    public UserListLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        this.setGravity(Gravity.CENTER);
        this.setOrientation(HORIZONTAL);
    }

    public UserListLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setGravity(Gravity.CENTER);
        this.setOrientation(HORIZONTAL);
    }

    public void show(List<UserModel> lstUser, int imageSize) {
        if (isGravityLeft) {
            this.setGravity(Gravity.LEFT);
        }
        this.removeAllViews();
        this.lstUser = lstUser;
        if (!CCCollectionUtil.isEmpty(lstUser)) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(params);

            // image size 20 x 20:
            int maxUserNumber = (int) (this.getWidth() / imageSize);
            if (lstUser.size() <= maxUserNumber) {
                for (UserModel userModel : lstUser) {
                    addUserView(userModel);
                }
            } else {
                for (int index = 0; index < maxUserNumber - 1; index++) {
                    UserModel userModel = lstUser.get(index);
                    addUserView(userModel);
                }

                // add text
                TextView txtSchedule = new TextView(mContext);
                // int textDimension = (int)mContext.getResources()
                // .getDimension(R.dimen.margin_40dp);
                LayoutParams textLayout = new LayoutParams(imageSize,
                        imageSize);
                txtSchedule.setLayoutParams(textLayout);
                int textSize = (int) (getResources().getDimension(R.dimen
                        .margin_20dp) / getResources().getDisplayMetrics()
                        .density);
                txtSchedule.setTextSize(textSize);
                txtSchedule.setText("+" + (lstUser.size() - maxUserNumber + 1));
                txtSchedule.setGravity(Gravity.CENTER);
                this.addView(txtSchedule);
            }
        }

    }

    private void addUserView(UserModel userModel) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View userItemView = inflater.inflate(R.layout.view_user_item, null);
        if (!CCStringUtil.isEmpty(userModel.avatarPath)) {
            ImageView imgAvatar = (ImageView) userItemView.findViewById(R.id
                    .img_id_avatar);
            WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + userModel
                    .avatarPath, imgAvatar, null);
        }
        this.addView(userItemView);
    }

    public String formatUserList() {
        StringBuilder builder = new StringBuilder();
        if (!CCCollectionUtil.isEmpty(lstUser)) {
            for (UserModel userModel : lstUser) {
                builder.append(userModel.key + ",");
            }
        }
        return builder.toString();
    }

    public List<UserModel> getLstUser() {
        return lstUser;
    }
}
