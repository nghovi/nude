package trente.asia.addresscard.services.user;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * PhotoPageFragment
 *
 * @author Vietnh
 */
public class PhotoPageFragment extends WelfareFragment{

	ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_photo_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		imageView = (ImageView)getView().findViewById(R.id.img_photo);
		// showPhoto();
	}

	public void showPhoto(String imgUrl){
		if(imageView != null && imageView.getDrawable() == null){
			WfPicassoHelper.loadImage(activity, imgUrl, imageView, null);
		}
	}

	@Override
	protected void initData(){
		String imageUrl = ((PhotoPageContainerFragment)getParentFragment()).getImgUrl(pagePosition);
		showPhoto(imageUrl);
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_CL;
	}

	protected Date				selectedDate;
	protected int				pagePosition;
	protected PageSharingHolder	pageSharingHolder;

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}

	public void setPageSharingHolder(PageSharingHolder pageSharingHolder){
		this.pageSharingHolder = pageSharingHolder;
	}

	public void setPagePosition(int pagePosition){
		this.pagePosition = pagePosition;
	}

	public boolean isActivePage(){
		return pageSharingHolder.selectedPagePosition == pagePosition;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		imageView.setImageDrawable(null);
		imageView = null;
	}

}
