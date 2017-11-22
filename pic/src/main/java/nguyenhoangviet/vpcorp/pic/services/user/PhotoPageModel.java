package nguyenhoangviet.vpcorp.pic.services.user;

import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by hviet on 11/1/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class PhotoPageModel{

	public Integer			current_page;
	public Integer			total_pages;
	public Integer			total_items;
	public List<PhotoModel>	photos;
}
