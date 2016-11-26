package trente.asia.messenger.commons.dialog;

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

import trente.asia.android.view.ChiaseDialog;
import trente.asia.android.view.adapter.ChiaseListAdapter;
import trente.asia.android.view.model.ChiaseListItemModel;
import trente.asia.messenger.R;
import trente.asia.messenger.services.message.listener.OnActionClickListener;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.messenger.services.message.view.CheckUserListAdapter;
import trente.asia.welfare.adr.models.UserModel;

/**
 * MsChiaseDialog
 *
 * @author TrungND
 */
public class MsChiaseDialog extends ChiaseDialog{

	private Context				mContext;

	private MessageContentModel	message;

	public MsChiaseDialog(Context context){
		super(context);
		this.mContext = context;
	}

	public MsChiaseDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public MsChiaseDialog setDialogCheckUserList(){
		this.setContentView(R.layout.dialog_check_user_list);

		ImageView imgClose = (ImageView)this.findViewById(R.id.img_id_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				MsChiaseDialog.this.dismiss();
			}
		});

		return this;
	}

	public void updateCheckUserList(List<UserModel> lstCheck){
		ListView lsvUser = (ListView)this.findViewById(R.id.lsv_id_user);
		CheckUserListAdapter adapter = new CheckUserListAdapter(this.mContext, lstCheck);
		lsvUser.setAdapter(adapter);
	}

	public MsChiaseDialog setDialogMovieRun(File file){
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dialog_run_movie);

		VideoView videoView = (VideoView)this.findViewById(R.id.id_video);

		// url don't run
		// videoView.setVideoURI(Uri.parse("http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4"));
		// videoView.setVideoURI(Uri.parse(BuildConfig.HOST + url));

		MediaController mc = new MediaController(mContext);
		videoView.setMediaController(mc);
		videoView.setVideoPath(file.getAbsolutePath());
		// video finish listener
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp){
				System.out.println("Finished");
			}
		});

		videoView.start();
		videoView.setMediaController(new MediaController(getContext()));

		return this;
	}

	public MsChiaseDialog setDialogAction(final OnActionClickListener listener){
		this.setContentView(R.layout.dialog_message_action_list);

		ImageView imgClose = (ImageView)this.findViewById(R.id.img_id_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				MsChiaseDialog.this.dismiss();
			}
		});

		ListView lsvAction = (ListView)this.findViewById(R.id.lsv_id_action);
		lsvAction.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				ChiaseListItemModel itemModel = (ChiaseListItemModel)parent.getItemAtPosition(position);
				if(message != null){
					listener.onActionClickListener(itemModel, message);
				}
				MsChiaseDialog.this.dismiss();
			}
		});

		return this;
	}

	public void updateDialogAction(List<ChiaseListItemModel> itemList){

		ListView lsvAction = (ListView)this.findViewById(R.id.lsv_id_action);
		ChiaseListAdapter adapter = new ChiaseListAdapter(mContext, itemList);
		lsvAction.setAdapter(adapter);
		this.show();
	}

	public MessageContentModel getMessage(){
		return message;
	}

	public void setMessage(MessageContentModel message){
		this.message = message;
	}
}
