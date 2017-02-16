package trente.asia.welfare.adr.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huy-nq on 8/25/2016.
 */
public class SettingModel implements Serializable{

	public String	DR_PUBLIC_LEVEL;
	public String	WF_PUSH_SETTING;
	public String	WF_MAX_FILE_SIZE;
	public String	SW_APPOINTMENT_ENABLED;

	public String getDR_PUBLIC_LEVEL(){
		return DR_PUBLIC_LEVEL;
	}

	public void setDR_PUBLIC_LEVEL(String DR_PUBLIC_LEVEL){
		this.DR_PUBLIC_LEVEL = DR_PUBLIC_LEVEL;
	}

	public String getWF_PUSH_SETTING(){
		return WF_PUSH_SETTING;
	}

	public void setWF_PUSH_SETTING(String WF_PUSH_SETTING){
		this.WF_PUSH_SETTING = WF_PUSH_SETTING;
	}
}
