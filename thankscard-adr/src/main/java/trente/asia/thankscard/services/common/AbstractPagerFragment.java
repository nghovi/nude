package trente.asia.thankscard.services.common;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import trente.asia.thankscard.R;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.mypage.view.MyPagerAdapter2;

/**
 * Created by viet on 2/15/2016.
 */
public abstract class AbstractPagerFragment extends AbstractTCFragment{

	Button				btnBack;
	Button				btnNext;
	RelativeLayout		rltStickers;
	List<HistoryModel>	lstHistory;
	int					defaultPos	= 0;

	@Override
	public boolean hasBackBtn(){
		return true;
	}

	@Override
	public boolean hasSettingBtn(){
		return true;
	}

	@Override
	public void buildBodyLayout(){
		buildPager();
		buildPagerHeader();
	}

	abstract protected void buildPagerHeader();

	/**
	 * Do not handle event on this function, use setOnPageSelectedListener
	 * 
	 * @return
	 */
	// abstract protected PageFragment.OnPageLayoutBuilder getPageLayoutBuilder();

	final void buildPager(){
		buildDirectButton();
	}

	// protected void setOnPageSelectedListener(){
	// this.onPageSelectedListener = null;
	// }

	protected void buildDirectButton(){
		btnNext = (Button)getView().findViewById(R.id.btn_view_common_pager_next);
		btnBack = (Button)getView().findViewById(R.id.btn_view_common_pager_back);
		rltStickers = (RelativeLayout)getView().findViewById(R.id.rlt_stickers);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onPageHistorySelected(--defaultPos);
				buildEnabledButtons();
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onPageHistorySelected(++defaultPos);
				buildEnabledButtons();
			}
		});
	}

	public void buildEnabledButtons(){
		if (defaultPos == 0) {
			btnBack.setEnabled(false);
		} else {
			btnBack.setEnabled(true);
		}

		if (defaultPos == lstHistory.size() - 1) {
			btnNext.setEnabled(false);
		} else {
			btnNext.setEnabled(true);
		}
	}

	abstract protected void onPageHistorySelected(int position);

	final protected List<PageFragment.PageModelContainer> wrapToPageModels(List<Object> realModels){
		List<PageFragment.PageModelContainer> pageModels = new ArrayList<>();
		for(final Object realModel : realModels){
			pageModels.add(new PageFragment.PageModelContainer() {

				@Override
				public Object getPageModel(){
					return realModel;
				}
			});
		}
		return pageModels;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		btnBack = null;
		btnNext = null;
	}

}
