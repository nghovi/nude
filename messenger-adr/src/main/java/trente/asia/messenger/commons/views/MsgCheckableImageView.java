package nguyenhoangviet.vpcorp.messenger.commons.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.view.ChiaseCheckableImageView;
import nguyenhoangviet.vpcorp.android.view.listener.ChiaseOnCheckedChangeListener;
import nguyenhoangviet.vpcorp.messenger.R;

/**
 * MsgCheckableImageView
 *
 * @author TrungND
 */
public class MsgCheckableImageView extends ChiaseCheckableImageView{

	private TextView txtNumber;

	public MsgCheckableImageView(Context context){
		super(context);
		setOnCheckedChangeListener();
	}

	public MsgCheckableImageView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
		setOnCheckedChangeListener();
	}

	public MsgCheckableImageView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
		setOnCheckedChangeListener();
	}

	private void setOnCheckedChangeListener(){
		this.setOnCheckedChangeListener(new ChiaseOnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(Checkable view, boolean isChecked){
				if(isChecked){
					txtNumber.setTextColor(getResources().getColor(R.color.msg_message_action_active));
					MsgCheckableImageView.this.setEnabled(false);
				}else{
					txtNumber.setTextColor(getResources().getColor(R.color.wf_common_color_text));
					MsgCheckableImageView.this.setEnabled(true);
				}
			}
		});
	}

	public void increaseLike(){
		if(txtNumber != null){
			String number = CCStringUtil.toString(txtNumber.getText());
			txtNumber.setText(String.valueOf(CCNumberUtil.toInteger(number) + 1));
			MsgCheckableImageView.this.setChecked(true);
		}
	}

	public void setTxtNumber(TextView txtNumber){
		this.txtNumber = txtNumber;
	}
}
