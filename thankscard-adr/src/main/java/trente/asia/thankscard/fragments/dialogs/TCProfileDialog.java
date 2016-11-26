package trente.asia.thankscard.fragments.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * TCProfileDialog
 *
 * @author TrungND
 */
public class TCProfileDialog extends ChiaseDialog{

	private Context mContext;

	public TCProfileDialog(Context context){
        super(context, R.style.tc_profile_dialog);
        this.mContext = context;
    }

	public TCProfileDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public TCProfileDialog setDialogProfileDetail(){
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dialog_user_profile);

		ImageView imgClose = (ImageView)this.findViewById(R.id.img_id_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				TCProfileDialog.this.dismiss();
			}
		});

		return this;
	}

	public TCProfileDialog updateProfileDetail(String userName, String photoUrl){

		ImageView imgPhoto = (ImageView)this.findViewById(R.id.img_id_photo);
		imgPhoto.setImageResource(R.drawable.wf_profile);
		if(!CCStringUtil.isEmpty(photoUrl)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + photoUrl, imgPhoto, null);
		}

		TextView txtUserName = (TextView)this.findViewById(R.id.txt_id_user_name);
		txtUserName.setText(userName);

		return this;
	}
}
