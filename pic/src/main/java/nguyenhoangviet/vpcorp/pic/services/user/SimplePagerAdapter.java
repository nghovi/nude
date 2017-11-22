package nguyenhoangviet.vpcorp.pic.services.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by hviet on 11/22/17.
 */

public class SimplePagerAdapter extends PagerAdapter{

	private Context					mContext;
	public Map<Integer, ImageView>	datas	= new HashMap();
	public List<String>				photoUrls;

	public SimplePagerAdapter(Context activity, List<String> urls){
		super();
		mContext = activity;
		photoUrls = urls;
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position){
		ImageView imageView = datas.get(position);
		if(imageView == null){
			imageView = new ImageView(mContext);
			String imgUrl = getImgUrl(position);
			WfPicassoHelper.loadImage(mContext, imgUrl, imageView, null);
			datas.put(position, imageView);
			collection.addView(imageView);
		}
		return imageView;
	}

	public String getImgUrl(int pagePosition){
		String imgUrl = "";
		if(photoUrls.size() > 0){
			int urlIdx = Math.abs(pagePosition % photoUrls.size());
			imgUrl = photoUrls.get(urlIdx);
		}
		return imgUrl;
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view){
		collection.removeView((View)view);
		ImageView img = datas.remove(position);
		img.setImageDrawable(null);
		img = null;
	}

	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object){
		return view == object;
	}

	public void loadPhotoAt(int position){
		ImageView img = datas.get(position);
		if(img != null && img.getDrawable() == null){
			String imgUrl = getImgUrl(position);
			WfPicassoHelper.loadImage(mContext, imgUrl, img, null);
		}
	}
}
