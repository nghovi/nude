package trente.asia.messenger.fragment;

import org.json.JSONObject;

import android.os.Bundle;

import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.services.message.MessageFragment;
import trente.asia.messenger.services.user.MsgLoginFragment;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfErrorConst;

/**
 * AbstractMsgFragment
 *
 * @author TrungND
 */
public class AbstractMsgFragment extends WelfareFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		host = BuildConfig.HOST;
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new MsgLoginFragment());
	}

	@Override
	protected void commonNotSuccess(JSONObject response){
		String returnCd = response.optString(WelfareConst.RETURN_CODE_PARAM);
		if(WfErrorConst.MS_ERR_CODE_DEPT_CHANGED.equals(returnCd) || WfErrorConst.MS_ERR_CODE_CONTENT_DELETED.equals(returnCd)){
			emptyBackStack();
			gotoFragment(new MessageFragment());
		}else{
			super.commonNotSuccess(response);
		}
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_MS;
	}
}
