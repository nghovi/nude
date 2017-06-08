package trente.asia.messenger.services.message.model;

import java.util.List;

import trente.asia.welfare.adr.models.AttachmentModel;

/**
 * Created by tien on 6/6/2017.
 */

public class SSStampCategoryModel{

	public String				categoryName;
	public String				categoryKey;
	public String				categoryNote;
	public List<SSStampModel>	stamps;
	public int					key;
	public AttachmentModel		attachment;
}
