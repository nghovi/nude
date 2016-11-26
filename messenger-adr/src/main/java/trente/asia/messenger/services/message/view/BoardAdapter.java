package trente.asia.messenger.services.message.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * BoardAdapter.
 *
 * @author TrungND
 */
public class BoardAdapter extends ArrayAdapter<BoardModel>{

	private List<BoardModel>	boardList;
	private Context				mContext;

	public class BoardViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtBoardType;
		public TextView		txtBoardName;
		public TextView		txtBoardUnread;

		public BoardViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtBoardType = (TextView)view.findViewById(R.id.txt_id_type);
			txtBoardName = (TextView)view.findViewById(R.id.txt_id_name);
			txtBoardUnread = (TextView)view.findViewById(R.id.txt_board_notice);

		}
	}

	public BoardAdapter(Context context, List<BoardModel> boardList){
		super(context, R.layout.item_board_list, boardList);
		this.mContext = context;
		this.boardList = boardList;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		BoardModel model = this.boardList.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_board_list, null);
		BoardViewHolder holder = new BoardViewHolder(convertView);

		if(WelfareConst.BOARD_TYPE_DEPT.equals(model.boardType)){
			holder.txtBoardType.setText(mContext.getString(R.string.wf_common_board_type_dept, String.valueOf(WelfareUtil.size(model.memberList))));
		}else if(WelfareConst.BOARD_TYPE_GROUP.equals(model.boardType)){
			holder.txtBoardType.setText(mContext.getString(R.string.wf_common_board_type_group, String.valueOf(WelfareUtil.size(model.memberList))));
		}else{
			holder.txtBoardType.setText(mContext.getString(R.string.wf_common_board_type_private));
		}
		holder.txtBoardName.setText(model.boardName);

		if(!CCStringUtil.isEmpty(model.avatarPath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.avatarPath, holder.imgAvatar, null);
		}else{
			// WfPicassoHelper.loadImage(mContext, "https://upload.wikimedia.org/wikipedia/en/8/86/Avatar_Aang.png", holder.imgAvatar, null);
		}

		if(!CCStringUtil.isEmpty(model.boardUnread) && !WelfareConst.NONE.equals(model.boardUnread)){
			holder.txtBoardUnread.setText(model.boardUnread);
		}else{
			holder.txtBoardUnread.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void add(BoardModel item, int position){
		this.boardList.add(position, item);
		this.notifyDataSetChanged();
	}

	public void addUnreadMessage(MessageContentModel messageModel){
		for(BoardModel boardModel : boardList){
			if(boardModel.key.equals(messageModel.boardId)){
				boardModel.boardUnread = CCStringUtil.toString(CCNumberUtil.checkNull(boardModel.boardUnread) + 1);
				this.notifyDataSetChanged();
				break;
			}
		}
	}
}
