package trente.asia.welfare.adr.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * WfDialog
 *
 * @author TrungND
 */
public class WfDialog extends ChiaseDialog{

	private Context mContext;

	public WfDialog(Context context){
		super(context);
		this.mContext = context;
	}

	public WfDialog setDialogUpgradeVersion(){
		this.setContentView(R.layout.dialog_upgrade_version);

		LinearLayout lnrCancel = (LinearLayout)this.findViewById(R.id.lnr_id_cancel);
		lnrCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				WfDialog.this.dismiss();
			}
		});

		LinearLayout lnrOk = (LinearLayout)this.findViewById(R.id.lnr_id_ok);
		lnrOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				Toast.makeText(mContext, "Go to Google play", Toast.LENGTH_LONG).show();
			}
		});

		return this;
	}

	public WfDialog setDialogTitleButton(String strMessage, String strPositive, String strNegative, View.OnClickListener listener){
		this.setContentView(R.layout.dialog_title_button);
		if(!CCStringUtil.isEmpty(strMessage)){
			TextView txtMessage = (TextView)this.findViewById(R.id.txt_id_msg);
			txtMessage.setText(strMessage);
		}

		TextView txtPositive = (TextView)this.findViewById(R.id.txt_id_positive);
		if(!CCStringUtil.isEmpty(strPositive)){
			txtPositive.setText(strPositive);
		}
		txtPositive.setOnClickListener(listener);

		TextView txtNegative = (TextView)this.findViewById(R.id.txt_id_negative);
		txtNegative.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				dismiss();
			}
		});
		if(!CCStringUtil.isEmpty(strNegative)){
			txtNegative.setText(strNegative);
		}else{
			txtNegative.setVisibility(View.GONE);
		}

		return this;
	}

	public WfDialog setDialogPhotoDetail(String photoUrl){
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dialog_photo_detail);

		ImageView imgClose = (ImageView)this.findViewById(R.id.img_id_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				WfDialog.this.dismiss();
			}
		});

		ImageView imgPhoto = (ImageView)this.findViewById(R.id.img_id_photo);
		WfPicassoHelper.loadImage(mContext, photoUrl, imgPhoto, null);

		return this;
	}
}
