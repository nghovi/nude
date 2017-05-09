package trente.asia.addresscard.commons.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.card.BusinessCardMainFragment;
import trente.asia.addresscard.services.card.CategoryListFragment;
import trente.asia.addresscard.services.user.ACLoginFragment;
import trente.asia.addresscard.setting.SettingFragment;
import trente.asia.android.view.ChiaseImageView;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractAddressCardFragment extends WelfareFragment implements View.OnClickListener{

	// test
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		buildFooter();
	}

	@Override
	protected void initView(){
		super.initView();
	}

	/**
	 * return 0 if we don't want to show footer
	 *
	 * @return view id of the footer item. E.g. lnr_view_common_footer_mypage
	 */
	public abstract int getFooterItemId();

	protected void buildFooter(){
		final int footerItemId = getFooterItemId();
		if(footerItemId != 0){
			View.OnClickListener listener = new View.OnClickListener() {

				@Override
				public void onClick(View view){
					switch(view.getId()){
					case R.id.lnr_view_footer_card:
						onClickFooterItemCard();
						break;
					case R.id.lnr_view_footer_company:
						onClickFooterItemCompany();
						break;
					case R.id.lnr_view_footer_setting:
						onClickFooterItemSetting();
						break;
					default:
						break;
					}
				}
			}; // end listener

			getView().findViewById(R.id.lnr_view_footer_card).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_footer_company).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_footer_setting).setOnClickListener(listener);
			LinearLayout lnrFooter = (LinearLayout)getView().findViewById(footerItemId);
			setSelectedFooterItem(lnrFooter);
		}

	}

	private void setSelectedFooterItem(ViewGroup footer){
		for(int i = 0; i < footer.getChildCount(); i++){
			View view = footer.getChildAt(i);
			if(view instanceof ChiaseImageView){
				view.setAlpha((float)1);
			}else if(view instanceof ChiaseTextView){
				((ChiaseTextView)view).setTextColor(Color.WHITE);
			}else if(view instanceof ViewGroup){
				setSelectedFooterItem((ViewGroup)view);
			}
		}
	}

	public void onClickFooterItemCard(){
		emptyBackStack();
		gotoFragment(new BusinessCardMainFragment());
	}

	public void onClickFooterItemCompany(){
		emptyBackStack();
		gotoFragment(new CategoryListFragment());
	}

	public void onClickFooterItemSetting(){
		emptyBackStack();
		gotoFragment(new SettingFragment());
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_CL;
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new ACLoginFragment());
	}

}