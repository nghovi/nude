package trente.asia.android.view.adapter;

/**
 * Created by TrungND on 10/24/2014.
 */
public class SelectedFileItem{

	private int		icon;

	private String	name;

	private String	path;

	public SelectedFileItem(){
	}

	public SelectedFileItem(int icon, String name, String path){
		this.icon = icon;
		this.name = name;
		this.path = path;
	}

	public int getIcon(){
		return icon;
	}

	public void setIcon(int icon){
		this.icon = icon;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getPath(){
		return path;
	}

	public void setPath(String path){
		this.path = path;
	}
}
