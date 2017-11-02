package nguyenhoangviet.vpcorp.thankscard.services.mypage.view;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyenhoangviet.vpcorp.thankscard.services.common.PageFragment;

/**
 * Created by viet on 2/18/2016.
 */
public class MyPagerAdapter extends FragmentPagerAdapter{

	List<PageFragment.PageModelContainer>	pageModels;
	PageFragment.OnPageLayoutBuilder		builder;
	FragmentManager							fragmentManager;
	List<Fragment>							fragmentList;

	public MyPagerAdapter(FragmentManager fm, List<PageFragment.PageModelContainer> pageModels, PageFragment.OnPageLayoutBuilder builder){
		super(fm);
		this.fragmentManager = fm;
		this.builder = builder;
		this.pageModels = pageModels;
		fragmentList = new ArrayList<>();
	}

	public void rebuildData(List<PageFragment.PageModelContainer> myModels){
		if(fragmentManager != null && fragmentList != null){
			for(int i = 0; i < fragmentList.size(); i++){
				fragmentManager.beginTransaction().remove(fragmentList.get(i)).commitAllowingStateLoss();
			}
		}
		pageModels = myModels;
		notifyDataSetChanged();
	}

	protected void updateAdapter(){

	}

	@Override
	public Fragment getItem(int position){
		PageFragment pageFragment = new PageFragment();
		pageFragment.setContent(pageModels.get(position), builder);
		fragmentList.add(pageFragment);
		return pageFragment;
	}

	@Override
	public int getItemPosition(Object object){
		return POSITION_NONE;
	}

	@Override
	public int getCount(){
		return pageModels.size();
	}
}
