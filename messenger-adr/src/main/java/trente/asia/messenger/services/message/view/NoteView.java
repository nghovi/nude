package trente.asia.messenger.services.message.view;

import android.app.Activity;
import android.content.Context;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.messenger.R;

/**
 * MessageView
 *
 * @author TrungND
 */
public class NoteView extends LinearLayout{

	private Context		mContext;
	public EditText		edtNote;

	public Button		btnEdit;
	public Button		btnSave;
	private boolean		isEditMode	= false;
	private String		noteContent	= "";
	private KeyListener	mDefaultKeyListener;

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

	public void initialization(){
		// lsvMember = (ListView)this.findViewById(R.id.lsv_id_member);
		edtNote = (EditText)this.findViewById(R.id.edt_id_note);
		btnEdit = (Button)this.findViewById(R.id.btn_id_edit);
		btnSave = (Button)this.findViewById(R.id.btn_id_save);

		mDefaultKeyListener = edtNote.getKeyListener();

		setEditMode(isEditMode);
		btnEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(isEditMode){
					edtNote.setText(noteContent);
				}
//				changeMode(!isEditMode);
			}
		});
	}

	// public void updateMemberList(List<UserModel> lstMember){
	// BoardMemberAdapter adapter = new BoardMemberAdapter(mContext, lstMember);
	// lsvMember.setAdapter(adapter);
	// }

	public void changeMode(boolean isEditMode){
		setEditMode(isEditMode);
		this.isEditMode = isEditMode;
	}

	private void setEditMode(boolean isEditMode){
		if(isEditMode){
			noteContent = CCStringUtil.toString(edtNote.getText());
			edtNote.setEnabled(true);
			edtNote.setKeyListener(mDefaultKeyListener);
			btnSave.setEnabled(true);
			btnEdit.setText(mContext.getString(R.string.chiase_common_cancel1));
		}else{
			noteContent = "";
			ChiaseFragment.hideKeyBoard((Activity)getContext());
			btnSave.setEnabled(false);
			edtNote.setEnabled(false);
			btnEdit.setText(mContext.getString(R.string.chiase_common_edit));
		}
	}
}
