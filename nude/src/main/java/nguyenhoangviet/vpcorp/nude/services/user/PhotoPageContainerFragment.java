package nguyenhoangviet.vpcorp.nude.services.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.nude.BuildConfig;
import nguyenhoangviet.vpcorp.nude.R;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareFragment;

/**
 * Created by tien on 4/17/2017.
 */

public class PhotoPageContainerFragment extends WelfareFragment{

	private static final int	OFF_SCREEN_PAGE_LIMIT	= 5;
	public List<String>			photoUrls				= new ArrayList<>();
	public FrameLayout			mAdFrameLayout;
	private AdView				mAdView;

	protected Date getActiveDate(int position){
		return null;
	}

	protected PhotoPagerAdapter initPagerAdapter(){
		return new PhotoPagerAdapter(getChildFragmentManager());
	}

	protected PageSharingHolder	holder;
	protected ViewPager			mViewPager;
	protected PhotoPagerAdapter	mPagerAdapter;

	protected final int			INITIAL_POSITION	= 0;
	protected final Date		TODAY				= Calendar.getInstance().getTime();
	private int					pagerScrollingState;
	public PhotoPageFragment	leftNeighborFragment;
	public PhotoPageFragment	rightNeiborFragment;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_pager_container, container, false);
		}
		return mRootView;
	}

	public void loadAds(){
		mAdFrameLayout.setVisibility(View.GONE);
		if(mAdView != null){
			mAdFrameLayout.removeView(mAdView);
			mAdView.destroy();
		}

		mAdView = new AdView(getActivity());
		mAdView.setAdListener(new AdListener() {

			private void showToast(String message){
				View view = getView();
				if(view != null){
					Toast.makeText(getView().getContext(), message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onAdLoaded(){
				mAdFrameLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAdFailedToLoad(int errorCode){
				// showToast(String.format("Ad failed to load with error code %d.", errorCode));
				mAdFrameLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAdOpened(){
				// showToast("Ad opened.");
				mAdFrameLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAdClosed(){
				// showToast("Ad closed.");
				mAdFrameLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAdLeftApplication(){
				// showToast("Ad left application.");
				mAdFrameLayout.setVisibility(View.GONE);
			}
		});

		// App ID: ca-app-pub-4354101127297995~5318775175
		// Ad unit ID: ca-app-pub-4354101127297995/2910080169
		mAdView.setAdUnitId("ca-app-pub-4354101127297995/2910080169");
		mAdFrameLayout.addView(mAdView);
		mAdView.setAdSize(AdSize.SMART_BANNER);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
		mAdView.loadAd(adRequest);
	}

	@Override
	protected void initView(){
		super.initView();
		mAdFrameLayout = (FrameLayout)getView().findViewById(R.id.bannersizes_fl_adframe);
		holder = new PageSharingHolder();
		holder.selectedPagePosition = INITIAL_POSITION;

		mViewPager = (ViewPager)getView().findViewById(R.id.view_id_pager);
		mViewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
		mPagerAdapter = initPagerAdapter();
		mPagerAdapter.setPageSharingHolder(holder);
		mPagerAdapter.setInitialPosition(INITIAL_POSITION);
		mViewPager.setAdapter(mPagerAdapter);

		prefAccUtil.saveActiveDate(TODAY);
		setActiveDate(INITIAL_POSITION);
		setWizardProgress(INITIAL_POSITION + 1);
		mViewPager.setCurrentItem(INITIAL_POSITION);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
				pagerScrollingState = state;
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				if(isSwipeRightToLeftOnly()){
					setWizardProgress(position);
				}
				setActiveDate(position);
				holder.selectedPagePosition = position;
				PhotoPageFragment fragment = (PhotoPageFragment)mPagerAdapter.getItem(position);
				leftNeighborFragment = (PhotoPageFragment)mPagerAdapter.getItem(position - 1);
				rightNeiborFragment = (PhotoPageFragment)mPagerAdapter.getItem(position + 1);
				fragment.showPhoto(getImgUrl(fragment.pagePosition));
				onFragmentSelected(fragment);
				checkToLoadMore(position);
			}
		});
		leftNeighborFragment = (PhotoPageFragment)mPagerAdapter.getItem(INITIAL_POSITION - 1);
		rightNeiborFragment = (PhotoPageFragment)mPagerAdapter.getItem(INITIAL_POSITION + 1);


		loadAds();
	}

	private void checkToLoadMore(int position){
		if(position - INITIAL_POSITION - photoUrls.size() == 5){
			loadPhotos(false);
		}
	}

	@Override
	protected void initData(){
		loadPhotos(true);
	}

	// https://github.com/500px/api-documentation/blob/master/endpoints/photo/GET_photos.md
	public void loadPhotos(boolean showLoadingIcon){
		JSONObject jsonObject = new JSONObject();
		// "https://api.500px.com/v1/photos?feature=fresh_today&sort=created_at&image_size=3&include_store=store_download&include_states=voted";

		String url = "v1/photos?";
		url += "feature=popular&";
		url += "sort=rating&";
		url += "image_size=1080&";
		// url += "only=Nude&";
		url += "rpp=100&";
		url += "consumer_key=PSfuSSCFSFOBqq6vvhp54lEVRODRa1xncBOPIJem";

		// String url =
		// "v1/photos?feature=popular&only=Nude&sort=created_at&image_size=3&include_store=store_download&include_states=voted&consumer_key=PSfuSSCFSFOBqq6vvhp54lEVRODRa1xncBOPIJem";

		// String url = "v1/photos?";
		// url += "feature=popular";
		// url += "&sort=created_at";
		// url += "&image_size=3";
		// url += "&include_store=store_download";
		// url += "&include_states=voted";
		// url += "&consumer_key=PSfuSSCFSFOBqq6vvhp54lEVRODRa1xncBOPIJem";
		// url += "&only=4";

		requestLoad(url, jsonObject, showLoadingIcon);
	}

	/**
	 * On success load.
	 *
	 * @param response the response
	 */
	@Override
	protected void onSuccessLoad(JSONObject response, boolean isAlert, String url){
		if(response == null){
			response = createSystemErrorResponse(activity);
		}
		if(!CCStringUtil.isEmpty(response.toString())){

			successLoad(response, url);

			dismissLoad();
		}else{
			loadingDialog.continueShowing = false;
			dismissLoad();
			// commonNotSuccess(response, url);
		}
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		super.successLoad(response, url);

		try{
			PhotoPageModel photoPageModel = LoganSquare.parse(response.toString(), PhotoPageModel.class);
			for(PhotoModel photoModel : photoPageModel.photos){
				if(!photoUrls.contains(photoModel.image_url)){
					photoUrls.add(photoModel.image_url);
				}
			}
			if(photoUrls.size() > 0){
				for(int position : mPagerAdapter.pagesMap.keySet()){
					PhotoPageFragment photoPageFragment = (PhotoPageFragment)mPagerAdapter.getItem(position);
					String imgUrl = getImgUrl(photoPageFragment.pagePosition);
					photoPageFragment.showPhoto(imgUrl);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	protected void onFragmentSelected(PhotoPageFragment fragment){

	}

	public int getScrollingState(){
		return pagerScrollingState;
	}

	protected void setWizardProgress(int progress){
	}

	protected boolean isSwipeRightToLeftOnly(){
		return false;
	}

	protected void setActiveDate(int position){
		Date activeDate = getActiveDate(position);
		String title = CCFormatUtil.formatDateCustom("yyyy/M", activeDate);
		updateHeader(title);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public String getImgUrl(int pagePosition){
		String imgUrl = "";
		if(photoUrls.size() > 0){
			int urlIdx = Math.abs(pagePosition % photoUrls.size());
			imgUrl = photoUrls.get(urlIdx);
		}
		return imgUrl;
	}
}
