package trente.asia.messenger.services.message.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.RealmList;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.services.message.listener.ItemMsgClickListener;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.messenger.services.message.model.RealmCommentModel;
import trente.asia.messenger.services.message.model.RealmMessageModel;
import trente.asia.messenger.services.message.model.RealmUserModel;
import trente.asia.messenger.services.message.model.WFMStampModel;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by Huy-nq on 7/12/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

	private final WelfareFragment.OnAvatarClickListener	onAvatarClickListener;
	private Context										mContext;
	private List<RealmMessageModel>						mLstMessage;
	public ItemMsgClickListener							itemMsgClickListener;

	private List<String>								lstDate	= new ArrayList<>();
	private List<Integer>								lstKey	= new ArrayList<>();

	public class MessageViewHolder extends RecyclerView.ViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtUserName;
		public TextView		txtMessageDate;
		public TextView		txtContent;
		public ImageView	imgViewContent;

		public TextView		txtLocation;
		public TextView		txtDate;

		public TextView		txtCommentNumber;
		public TextView		txtFileName;
		public LinearLayout	lnrTargetUser;

		public ProgressBar	pgrLoading;
		public TextView		txtNumCheck;
		public LinearLayout	lnrCheck;
		public ImageView	imgIcon;

		public MessageViewHolder(View itemView, int viewType){
			super(itemView);
			if(viewType == R.layout.item_messages_date){
				txtDate = (TextView)itemView.findViewById(R.id.txt_id_date);
			}else{
				if(viewType != R.layout.item_messages_text && viewType != R.layout.item_messages_emotion){
					txtCommentNumber = (TextView)itemView.findViewById(R.id.txt_id_comment_numb);
				}
				if(viewType != R.layout.item_messages_emotion){
					lnrTargetUser = (LinearLayout)itemView.findViewById(R.id.lnr_id_target_user);
				}else{
					imgIcon = (ImageView)itemView.findViewById(R.id.img_id_icon);
				}

				imgAvatar = (ImageView)itemView.findViewById(R.id.img_avatar_msg);
				imgViewContent = (ImageView)itemView.findViewById(R.id.img_contentMsg);
				txtUserName = (TextView)itemView.findViewById(R.id.txt_userName);
				txtMessageDate = (TextView)itemView.findViewById(R.id.txt_message_date);
				txtNumCheck = (TextView)itemView.findViewById(R.id.txt_numb_check);
				lnrCheck = (LinearLayout)itemView.findViewById(R.id.lnr_id_check);

				if(viewType == R.layout.item_messages_location){
					txtLocation = (TextView)itemView.findViewById(R.id.txt_id_location);
				}else if(viewType == R.layout.item_messages_file){
					txtFileName = (TextView)itemView.findViewById(R.id.txt_id_file_name);
				}else if(viewType == R.layout.item_messages_text){
					txtContent = (TextView)itemView.findViewById(R.id.txt_id_content);
				}else if(viewType == R.layout.item_messages_photo || viewType == R.layout.item_messages_movie){
					txtContent = (TextView)itemView.findViewById(R.id.txt_id_content);
					pgrLoading = (ProgressBar)itemView.findViewById(R.id.pgr_id_loading);
				}
			}
		}

		public void bind(final MessageViewHolder holder, final RealmMessageModel item, final ItemMsgClickListener listener){
			if(!WelfareConst.ITEM_TEXT_TYPE_TEXT.equals(item.messageType) && !WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(item.messageType) && !WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(item.messageType)){
				itemView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						listener.onItemMsgClickListener(item);
					}
				});
			}

			itemView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View view){
					listener.onItemMsgLongClickListener(item);
					return true;
				}
			});

			if(!WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(item.messageType) && !WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(item.messageType)){
				// if(item.getCheckCount().compareTo(CCConst.ZERO) > 0){
				// lnrCheck.setOnClickListener(new View.OnClickListener() {
				//
				// @Override
				// public void onClick(View v){
				// if(itemMsgClickListener != null) itemMsgClickListener.onItemCheckClickListener(item);
				// }
				// });
				// }else{
				// lnrCheck.setOnClickListener(null);
				// }
			}
		}
	}

	public MessageAdapter(Context context, List<RealmMessageModel> contentModelList, ItemMsgClickListener itemMsgClickListener, WelfareFragment.OnAvatarClickListener listener){
		super();
		this.mContext = context;
		this.mLstMessage = contentModelList;
		for(RealmMessageModel contentModel : contentModelList){
			lstKey.add(contentModel.key);
		}
		this.itemMsgClickListener = itemMsgClickListener;
		this.onAvatarClickListener = listener;
	}

	@Override
	public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View itemView = LayoutInflater.from(mContext).inflate(viewType, parent, false);
		return new MessageViewHolder(itemView, viewType);
	}

	@Override
	public void onBindViewHolder(MessageViewHolder viewHolder, int position){
		// boolean isEmotion = false;
		final RealmMessageModel contentModel = mLstMessage.get(position);
		if(WelfareConst.ITEM_TEXT_TYPE_DATE.equals(contentModel.messageType)){
			viewHolder.txtDate.setText(contentModel.messageDate);
		}else{
			viewHolder.bind(viewHolder, contentModel, itemMsgClickListener);
			// viewHolder.imgAvatar.setImageDrawable(null);
			// WfPicassoHelper.cancelLoadImage(mContext, viewHolder.imgAvatar);
			if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(contentModel.messageType)){
				viewHolder.txtLocation.setText(contentModel.messageContent);
			}else if(WelfareConst.ITEM_FILE_TYPE_FILE.equals(contentModel.messageType)){
				viewHolder.txtFileName.setText(contentModel.attachment.fileName);
			}else if(WelfareConst.ITEM_FILE_TYPE_PHOTO.equals(contentModel.messageType) || WelfareConst.ITEM_FILE_TYPE_MOVIE.equals(contentModel.messageType)){
				if(contentModel.bitmapModel.bitmap != null){
					viewHolder.imgViewContent.setImageBitmap(contentModel.bitmapModel.bitmap);
				}else{
					viewHolder.imgViewContent.setImageBitmap(null);
					if(contentModel.thumbnailAttachment != null && !CCStringUtil.isEmpty(contentModel.thumbnailAttachment.fileUrl)){
						if(!contentModel.bitmapModel.started){
							String fullUrl = BuildConfig.HOST + contentModel.thumbnailAttachment.fileUrl;
							WfPicassoHelper.loadImageFit(mContext, fullUrl, viewHolder.imgViewContent, viewHolder.pgrLoading, contentModel.bitmapModel);
						}
					}
				}

				// check content text
				if(!CCStringUtil.isEmpty(contentModel.messageContent)){
					viewHolder.txtContent.setVisibility(View.VISIBLE);
					viewHolder.txtContent.setText(contentModel.messageContent);
				}else{
					viewHolder.txtContent.setVisibility(View.GONE);
				}
			}else if(WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(contentModel.messageType)){
				Picasso.with(mContext).load(R.drawable.wf_good).into(viewHolder.imgIcon);
			}else if(WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(contentModel.messageType)){
				WFMStampModel stamp = WFMStampModel.getStamp(contentModel.messageContent);
				if(stamp != null){
					Picasso.with(mContext).load(BuildConfig.HOST + stamp.stampPath).resize(WelfareUtil.dpToPx(80), WelfareUtil.dpToPx(80)).into(viewHolder.imgIcon);
				}
			}else{
				viewHolder.txtContent.setText(contentModel.messageContent);
			}
			Date messageDate = CCDateUtil.makeDateCustom(contentModel.messageDate, WelfareConst.WF_DATE_TIME);
			String messageDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HH_MM, messageDate);
			viewHolder.txtUserName.setText(contentModel.messageSender.userName);
			viewHolder.txtMessageDate.setText(messageDateFormat);
			viewHolder.imgAvatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					onAvatarClickListener.OnAvatarClick(contentModel.messageSender.userName, contentModel.messageSender.avatarPath);
				}
			});

			if(!WelfareConst.ITEM_TEXT_TYPE_TEXT.equals(contentModel.messageType) && !WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(contentModel.messageType) && !WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(contentModel.messageType)){
				viewHolder.txtCommentNumber.setText(String.valueOf(WelfareUtil.size(contentModel.comments)));
			}

			if(viewHolder.lnrTargetUser != null){
				viewHolder.lnrTargetUser.removeAllViews();
			}
			if(!CCCollectionUtil.isEmpty(contentModel.targets)){
				viewHolder.lnrTargetUser.setVisibility(View.VISIBLE);
				LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

				for(int i = 0; i < contentModel.targets.size() && i < 5; i++){
					RealmUserModel userModel = contentModel.targets.get(i);
					View toUserView = mInflater.inflate(R.layout.item_to_user_list, null);
					ImageView imgToUserAvatar = (ImageView)toUserView.findViewById(R.id.img_id_to_user_avatar);
					if(!CCStringUtil.isEmpty(userModel.avatarPath)){
						WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + userModel.avatarPath, imgToUserAvatar, null);
					}
					viewHolder.lnrTargetUser.addView(toUserView);
				}
			}

			if(!CCCollectionUtil.isEmpty(contentModel.checks)){
				if(!WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(contentModel.messageType) && !WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(contentModel.messageType)){
					viewHolder.txtNumCheck.setText(String.valueOf(contentModel.checks.size()));
				}
			}
		}
	}

	@Override
	public int getItemCount(){
		return mLstMessage.size();
	}

	@Override
	public void onViewDetachedFromWindow(MessageViewHolder viewHolder){
		if(viewHolder.imgAvatar != null){
			viewHolder.imgAvatar.setImageDrawable(null);
			WfPicassoHelper.cancelLoadImage(mContext, viewHolder.imgAvatar);
		}
	}

	@Override
	public void onViewAttachedToWindow(MessageViewHolder viewHolder){
		if(viewHolder.imgAvatar != null){
			int position = viewHolder.getAdapterPosition();
			RealmMessageModel contentModel = mLstMessage.get(position);
			if(!CCStringUtil.isEmpty(contentModel.messageSender.avatarPath)){
				WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + contentModel.messageSender.avatarPath, viewHolder.imgAvatar, null);
			}else{
				viewHolder.imgAvatar.setImageResource(R.drawable.wf_profile);
			}
		}
	}

	@Override
	public int getItemViewType(int position){
		RealmMessageModel contentModel = mLstMessage.get(position);
		if(WelfareConst.ITEM_TEXT_TYPE_DATE.equals(contentModel.messageType)){
			return R.layout.item_messages_date;
		}else if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(contentModel.messageType)){
			return R.layout.item_messages_location;
		}else if(WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(contentModel.messageType) || WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(contentModel.messageType)){
			return R.layout.item_messages_emotion;
		}else if(WelfareConst.ITEM_FILE_TYPE_FILE.equals(contentModel.messageType)){
			return R.layout.item_messages_file;
		}else if(WelfareConst.ITEM_FILE_TYPE_PHOTO.equals(contentModel.messageType)){
			return R.layout.item_messages_photo;
		}else if(WelfareConst.ITEM_FILE_TYPE_MOVIE.equals(contentModel.messageType)){
			return R.layout.item_messages_movie;
		}else{
			// message text
			return R.layout.item_messages_text;
		}
	}

	/**
	 * add more message to adapter
	 *
	 * @param message received message
	 */
	public void addMessage(RealmMessageModel message){
		List<RealmMessageModel> lstMessage = appendMessageWithDateLabel(message);
		this.mLstMessage.addAll(lstMessage);
		notifyItemRangeInserted(mLstMessage.size() - lstMessage.size(), mLstMessage.size());
	}

	/**
	 * add more message to top adapter
	 *
	 * @param messages received message
	 */
	public void addMessage2Top(List<RealmMessageModel> messages){
		// notifyItemRangeChanged(0, 1);
		int oldSize = this.mLstMessage.size();
		for(RealmMessageModel messageModel : messages){
			List<RealmMessageModel> lstAppend = appendMessageWithDateLabel(messageModel);
			if(lstAppend.size() > 1){
				this.mLstMessage.addAll(0, lstAppend);
				// notifyItemRangeInserted(0, lstAppend.size() - 1);
			}else{
				this.mLstMessage.addAll(1, lstAppend);
				// notifyItemInserted(1);
				// notifyItemChanged(0);
			}
		}
		notifyItemRangeInserted(0, this.mLstMessage.size() - oldSize);
	}

	/**
	 * add messages to adapter
	 *
	 * @param lstMessage messages to add
	 */
	public void addMessages(List<RealmMessageModel> lstMessage){
		List<RealmMessageModel> lstAppend = appendDateLabel(lstMessage);
		int size = this.mLstMessage.size();
		this.mLstMessage.addAll(lstAppend);
		notifyItemRangeInserted(size, this.mLstMessage.size());
	}

	private List<RealmMessageModel> appendDateLabel(List<RealmMessageModel> lstMessage){
		List<RealmMessageModel> lstAppend = new ArrayList<>();
		for(RealmMessageModel contentModel : lstMessage){
			List<RealmMessageModel> list = appendMessageWithDateLabel(contentModel);
			if(!CCCollectionUtil.isEmpty(list)){
				lstAppend.addAll(list);
			}
		}
		return lstAppend;
	}

	private List<RealmMessageModel> appendMessageWithDateLabel(RealmMessageModel messageModel){
		List<RealmMessageModel> lstMessage = new ArrayList<>();
		if(!lstKey.contains(messageModel.key)){
			Date messageDate = CCDateUtil.makeDateCustom(messageModel.messageDate, WelfareConst.WF_DATE_TIME);
			if(CCCollectionUtil.isEmpty(this.lstDate) || !this.lstDate.contains(CCFormatUtil.formatDateCustom(CCFormatUtil.SDF_DATE_SLS, messageDate))){
				this.lstDate.add(CCFormatUtil.formatDateCustom(CCFormatUtil.SDF_DATE_SLS, messageDate));
				RealmMessageModel dateModel = new RealmMessageModel(CCFormatUtil.formatDateCustom(CCFormatUtil.SDF_DATE_SLS, messageDate));
				lstMessage.add(dateModel);
			}
			lstMessage.add(messageModel);
			lstKey.add(messageModel.key);
		}
		return lstMessage;
	}

	public void clearAll(){
		// int itemCount = this.getItemCount();
		this.mLstMessage.clear();
		this.lstDate.clear();
		this.lstKey.clear();
		this.notifyDataSetChanged();
	}

	public void addComment(int messageId){
		for(int i = 0; i < mLstMessage.size(); i++){
			RealmMessageModel messageModel = mLstMessage.get(i);
			if(messageModel.key == messageId){
				this.notifyItemChanged(i);
				break;
			}
		}
	}

	/**
	 * update message information
	 *
	 * @param lstMessage received message
	 */
	public void updateMessage(List<RealmMessageModel> lstMessage){
		int index = 0;
		int minKey = CCNumberUtil.toInteger(lstMessage.get(0).key);
		int maxKey = CCNumberUtil.toInteger(lstMessage.get(lstMessage.size() - 1).key);
		List<RealmMessageModel> lstClone = new ArrayList<>();
		for(RealmMessageModel messageModel : mLstMessage){
			lstClone.add(messageModel);
		}

		for(RealmMessageModel messageModel : lstClone){
			if(!WelfareConst.ITEM_TEXT_TYPE_DATE.equals(messageModel.messageType)){
				findMessage4Update(lstMessage, messageModel, index, minKey, maxKey);
			}

			index++;
		}
	}

	/**
	 * find message for key
	 *
	 * @param messageContentModel received message
	 */
	private void findMessage4Update(List<RealmMessageModel> lstMessage, RealmMessageModel messageContentModel, int index, int minKey, int maxKey){
		boolean isDeleted = true;
		for(RealmMessageModel message : lstMessage){
			int messageKey = CCNumberUtil.toInteger(messageContentModel.key);
			if(messageKey > maxKey || messageKey < minKey){
				isDeleted = false;
				break;
			}
			if(messageContentModel.key == message.key){
				isDeleted = false;
				if(isChanged(messageContentModel, message)){
					messageContentModel.messageContent = message.messageContent;
					messageContentModel.checks = message.checks;
					messageContentModel.comments = message.comments;
					messageContentModel.isChanged = true;
					this.notifyItemChanged(index);
					break;
				}
			}
		}

		// check message is deleted
		if(isDeleted){
			deleteMessage4Index(index);
		}
	}

	/**
	 * message is changed
	 */
	private boolean isChanged(RealmMessageModel message1, RealmMessageModel message2){
		if(WelfareUtil.size(message1.comments) != WelfareUtil.size(message2.comments) || WelfareUtil.size(message1.checks) != WelfareUtil.size(message2.checks)){
			return true;
		}else if(WelfareConst.ITEM_TEXT_TYPE_TEXT.equals(message1.messageType) && !message1.messageContent.equals(message2.messageContent)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * delete message to adapter
	 *
	 * @param messageId message id
	 */
	public void deleteMessage(int messageId){
		int index = 0;
		for(RealmMessageModel message : mLstMessage){
			if(messageId == message.key){
				deleteMessage4Index(index);
				break;
			}
			index++;
		}
	}

	public void deleteMessage4Index(int index){
		this.mLstMessage.remove(index);
		this.notifyItemRemoved(index);
		// check last message is date
		RealmMessageModel contentModel = this.mLstMessage.get(index - 1);
		if(WelfareConst.ITEM_TEXT_TYPE_DATE.equals(contentModel.messageType)){
			// check we need delete date label
			boolean isDelete = WelfareUtil.size(this.mLstMessage) == (index) || WelfareConst.ITEM_TEXT_TYPE_DATE.equals(this.mLstMessage.get(index).messageType);
			if(isDelete){
				this.lstDate.remove(contentModel.messageDate);
				this.mLstMessage.remove(index - 1);
				this.notifyItemRemoved(index - 1);
			}
		}
	}
}
