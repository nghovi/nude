package trente.asia.calendar.services.calendar.model;

/**
 * Created by trungnd on 3/2/17.
 */

public class CategoryModel{

	public String	key;

	public String	categoryName;

	public String	categoryColor;

	public String	categoryNote;

	public int		userId;

	public CategoryModel(String colorCode){
		this.categoryColor = colorCode;
	}

	public CategoryModel(){
	}
}
