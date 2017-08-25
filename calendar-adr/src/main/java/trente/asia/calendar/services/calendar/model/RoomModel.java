package trente.asia.calendar.services.calendar.model;

import java.util.List;

/**
 * Created by hviet on 7/26/17.
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RoomModel{

	public String	key;
	public String	roomName;
	public String	roomNote;
	public String	color		= "#000000";
	public String	textColor	= "#000000";

	public static RoomModel get(List<RoomModel> rooms, String roomId){
		for(RoomModel roomModel : rooms){
			if(roomModel.key.equals(roomId)){
				return roomModel;
			}
		}
		return null;
	}
}
