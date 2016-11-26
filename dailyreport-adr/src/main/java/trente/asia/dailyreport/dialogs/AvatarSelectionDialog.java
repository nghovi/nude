package trente.asia.dailyreport.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import trente.asia.dailyreport.R;

/**
 * Created by viet on 3/16/2016.
 */
public class AvatarSelectionDialog extends DRDialog implements View.OnClickListener{

	public static final int	USE_CAMERA	= 1;
	public static final int	USE_GALLERY	= 2;
	public static final int	USE_DELETE	= 3;

	public AvatarSelectionDialog(Context context){
		super(context);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.txt_dialog_avatar_handlers_camera:
			listener.onAvatarHandlerSelected(USE_CAMERA);
			break;
		case R.id.txt_dialog_avatar_handlers_gallery:
			listener.onAvatarHandlerSelected(USE_GALLERY);
			break;
		// case R.id.txt_dialog_avatar_handlers_delete:
		// listener.onAvatarHandlerSelected(USE_DELETE);
		// break;
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

	public void buildDialogLayout(){
		this.setContentView(R.layout.dialog_avatar_handlers);
		TextView txtCamera = (TextView)this.findViewById(R.id.txt_dialog_avatar_handlers_camera);
		txtCamera.setOnClickListener(this);
		TextView txtGallery = (TextView)this.findViewById(R.id.txt_dialog_avatar_handlers_gallery);
		txtGallery.setOnClickListener(this);
		// TextView txtDelete = (TextView) this.findViewById(R.id.txt_dialog_avatar_handlers_delete);
		// txtDelete.setOnClickListener(this);
	}

}
