package trente.asia.messenger.services.message.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by tien on 6/7/2017.
 */

@Table(name = "ss_stamp_category")
public class WFMStampCategoryModel extends Model{

	@Column(name = "category_id")
	public String	categoryId;

	@Column(name = "category_name")
	public String	categoryName;

	@Column(name = "category_key")
	public String	categoryKey;

	@Column(name = "category_url")
	public String	categoryUrl;

	public WFMStampCategoryModel(){
		super();
	}

	public WFMStampCategoryModel(SSStampCategoryModel category) {
		super();
		this.categoryId = category.key;
		this.categoryName = category.categoryName;
		this.categoryKey = category.categoryKey;
		this.categoryUrl = category.attachment.fileUrl;
	}

	public static WFMStampCategoryModel get(String categoryId) {
		List<WFMStampCategoryModel> categories = new Select().from(WFMStampCategoryModel.class)
				.where("category_id = ?", categoryId)
				.execute();
		return  categories.get(0);
	}

	public static List<WFMStampCategoryModel> getAll() {
		return new Select().from(WFMStampCategoryModel.class)
				.execute();
	}
}
