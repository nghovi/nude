package trente.asia.team360.services.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import trente.asia.team360.R;
import trente.asia.team360.common.fragments.AbstractTmFragment;
import trente.asia.team360.databinding.FragmentMemberViewBinding;
import trente.asia.team360.services.entity.UserEntity;
import trente.asia.team360.services.login.TmStampFormFragment;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

public class TmMemberViewFragment extends AbstractTmFragment {


    private FragmentMemberViewBinding binding;

    private RealmResults<UserEntity> bindingUsers;

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_member_view, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMemberViewBinding.bind(view);
        binding.setHandlers(new Handlers());
    }

    @Override
    public void initView() {
        super.initView();

        UserModel userModel = prefAccUtil.getUserPref();
        super.initHeader(null, userModel.getUserName(), null);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_common_footer_member;
    }

    @Override
    protected void initData() {

        this.bindingUsers = loadMemberListByRealM();

        if (!CCCollectionUtil.isEmpty(this.bindingUsers)) {
            createUserView();
        }

        loadMemberListByApi();
    }

    private RealmResults<UserEntity> loadMemberListByRealM() {

        List<UserModel> results = new ArrayList<>();
        RealmQuery<UserEntity> query = realm.where(UserEntity.class);

        // Execute the query:
        RealmResults<UserEntity> result1 = query.findAll();
//        for (UserEntity entity : result1) {
//
//            UserModel user = new UserModel();
//            user.userAccount = entity.getAvatarPath();
//            user.userName = entity.getUserName();
//            user.avatarPath = entity.getAvatarPath();
//            results.add(user);
//        }

        Toast.makeText(getActivity(), "done select database", Toast.LENGTH_LONG).show();
        //return results;
        return result1;

    }

    private void loadMemberListByApi() {


        Toast.makeText(getActivity(), "start call api", Toast.LENGTH_LONG).show();

        // TODO Tak confirm structure
        // We have to use async style to get data from api.
        // So I move logic to async class
        TmMemberApiAsyncTask get = new TmMemberApiAsyncTask(getContext(), this.bindingUsers);
        get.execute();

//        JSONObject jsonObject = new JSONObject();
//        try {
//            Date date = new Date();
//            jsonObject.put("targetDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        requestLoad(WfUrlConst.WF_ACC_INFO_MEMBER, jsonObject, true);

    }


    @Override
    protected void successLoad(JSONObject response, String url) {

        switch (url) {
            case WfUrlConst.WF_ACC_INFO_MEMBER:

                // TODO Tak confirm structure -> dont' use request load because of async
                // We have to use async style to get data from api.
                // So I move logic to async class

//                List<UserModel> results = new ArrayList<>();
//                RealmQuery<UserEntity> query = realm.where(UserEntity.class);
//                Map<String, UserEntity> exists = new LinkedHashMap<>();
//                for (UserEntity entity : query.findAll()) {
//                    exists.put(entity.getUserAccount(), entity);
//                }
//
//                final List<UserModel> userModels = CCJsonUtil.convertToModelList(response.optString("members"), UserModel.class);
//
//                realm.beginTransaction();
//                for (UserModel user : userModels) {
//
//                    UserEntity result = exists.get(user.userAccount);
//
//                    if (result == null) { // insert
//                        result = realm.createObject(UserEntity.class);
//                    }
//                    result.setUserAccount(user.userAccount);
//                    result.setUserName(user.userName);
//                    result.setAvatarPath(user.avatarPath);
//
//                }
//                realm.commitTransaction();
//
//                createUserView(userModels);

                break;
            default:
        }
        super.successLoad(response, url);
    }


    private void createUserView() {

        TmMemberAdapter adapter = new TmMemberAdapter(getContext(), this.bindingUsers);
        binding.gridview.setAdapter(adapter);

        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                UserModel target = bindingUsers.get(position);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("USER_NAME", target.getUserName());
//                bundle.putString("AVATAR_PATH", target.getAvatarPath());

                TmStampFormFragment fgmt = new TmStampFormFragment();
//                fgmt.setArguments(bundle);

                gotoFragment(fgmt);
            }
        });

    }

//	@Override
//	public void onClick(View v){
//		switch(v.getId()){
//		case R.id.img_id_header_right_icon:
//			OfferFragment fragment = new OfferFragment();
//			fragment.setActiveProject(activeProject);
//			gotoFragment(fragment);
//			break;
//		case R.id.btn_id_check_in:
//			getLocation();
//			break;
//		default:
//			break;
//		}
//	}

    public class Handlers {
        public void onClickRefresh(View view) {
            Toast.makeText(getActivity(), "click refresh button", Toast.LENGTH_LONG).show();
            loadMemberListByApi();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //binding.gridview = null;
    }

}