package trente.asia.welfare.adr.models;

import trente.asia.android.view.model.ChiaseSpinnerModel;

/**
 * CompanyModel
 *
 * @author TrungND
 */
public class CompanyModel{

	public String	key;
	public String	companyName;
	public String	value;

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}

	public ChiaseSpinnerModel convert2SpinnerModel(){
		ChiaseSpinnerModel model = new ChiaseSpinnerModel(this.key, this.value);
		return model;
	}
}
