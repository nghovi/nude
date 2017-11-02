package nguyenhoangviet.vpcorp.messenger.services.message.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import nguyenhoangviet.vpcorp.messenger.BuildConfig;

/**
 * Created by tien on 6/7/2017.
 */

public class WFMStampCategoryModel extends RealmObject{

	public String				key;

	public String				categoryName;

	public String				categoryPath;

	public RealmList<WFMStampModel> stamps;
	@Ignore
	public boolean				deleteFlag;

	public WFMStampCategoryModel(){
		super();
	}

	@BindingAdapter("imageUrl")
	public static void loadImage(ImageView imageView, String url){
		Picasso.with(imageView.getContext()).load(BuildConfig.HOST + url).fit().into(imageView);
	}

	public void updateStampCategory(WFMStampCategoryModel category){
		this.key = category.key;
		this.categoryName = category.categoryName;
		this.categoryPath = category.categoryPath;
	}

	public static WFMStampCategoryModel getCategory(Realm realm, String categoryId){
		return realm.where(WFMStampCategoryModel.class).equalTo("key", categoryId).findFirst();
	}

	public static void deleteStampCategory(Realm realm, String categoryId){
		WFMStampCategoryModel wfmStampCategoryModel = realm.where(WFMStampCategoryModel.class).equalTo("key", categoryId).findFirst();
		if(wfmStampCategoryModel != null){
			wfmStampCategoryModel.deleteFromRealm();
		}
	}
}
