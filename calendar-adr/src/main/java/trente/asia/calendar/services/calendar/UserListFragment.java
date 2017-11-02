package nguyenhoangviet.vpcorp.calendar.services.calendar;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.defines.ClConst;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.AbstractClFragment;
import nguyenhoangviet.vpcorp.calendar.services.user.UserListAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * UserListFragment
 *
 * @author VietNH
 */
public class UserListFragment extends AbstractClFragment{

	private List<UserModel>	users;
	UserListAdapter			adapter;
	private ListView		lstView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_user_list, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		loadFilterData();
	}

	private void loadFilterData(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(ClConst.API_FILTER, jsonObject, true);
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.cl_join_user_dialog_title), null);
		lstView = (ListView)getView().findViewById(R.id.lst_view_user);
		adapter = new UserListAdapter(activity, users);
		lstView.setAdapter(adapter);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}

	public void setUsers(List<UserModel> users){
		this.users = users;
	}
}
