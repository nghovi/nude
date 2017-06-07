package trente.asia.messenger.services.message.model;

import io.realm.RealmObject;

/**
 * Created by tien on 6/7/2017.
 */

public class WFMStampCategoryModel extends RealmObject{

	public String	categoryName;
	public String	categoryKey;
	public String	categoryNote;
	public int		key;

	public void setValues(SSStampCategoryModel category) {
		this.categoryName = category.categoryName;
		this.categoryKey = category.categoryKey;
		this.categoryNote = category.categoryNote;
		this.key = category.key;
	}
}
