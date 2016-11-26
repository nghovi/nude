package trente.asia.thankscard.services.common.model;

/**
 * Created by takyas on 15/12/14.
 */
public class SpinnerItemModel{

	public SpinnerItemModel(String name, Object originModel){
		this.name = name;
		this.originModel = originModel;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String	name;
	public Object	originModel;
}
