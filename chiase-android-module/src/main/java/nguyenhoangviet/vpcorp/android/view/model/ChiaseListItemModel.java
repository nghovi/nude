package nguyenhoangviet.vpcorp.android.view.model;

/**
 * Created by TrungND on 10/29/2014.
 */
public class ChiaseListItemModel{

	public String	key;

	public String	value;

	public ChiaseListItemModel(){

	}

	public ChiaseListItemModel(String key, String value){
		this.key = key;
		this.value = value;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}
}
