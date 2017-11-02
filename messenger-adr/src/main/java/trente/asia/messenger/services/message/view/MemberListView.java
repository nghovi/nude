package nguyenhoangviet.vpcorp.messenger.services.message.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmUserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * MessageView
 *
 * @author TrungND
 */
public class MemberListView extends LinearLayout{

	private Context	mContext;
	public ListView	lsvMember;

	public MemberListView(Context context){
		super(context);
		this.mContext = context;
	}

	public MemberListView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public MemberListView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void initialization(){
		lsvMember = (ListView)this.findViewById(R.id.lsv_id_member);
	}

	public void updateMemberList(List<RealmUserModel> lstMember){
		BoardMemberAdapter adapter = new BoardMemberAdapter(mContext, lstMember);
		lsvMember.setAdapter(adapter);
	}
}
