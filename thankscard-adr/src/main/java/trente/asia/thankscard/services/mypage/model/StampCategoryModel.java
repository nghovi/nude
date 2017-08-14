package trente.asia.thankscard.services.mypage.model;

import com.squareup.picasso.Picasso;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import trente.asia.thankscard.BuildConfig;

/**
 * Created by tien on 6/7/2017.
 */

public class StampCategoryModel extends RealmObject{

	@PrimaryKey
	public String					key;

	public String					categoryName;

	public String					categoryPath;

	public RealmList<StampModel>	stamps;

	public String					services;
	@Ignore
	public List<String>				listService;
	@Ignore
	public boolean					deleteFlag;

	public StampCategoryModel(){
		super();
	}

	@BindingAdapter("imageUrl")
	public static void loadImage(ImageView imageView, String url){
		Picasso.with(imageView.getContext()).load(BuildConfig.HOST + url).fit().into(imageView);
	}

	public void updateStampCategory(StampCategoryModel category){
		this.key = category.key;
		this.categoryName = category.categoryName;
		this.categoryPath = category.categoryPath;
	}

	public static StampCategoryModel getCategory(Realm realm, String categoryId){
		return realm.where(StampCategoryModel.class).equalTo("key", categoryId).findFirst();
	}

	public static void deleteStampCategory(Realm realm, String categoryId){
		StampCategoryModel stampCategoryModel = realm.where(StampCategoryModel.class).equalTo("key", categoryId).findFirst();
		if(stampCategoryModel != null){
			stampCategoryModel.deleteFromRealm();
		}
	}
}
