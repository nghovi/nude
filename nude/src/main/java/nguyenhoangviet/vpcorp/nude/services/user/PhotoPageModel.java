package nguyenhoangviet.vpcorp.nude.services.user;

import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by hviet on 11/1/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class PhotoPageModel{

	public String			current_page;
	public String			total_pages;
	public String			total_items;
	public List<PhotoModel>	photos;
}
