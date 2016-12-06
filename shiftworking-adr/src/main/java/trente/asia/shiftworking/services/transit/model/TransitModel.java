package trente.asia.shiftworking.services.transit.model;

import java.util.List;

import trente.asia.welfare.adr.models.ImageAttachmentModel;

/**
 * TransitModel
 *
 * @author TrungND
 */

public class TransitModel{

	public String						key;
	public String						transLeave;
	public String						transArrive;

	public String						wayType;
	public String						wayTypeName;
	public String						costType;
	public String						costTypeName;
	public String						transType;
	public String						transTypeName;

	public String						fee;
	public String						note;
	public List<ImageAttachmentModel>	attachmentFile2;
}
