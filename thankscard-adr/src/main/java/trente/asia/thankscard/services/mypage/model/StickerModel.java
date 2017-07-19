package trente.asia.thankscard.services.mypage.model;

import com.squareup.picasso.Picasso;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import trente.asia.thankscard.BuildConfig;

/**
 * Created by tien on 6/7/2017.
 */

public class StickerModel extends RealmObject{

	public String	key;

	public String	stampName;

	public String	keyword;

	public String	stampPath;

	public String	categoryId;
    @Ignore
	public boolean	deleteFlag;

	public StickerModel(){
		super();
	}

	@BindingAdapter("imageUrl")
	public static void loadImage(ImageView imageView, String url){
		Picasso.with(imageView.getContext()).load(BuildConfig.HOST + url).fit().into(imageView);
	}

	public static StickerModel getStamp(String stampId){
		Realm realm = Realm.getDefaultInstance();
		StickerModel stickerModel = realm.where(StickerModel.class).equalTo("key", stampId).findFirst();
		realm.close();
		return stickerModel;
	}

	public static StickerModel getStamp(Realm realm, String stampId){
		return realm.where(StickerModel.class).equalTo("key", stampId).findFirst();
	}

	public void updateStamp(StickerModel stamp){
		this.stampName = stamp.stampName;
		this.key = stamp.key;
		this.keyword = stamp.keyword;
		this.stampPath = stamp.stampPath;
		this.categoryId = stamp.categoryId;
	}

	public static void deleteStamp(Realm realm, String stampId){
		StickerModel wfmStampModel = realm.where(StickerModel.class).equalTo("key", stampId).findFirst();
		if(wfmStampModel != null){
			wfmStampModel.deleteFromRealm();
		}
	}
}
