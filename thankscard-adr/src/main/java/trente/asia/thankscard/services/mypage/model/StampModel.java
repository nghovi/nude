package nguyenhoangviet.vpcorp.thankscard.services.mypage.model;

import com.squareup.picasso.Picasso;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import nguyenhoangviet.vpcorp.thankscard.BuildConfig;

/**
 * Created by tien on 6/7/2017.
 */

public class StampModel extends RealmObject{
	@PrimaryKey
	public String	key;

	public String	stampName;

	public String	keyword;

	public String	stampPath;

	public String	categoryId;
    @Ignore
	public boolean	deleteFlag;

	public StampModel(){
		super();
	}

	@BindingAdapter("imageUrl")
	public static void loadImage(ImageView imageView, String url){
		Picasso.with(imageView.getContext()).load(BuildConfig.HOST + url).fit().into(imageView);
	}

	public static StampModel getStamp(String stampId){
		Realm realm = Realm.getDefaultInstance();
		StampModel stampModel = realm.where(StampModel.class).equalTo("key", stampId).findFirst();
		realm.close();
		return stampModel;
	}

	public static StampModel getStamp(Realm realm, String stampId){
		return realm.where(StampModel.class).equalTo("key", stampId).findFirst();
	}

	public void updateStamp(StampModel stamp){
		this.stampName = stamp.stampName;
		this.key = stamp.key;
		this.keyword = stamp.keyword;
		this.stampPath = stamp.stampPath;
		this.categoryId = stamp.categoryId;
	}

	public static void deleteStamp(Realm realm, String stampId){
		StampModel wfmStampModel = realm.where(StampModel.class).equalTo("key", stampId).findFirst();
		if(wfmStampModel != null){
			wfmStampModel.deleteFromRealm();
		}
	}
}
