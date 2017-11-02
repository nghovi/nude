package nguyenhoangviet.vpcorp.thankscard.fragments.dialogs;

import android.view.View;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.thankscard.R;

/**
 * Created by viet on 3/16/2016.
 */
public class AvatarSelectionDialog extends TCDialog implements View.OnClickListener{

	public static final int	USE_CAMERA	= 1;
	public static final int	USE_GALLERY	= 2;
	public static final int	USE_DELETE	= 3;

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.txt_dialog_avatar_handlers_camera:
			listener.onAvatarHandlerSelected(USE_CAMERA);
			break;
		case R.id.txt_dialog_avatar_handlers_gallery:
			listener.onAvatarHandlerSelected(USE_GALLERY);
			break;
		default:
			break;
		}
		this.dismiss();
	}

	public interface OnSelectHandlerListener{

		public void onAvatarHandlerSelected(int handlerId);
	}

	OnSelectHandlerListener listener;

	public void setHandlerSelectedListener(OnSelectHandlerListener listener){
		this.listener = listener;
	}

	@Override
	public int getDialogLayoutId(){
		return R.layout.dialog_avatar_handlers;
	}

	@Override
    public void buildDialogLayout(View rootView){
		TextView txtCamera = (TextView)rootView.findViewById(R.id.txt_dialog_avatar_handlers_camera);
		txtCamera.setOnClickListener(this);
		TextView txtGallery = (TextView)rootView.findViewById(R.id.txt_dialog_avatar_handlers_gallery);
		txtGallery.setOnClickListener(this);
	}

}
