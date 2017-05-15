package trente.asia.team360.services.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.team360.BR;
import trente.asia.team360.R;
import trente.asia.team360.common.fragments.AbstractTmFragment;
import trente.asia.team360.databinding.FragmentMemberViewBinding;
import trente.asia.team360.services.model.TestHandlers;
import trente.asia.team360.services.model.TestModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

import static android.R.attr.data;

public class TmMemberViewFragment extends AbstractTmFragment {

    private GridView gridView;

    private FragmentMemberViewBinding binding;

    private TestModel mTestModel = new TestModel();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_member_view, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMemberViewBinding.bind(view);
        binding.setTest(mTestModel);
        binding.setHandlers(new TestHandlers(mTestModel));
    }


    @Override
    public void initView() {
        super.initView();

        UserModel userModel = prefAccUtil.getUserPref();
        super.initHeader(null, userModel.getUserName(), R.drawable.wf_setting_white);

        gridView = (GridView) getView().findViewById(R.id.gridview);

    }


    @Override
    protected void initData() {
        loadMemberList();
    }

    private void loadMemberList() {
        JSONObject jsonObject = new JSONObject();
        try {
            Date date = new Date();
            jsonObject.put("targetDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(WfUrlConst.WF_ACC_INFO_MEMBER, jsonObject, true);
    }


    @Override
    protected void successLoad(JSONObject response, String url) {

        switch (url) {
            case WfUrlConst.WF_ACC_INFO_MEMBER:

                final List<UserModel> userModels = CCJsonUtil.convertToModelList(response.optString("members"), UserModel.class);

                TmMemberAdapter adapter = new TmMemberAdapter(getContext());
                adapter.setUsers(userModels);
                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        UserModel target = userModels.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putString("USER_NAME", target.getUserName());
                        bundle.putString("AVATAR_PATH", target.getAvatarPath());

                        TmStampFormFragment fgmt = new TmStampFormFragment();
                        fgmt.setArguments(bundle);

                        gotoFragment(fgmt);
                    }
                });

                break;
            default:
        }
        super.successLoad(response, url);
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


    @Override
    public void onDestroy() {
        super.onDestroy();

        gridView = null;
    }

}
