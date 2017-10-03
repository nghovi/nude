package trente.asia.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by viet on 11/25/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ApproveHistory{

	public String	userId;
	public String	userName;
	public String	userAvatarPath;
	public String	historyDate;
	public String	historyStatus;
	public String	resultType;
	public String	resultTypeName;
	public String	key;
	public String	historyComment;
}
