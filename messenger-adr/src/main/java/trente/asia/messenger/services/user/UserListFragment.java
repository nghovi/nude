package trente.asia.messenger.services.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.user.listener.OnAddUserListener;
import trente.asia.messenger.services.user.view.UserListAdapter;
import trente.asia.welfare.adr.dialog.WfProfileDialog;
import trente.asia.welfare.adr.models.UserModel;

/**
 * UserListFragment
 *
 * @author TrungND
 */
public class UserListFragment extends AbstractMsgFragment implements OnAddUserListener{

	private ListView		mLsvUser;
	private EditText		mEdtSearch;
	private UserListAdapter	mAdapter;
	private WfProfileDialog	mDlgProfile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_user_list, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.msg_contract_list), null);

		mLsvUser = (ListView)getView().findViewById(R.id.lsv_id_user);
		mEdtSearch = (EditText)getView().findViewById(R.id.edt_id_search);

		mEdtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				// When user changed the Text
				mAdapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void afterTextChanged(Editable s){
			}
		});
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);
	}

	@Override
	protected void initData(){
		loadUserList();
	}

	private void loadUserList(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(MsConst.API_MESSAGE_CONTACT_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(MsConst.API_MESSAGE_CONTACT_LIST.equals(url)){
			List<UserModel> lstUser = null;
			try{
				lstUser = LoganSquare.parseList(response.optString("userList"), UserModel.class);
				OnAvatarClickListener listener = new OnAvatarClickListener() {

					@Override
					public void OnAvatarClick(String userName, String avatarPath){
						mDlgProfile.show(BuildConfig.HOST, userName, avatarPath);
					}
				};
				if(!CCCollectionUtil.isEmpty(lstUser)){
					mAdapter = new UserListAdapter(activity, lstUser, UserListFragment.this, listener);
					mLsvUser.setAdapter(mAdapter);
				}else{
					mAdapter = new UserListAdapter(activity, new ArrayList<UserModel>(), UserListFragment.this, listener);
					mLsvUser.setAdapter(mAdapter);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	public void onAddUsertListener(UserModel userModel){
		addContact(userModel);
	}

	private void addContact(UserModel userModel){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", userModel.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(MsConst.API_MESSAGE_CONTACT_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(MsConst.API_MESSAGE_CONTACT_UPDATE.equals(url)){

			Intent intent = activity.getIntent();
			intent.putExtra("detail", response.optString("detail"));
			activity.setResult(Activity.RESULT_OK, intent);
			activity.finish();

			// if(addedContactListener != null){
			// BoardModel boardModel = CCJsonUtil.convertToModel(response.optString("detail"), BoardModel.class);
			// addedContactListener.onAddedContactListener(boardModel);
			// }
			// super.onBackListener();
		}else{
			super.successUpdate(response, url);
		}
	}

	@Override
	protected void onClickBackBtn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		mLsvUser = null;
		mEdtSearch = null;
		mAdapter = null;
	}
}
