package nguyenhoangviet.vpcorp.android.view.model;

/**
 * ChiaseSpinnerModel
 *
 * @author
 */
public class ChiaseSpinnerModel{

	public String	key;

	public String	value;

	public ChiaseSpinnerModel(){

	}

	public ChiaseSpinnerModel(String key, String value){
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
