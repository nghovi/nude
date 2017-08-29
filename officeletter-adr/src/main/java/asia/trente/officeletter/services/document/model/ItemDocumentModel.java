package asia.trente.officeletter.services.document.model;

import trente.asia.welfare.adr.models.AttachmentModel;

/**
 * Created by tien on 8/23/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ItemDocumentModel{

	public int				key;
	public String			documentCategory;
	public String			documentTitle;
	public String			documentStatus;
	public String			documentMessage;
	public String			documentNote;
	public AttachmentModel	attachment;
	public String			dateUpdate;

	public ItemDocumentModel(){
	}
}
