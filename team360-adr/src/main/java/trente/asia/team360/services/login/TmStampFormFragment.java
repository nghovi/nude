package trente.asia.team360.services.login;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.team360.BuildConfig;
import trente.asia.team360.R;
import trente.asia.team360.common.fragments.AbstractTmFragment;
import trente.asia.team360.services.model.StampModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

public class TmStampFormFragment extends AbstractTmFragment {

    private GridView gridView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_stamp_form, container, false);
        }
        return mRootView;
    }


    @Override
    public void initView() {
        super.initView();

        UserModel userModel = prefAccUtil.getUserPref();
        super.initHeader(R.drawable.wf_back_white, userModel.getUserName(), null);

        gridView = (GridView) getView().findViewById(R.id.gridview);


        Bundle bundle = getArguments();
        String userName = bundle.getString("USER_NAME");
        String avatarPath = bundle.getString("AVATAR_PATH");

        ImageView ivAvatar = (ImageView) getView().findViewById(R.id.img_id_avatar);
        WfPicassoHelper.loadImageWithDefaultIcon(getContext(), BuildConfig.HOST, ivAvatar, avatarPath, R.drawable.wf_profile);

        TextView tvName = (TextView) getView().findViewById(R.id.tv_id_user_name);
        tvName.setText(userName);


    }


    @Override
    protected void initData() {
        loadStampList();
    }

    private void loadStampList() {
        JSONObject jsonObject = new JSONObject();
        try {
            Date date = new Date();
            jsonObject.put("targetDate", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(WfUrlConst.API_TEAM_STAMP_LIST, jsonObject, true);
    }


    @Override
    protected void successLoad(JSONObject response, String url) {

        switch (url) {
            case WfUrlConst.API_TEAM_STAMP_LIST:

                List<StampModel> userModels = CCJsonUtil.convertToModelList(response.optString("stamps"), StampModel.class);

                TmStampAdapter adapter = new TmStampAdapter(getContext());
                adapter.setStamps(userModels);
                gridView.setAdapter(adapter);
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
