package asia.chiase.core.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultJsonModel{

	public Boolean				result	= false;

	public String				data	= new String();

	public String				message	= new String();

	public List<String>			msglist	= new ArrayList<String>();

	public Map<String, String>	map		= new LinkedHashMap<String, String>();

	public ResultJsonModel(){
		super();
	}

	public ResultJsonModel(boolean result){
		this.result = result;
	}

	public ResultJsonModel(boolean result, String msg){
		this(result);
		this.message = msg;
	}

	public ResultJsonModel(boolean result, List<String> msglist){
		this(result);
		this.msglist = msglist;
	}

	public boolean getResult(){
		return result;
	}

	public void setResult(boolean result){
		this.result = result;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public List<String> getMsglist(){
		return msglist;
	}

	public void setMsglist(List<String> msglist){
		this.msglist = msglist;
	}

	public String getData(){
		return data;
	}

	public void setData(String data){
		this.data = data;
	}

	public Map<String, String> getMap(){
		return map;
	}

	public void setMap(Map<String, String> map){
		this.map = map;
	}

}
