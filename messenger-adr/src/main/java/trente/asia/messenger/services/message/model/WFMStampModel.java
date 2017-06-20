package trente.asia.messenger.services.message.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import trente.asia.messenger.BuildConfig;

/**
 * Created by tien on 6/7/2017.
 */

public class WFMStampModel extends RealmObject{

	public String	key;

	public String	stampName;

	public String	keyword;

	public String	stampPath;

	public String	categoryId;
    @Ignore
	public boolean	deleteFlag;

	public WFMStampModel(){
		super();
	}

	@BindingAdapter("imageUrl")
	public static void loadImage(ImageView imageView, String url){
		Picasso.with(imageView.getContext()).load(BuildConfig.HOST + url).fit().into(imageView);
	}

	public static WFMStampModel getStamp(String stampId){
		Realm realm = Realm.getDefaultInstance();
		WFMStampModel wfmStampModel = realm.where(WFMStampModel.class).equalTo("key", stampId).findFirst();
		realm.close();
		return wfmStampModel;
	}

	public static WFMStampModel getStamp(Realm realm, String stampId){
		return realm.where(WFMStampModel.class).equalTo("key", stampId).findFirst();
	}

	public void updateStamp(WFMStampModel stamp){
		this.stampName = stamp.stampName;
		this.key = stamp.key;
		this.keyword = stamp.keyword;
		this.stampPath = stamp.stampPath;
		this.categoryId = stamp.categoryId;
	}

	public static void deleteStamp(Realm realm, String stampId){
		WFMStampModel wfmStampModel = realm.where(WFMStampModel.class).equalTo("key", stampId).findFirst();
		if(wfmStampModel != null){
			wfmStampModel.deleteFromRealm();
		}
	}
}
