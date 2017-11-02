package nguyenhoangviet.vpcorp.thankscard.services.common.model;

import java.util.ArrayList;
import java.util.List;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;

/**
 * Created by viet on 3/16/2016.
 */
public class Category{

	public String	categoryId;
	public String	categoryName;
	public int		categoryImageId;

	public Category(String categoryId, String categoryName, int categoryImageId){
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryImageId = categoryImageId;
	}

	public static List<Category> getCategoryList(){
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(TcConst.THANKS_CATEGORY_ID, "Thanks!!", R.drawable.tc_category_thanks));
		categories.add(new Category(TcConst.CONGRAT_CATEGORY_ID, "Congrats!!", R.drawable.tc_category_congrats));
		categories.add(new Category(TcConst.GOOD_JOB_CATEGORY_ID, "Good Job!!", R.drawable.tc_category_good));
		return categories;
	}

    public static List<String> getCategoryNameList(){
        List<String> lstName = new ArrayList<>();
        lstName.add("Thanks!!");
        lstName.add("Congrats!!");
        lstName.add("Good Job!!");
        return lstName;
    }
}
