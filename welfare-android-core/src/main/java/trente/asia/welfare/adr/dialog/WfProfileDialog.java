package trente.asia.welfare.adr.dialog;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * TCProfileDialog
 *
 * @author TrungND
 */
public class WfProfileDialog extends ChiaseDialog{

	private Context	mContext;

	public WfProfileDialog(Context context){
		super(context, R.style.wf_profile_dialog);
		this.mContext = context;
	}

	public WfProfileDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public WfProfileDialog setDialogProfileDetail(int topDp, int BottomDp){
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dialog_user_profile);

		ImageView imgClose = (ImageView)this.findViewById(R.id.img_id_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				WfProfileDialog.this.dismiss();
			}
		});

		LinearLayout container = (LinearLayout)this.findViewById(R.id.lnr_dialog_user_profile_root);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(0, WelfareUtil.dpToPx(topDp), 0, WelfareUtil.dpToPx(BottomDp));
		container.setLayoutParams(params);

		return this;
	}

	public void show(String host, String userName, String photoUrl){

		ImageView imgPhoto = (ImageView)this.findViewById(R.id.img_id_photo);
		imgPhoto.setImageResource(R.drawable.wf_profile);
		if(!CCStringUtil.isEmpty(photoUrl)){
			WfPicassoHelper.loadImage(mContext, host + photoUrl, imgPhoto, null);
		}

		TextView txtUserName = (TextView)this.findViewById(R.id.txt_id_user_name);
		txtUserName.setText(userName);

		this.show();
	}
}
