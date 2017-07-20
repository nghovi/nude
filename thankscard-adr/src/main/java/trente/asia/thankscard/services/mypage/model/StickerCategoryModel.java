package trente.asia.thankscard.services.mypage.model;

import com.squareup.picasso.Picasso;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import trente.asia.thankscard.BuildConfig;

/**
 * Created by tien on 6/7/2017.
 */

public class StickerCategoryModel extends RealmObject{

	public String				key;

	public String				categoryName;

	public String				categoryPath;

	public RealmList<StickerModel> stamps;
	@Ignore
	public boolean				deleteFlag;

	public StickerCategoryModel(){
		super();
	}

	@BindingAdapter("imageUrl")
	public static void loadImage(ImageView imageView, String url){
		Picasso.with(imageView.getContext()).load(BuildConfig.HOST + url).fit().into(imageView);
	}

	public void updateStampCategory(StickerCategoryModel category){
		this.key = category.key;
		this.categoryName = category.categoryName;
		this.categoryPath = category.categoryPath;
	}

	public static StickerCategoryModel getCategory(Realm realm, String categoryId){
		return realm.where(StickerCategoryModel.class).equalTo("key", categoryId).findFirst();
	}

	public static void deleteStampCategory(Realm realm, String categoryId){
		StickerCategoryModel stickerCategoryModel = realm.where(StickerCategoryModel.class).equalTo("key", categoryId).findFirst();
		if(stickerCategoryModel != null){
			stickerCategoryModel.deleteFromRealm();
		}
	}
}
