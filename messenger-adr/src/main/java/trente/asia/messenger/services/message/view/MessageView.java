package trente.asia.messenger.services.message.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import trente.asia.messenger.R;
import trente.asia.welfare.adr.utils.AnimUtils;
import trente.asia.welfare.adr.view.MsgMultiAutoCompleteTextView;

/**
 * MessageView
 *
 * @author TrungND
 */
public class MessageView extends LinearLayout{

	private Context						mContext;
	public ImageButton					imgSend;
    public LinearLayout					lnrSend;

	public ImageButton					imgLike;
    public LinearLayout					lnrLike;
	public MsgMultiAutoCompleteTextView	edtMessage;

	public MsgRecyclerView				revMessage;

	protected TextWatcher				etMessageTextWatcher	= new TextWatcher() {

																	@Override
																	public void beforeTextChanged(CharSequence s, int start, int count, int after){
																	}

																	@Override
																	public void onTextChanged(CharSequence s, int start, int before, int count){
																	}

																	@Override
																	public void afterTextChanged(Editable s){
																		if(s.length() == 0){
																			animateSendButton(false);
																			animateLikeButton(true);
																		}else{
																			animateSendButton(true);
																		}
																		// sendTypingType(s.length());
																	}
																};

	public enum ButtonType{
		MENU, SEND, MENU_OPENED, IN_ANIMATION;
	}

	public enum LikeButtonType{
		LIKE, EDIT;
	}

	public ButtonType		buttonType		= ButtonType.MENU;
	public LikeButtonType	likeButtonType	= LikeButtonType.LIKE;

	public MessageView(Context context){
		super(context);
		this.mContext = context;
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// params.weight = 1;
		// this.setLayoutParams(params);
		// this.setOrientation(VERTICAL);

		// initialization();
	}

	public MessageView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
		// initialization();
	}

	public MessageView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
		// initialization();
	}

	public void initialization(){
		// LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View view = inflater.inflate(R.layout.board_pager_message, null);
		// this.addView(view);

		imgSend = (ImageButton)this.findViewById(R.id.btn_id_send);
        lnrSend = (LinearLayout)this.findViewById(R.id.lnr_id_send);
		imgLike = (ImageButton)this.findViewById(R.id.img_id_like);
        lnrLike = (LinearLayout) this.findViewById(R.id.lnr_id_like);

		edtMessage = (MsgMultiAutoCompleteTextView)this.findViewById(R.id.edt_id_message);
		edtMessage.addTextChangedListener(etMessageTextWatcher);

		revMessage = (MsgRecyclerView)this.findViewById(R.id.rev_id_message);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
		// mLayoutManager.setStackFromEnd(true);
		revMessage.setLayoutManager(mLayoutManager);
		revMessage.setItemAnimator(new DefaultItemAnimator());
	}

	private void animateSendButton(final boolean toSend){
		if(toSend && buttonType == ButtonType.SEND){
			return;
		}
		if(toSend){
			buttonType = ButtonType.SEND;
		}else{
			buttonType = ButtonType.MENU;
		}
		AnimUtils.fade(imgSend, 1, 0, 100, new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation){
				if(toSend){
					imgSend.setImageResource(R.drawable.wf_send);
				}else{
					imgSend.setImageResource(R.drawable.wf_attachment);
				}
				AnimUtils.fade(imgSend, 0, 1, 100, null);
			}
		});
	}

	public void animateLikeButton(final boolean toLike){
		if(toLike && likeButtonType == LikeButtonType.LIKE){
			return;
		}
		if(toLike){
			likeButtonType = LikeButtonType.LIKE;
		}else{
			likeButtonType = LikeButtonType.EDIT;
		}
		AnimUtils.fade(imgLike, 1, 0, 100, new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation){
				if(toLike){
					imgLike.setImageResource(R.drawable.wf_good);
				}else{
					imgLike.setImageResource(R.drawable.wf_close_black);
				}
				AnimUtils.fade(imgLike, 0, 1, 100, null);
			}
		});
	}
}
