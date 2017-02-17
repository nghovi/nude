package trente.asia.calendar.commons.dialogs;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.adapter.FilterUserListAdapter;
import trente.asia.welfare.adr.models.UserModel;

/**
 * QkChiaseDialog
 *
 * @author TrungND
 */
public class ClFilterUserListDialog extends ChiaseDialog{

	private Context					mContext;

	private ListView				mLsvUser;
	private FilterUserListAdapter	mAdapter;
	private List<UserModel>			mLstUser;

	public interface OnItemClicked{

		public void onClicked(String selectedKey, boolean isSelected);
	}

	public ClFilterUserListDialog(Context context, List<UserModel> lstUser){
		super(context);
		this.setContentView(R.layout.dialog_common_filter_user);
		this.mContext = context;
		this.mLstUser = lstUser;

		mLsvUser = (ListView)this.findViewById(R.id.lsv_id_user);
		mAdapter = new FilterUserListAdapter(mContext, mLstUser);
		mLsvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			}
		});
		mLsvUser.setAdapter(mAdapter);
	}
}
