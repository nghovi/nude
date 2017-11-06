package nguyenhoangviet.vpcorp.pic.services.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.pic.BuildConfig;
import nguyenhoangviet.vpcorp.pic.R;
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
	public PhotoPageFragment	rightNeighborFragment;

	private InterstitialAd		mInterstitialAd;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
		mInterstitialAd = new InterstitialAd(activity);
		String nudeAdId = "ca-app-pub-4354101127297995/7048914965";
		// String nudeAdId = "ca-app-pub-3940256099942544/1033173712";//google sample id
		mInterstitialAd.setAdUnitId(nudeAdId);
		mInterstitialAd.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded(){
				mInterstitialAd.show();
			}

			@Override
			public void onAdFailedToLoad(int errorCode){
				Toast.makeText(activity, "Failed to load.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdOpened(){
				// Code to be executed when the ad is displayed.
			}

			@Override
			public void onAdLeftApplication(){
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed(){
				// Code to be executed when when the interstitial ad is closed.
			}
		});

		mInterstitialAd.loadAd(new AdRequest.Builder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_pager_container, container, false);
		}
		return mRootView;
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
				rightNeighborFragment = (PhotoPageFragment)mPagerAdapter.getItem(position + 1);
				fragment.showPhoto(getImgUrl(fragment.pagePosition));
				onFragmentSelected(fragment);
				checkToLoadMoreAndDeleteOldFragment(position);
			}
		});
		leftNeighborFragment = (PhotoPageFragment)mPagerAdapter.getItem(INITIAL_POSITION - 1);
		rightNeighborFragment = (PhotoPageFragment)mPagerAdapter.getItem(INITIAL_POSITION + 1);
	}

	private void checkToLoadMoreAndDeleteOldFragment(int position){
		if(position - INITIAL_POSITION - photoUrls.size() == 5){
			loadPhotos(false);
		}
		if(position > OFF_SCREEN_PAGE_LIMIT){
			mPagerAdapter.pagesMap.remove(mPagerAdapter.pagesMap.get(position - OFF_SCREEN_PAGE_LIMIT));
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
		url += "exclude=Nude&";
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
			if(!CCStringUtil.isEmpty(photoPageModel.photos)){
				for(PhotoModel photoModel : photoPageModel.photos){
					if(!photoUrls.contains(photoModel.image_url)){
						photoUrls.add(photoModel.image_url);
					}
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
