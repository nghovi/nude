package trente.asia.messenger.services.message.model;

/**
 * SocketModel
 *
 * @author TrungND
 */
public class SocketModel{

	public String	socketType;

	public String	serviceCd;

	public String	key;

	public String	parentKey;

	public String	fromUserKey;

	public String	data;

	public SocketModel(){

	}

	public SocketModel(String serviceCd, String socketType, String key, String fromUserKey){
		this.serviceCd = serviceCd;
		this.socketType = socketType;
		this.key = key;
		this.fromUserKey = fromUserKey;
	}

	public String getSocketType(){
		return socketType;
	}

	public void setSocketType(String socketType){
		this.socketType = socketType;
	}

	public String getServiceCd(){
		return serviceCd;
	}

	public void setServiceCd(String serviceCd){
		this.serviceCd = serviceCd;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getParentKey(){
		return parentKey;
	}

	public void setParentKey(String parentKey){
		this.parentKey = parentKey;
	}

	public String getFromUserKey(){
		return fromUserKey;
	}

	public void setFromUserKey(String fromUserKey){
		this.fromUserKey = fromUserKey;
	}

	public String getData(){
		return data;
	}

	public void setData(String data){
		this.data = data;
	}
}
