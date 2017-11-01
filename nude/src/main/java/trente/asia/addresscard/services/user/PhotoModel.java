package trente.asia.addresscard.services.user;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by hviet on 11/1/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class PhotoModel{

	public String	image_url;
	public String	url;

}
