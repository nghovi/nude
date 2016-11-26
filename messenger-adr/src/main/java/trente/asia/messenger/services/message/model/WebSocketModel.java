package trente.asia.messenger.services.message.model;

/**
 * WebSocketModel
 *
 * @author TrungND
 */
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
