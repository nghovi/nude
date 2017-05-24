package trente.asia.addresscard.services.business.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject
public class AttachmentModel{

	@JsonField
	public String	service;

	@JsonField
	public int		key;

	@JsonField
	public String	fileType;

	@JsonField
	public String	fileName;

	@JsonField
	public String	fileUrl;

	@JsonField
	public int		fileSize;

	@JsonField
	public String	attachedType;

	@JsonField
	public Boolean	isOrginal;

}