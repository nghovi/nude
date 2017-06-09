package trente.asia.messenger.services.message.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by tien on 6/7/2017.
 */

@Table(name = "ss_stamp")
public class WFMStampModel extends Model{

	@Column(name = "stamp_id")
	public String	stampId;

	@Column(name = "stamp_name")
	public String	stampName;

	@Column(name = "stamp_key")
	public String	stampKey;

	@Column(name = "stamp_keyword")
	public String	stampKeyword;

	@Column(name = "stamp_url")
	public String	stampUrl;

	@Column(name = "category_id")
	public String	categoryId;

	public WFMStampModel() {
		super();
	}

	public WFMStampModel(SSStampModel stamp){
		super();
		this.stampKey = stamp.stampKey;
		this.stampName = stamp.stampName;
		this.stampId = stamp.key;
		this.stampKeyword = stamp.keyword;
		this.stampUrl = stamp.attachment.fileUrl;
		this.categoryId = stamp.categoryId;
	}

	public static List<WFMStampModel> getAll(String categoryId) {
		return new Select().from(WFMStampModel.class)
				.where("category_id = ?", categoryId)
				.execute();
	}

	public static WFMStampModel get(String stampId) {
		List<WFMStampModel> stamps = new Select().from(WFMStampModel.class)
				.where("stamp_id = ?", stampId).execute();
		return stamps.get(0);
	}
}
