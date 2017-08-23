package asia.trente.officeletter.services.document.model;

/**
 * Created by tien on 8/23/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class DocumentModel{

	public int					key;
	public String				deliveryStatus;
	public String				deliveryDate;
	public ItemDocumentModel	document;
	public int					passwordType;
	public String				passwordHint;
	public String				dateUpdate;

	public DocumentModel(){
	}
}
