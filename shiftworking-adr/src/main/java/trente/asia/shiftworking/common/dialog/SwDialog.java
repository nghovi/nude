package nguyenhoangviet.vpcorp.shiftworking.common.dialog;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import nguyenhoangviet.vpcorp.android.view.ChiaseDialog;
import nguyenhoangviet.vpcorp.android.view.adapter.ChiaseListAdapter;
import nguyenhoangviet.vpcorp.android.view.model.ChiaseListItemModel;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * SwDialog
 *
 * @author TrungND
 */
public class SwDialog extends ChiaseDialog{

	private Context				mContext;

	public SwDialog(Context context){
		super(context);
		this.mContext = context;
	}

	public SwDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public SwDialog setDialogCheckUserList(){
		this.setContentView(R.layout.dialog_check_in);

		return this;
	}
}
