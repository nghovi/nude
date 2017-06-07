package trente.asia.messenger.services.message.model;

import io.realm.RealmObject;

/**
 * Created by tien on 6/7/2017.
 */

public class WFMStampModel extends RealmObject{

	public String	stampName;
	public String	stampKey;
	public String	stampNote;
	public String	keyword;
	public String	stampUrl;
	public int		key;
	public int		categoryId;

	public void setValues(SSStampModel stamp) {
		this.stampKey = stamp.stampKey;
		this.stampName = stamp.stampName;
		this.stampNote = stamp.stampNote;
		this.keyword = stamp.keyword;
		this.stampUrl = stamp.attachmentModel.fileUrl;
		this.key = stamp.key;
		this.categoryId = stamp.categoryId;
	}
}
