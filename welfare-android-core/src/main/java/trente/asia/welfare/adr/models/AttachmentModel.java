package trente.asia.welfare.adr.models;

/**
 * Created by viet on 7/22/2016.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class AttachmentModel{

	public String	service;
	public String	key;
	public String	fileType;
	public String	fileName;
	public String	fileUrl;
	public String	fileSize;
}
