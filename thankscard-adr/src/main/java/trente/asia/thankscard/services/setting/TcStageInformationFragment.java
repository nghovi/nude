package nguyenhoangviet.vpcorp.thankscard.services.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.databinding.FragmentStageInformationBinding;
import nguyenhoangviet.vpcorp.thankscard.fragments.AbstractTCFragment;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

/**
 * Created by tien on 7/10/2017.
 */

public class TcStageInformationFragment extends AbstractTCFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if (mRootView == null) {
			FragmentStageInformationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stage_information, container, false);
			PreferencesAccountUtil preferences = new PreferencesAccountUtil(getContext());
			binding.txtPointGold.setText(String.format(getString(R.string.tc_point_gold), preferences.get(TcConst.PREF_POINT_GOLD)));
			binding.txtPointSilver.setText(String.format(getString(R.string.tc_point_silver), preferences.get(TcConst.PREF_POINT_SILVER)));
			binding.txtPointBronze.setText(String.format(getString(R.string.tc_point_bronze), preferences.get(TcConst.PREF_POINT_BRONZE)));
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_stage_information;
	}

	@Override
	public boolean hasBackBtn(){
		return true;
	}

	@Override
	public boolean hasSettingBtn(){
		return false;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public int getTitle(){
		return R.string.tc_stage_information;
	}

	@Override
	public void buildBodyLayout(){

	}

	private void log(String msg){
		Log.e("TcStageInformation", msg);
	}
}
