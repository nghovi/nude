package nguyenhoangviet.vpcorp.messenger.services.message.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import nguyenhoangviet.vpcorp.messenger.databinding.BoardPagerNoteBinding;

/**
 * MessageView
 *
 * @author TrungND
 */
public class NoteView extends LinearLayout{

	private Context					mContext;
	private BoardPagerNoteBinding	binding;
	private NoteAdapter noteAdapter;

	public NoteView(Context context){
		super(context);
		this.mContext = context;
	}

	public NoteView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public NoteView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void initiate(NoteAdapter.OnNoteAdapterListener listener){
		binding = DataBindingUtil.bind(this);
		noteAdapter = new NoteAdapter();
		noteAdapter.setCallback(listener);
		binding.rlvNote.setAdapter(noteAdapter);
		binding.rlvNote.setLayoutManager(new LinearLayoutManager(mContext));
	}

}
