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

	protected MyPagerAdapter2	adapter;
	protected ViewPager			viewPager;
	Button						btnBack;
	Button						btnNext;
	RelativeLayout				rltStickers;

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
		viewPager = (ViewPager)getView().findViewById(R.id.pager);
		// setOnPageSelectedListener();
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
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			}
		});
	}

	protected void loadPagerLayout(final List<HistoryModel> lstHistory, int defaultPos, boolean isMultiplePages){
		adapter = createMyPagerAdapter(lstHistory);
		viewPager.setAdapter(adapter);
		if(isMultiplePages){
			if(defaultPos == 0){
				btnBack.setEnabled(false);
				// setImageButtonEnabled(getContext(), false, btnBack, R.drawable.btn_prev_on);
			}
			if(defaultPos == adapter.getCount() - 1){
				btnNext.setEnabled(false);
				// setImageButtonEnabled(getContext(), false, btnNext, R.drawable.btn_prev_on);
			}
			viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

				}

				@Override
				public void onPageSelected(int position){
					if(position == 0){
						btnBack.setEnabled(false);
						// setImageButtonEnabled(getContext(), false, btnBack, R.drawable.btn_prev_on);
					}else{
						btnBack.setEnabled(true);
						// setImageButtonEnabled(getContext(), true, btnBack, R.drawable.btn_prev_on);
					}
					if(viewPager.getCurrentItem() == adapter.getCount() - 1){
						btnNext.setEnabled(false);
						// setImageButtonEnabled(getContext(), false, btnNext, R.drawable.btn_prev_on);
					}else{
						btnNext.setEnabled(true);
						// setImageButtonEnabled(getContext(), true, btnNext, R.drawable.btn_prev_on);
					}
					onPageHistorySelected(position);
				}

				@Override
				public void onPageScrollStateChanged(int state){

				}
			});
			viewPager.setCurrentItem(defaultPos);
		}
		// call for the first page
		if(viewPager.getCurrentItem() == 0){
			onPageHistorySelected(0);
		}
	}

	abstract protected void onPageHistorySelected(int position);

	final private MyPagerAdapter2 createMyPagerAdapter(List<HistoryModel> lstHistory){
		MyPagerAdapter2 adapter2 = new MyPagerAdapter2(activity, lstHistory);
		return adapter2;
	}

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
		viewPager = null;
		adapter = null;
		btnBack = null;
		btnNext = null;
	}

}
