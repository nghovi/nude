package trente.asia.messenger.services.message.model;

/**
 * WebSocketModel
 *
 * @author TrungND
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class WebSocketModel{

	public SocketModel model;

	public WebSocketModel(){

	}

	public WebSocketModel(SocketModel model){
		this.model = model;
	}

	public SocketModel getModel(){
		return model;
	}

	public void setModel(SocketModel model){
		this.model = model;
	}
}
