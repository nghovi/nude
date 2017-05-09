package trente.asia.addresscard.services.card;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by tien on 5/8/2017.
 */

public class Category {
    public String name;
    public ArrayList<Company> listCompany;

    Category(String name, ArrayList<Company> listCompany) {
        this.name = name;
        this.listCompany = listCompany;
    }
    public String getSize() {
        return listCompany.size() + "";
    }

    public int getImageId(int position) {
        if (position < listCompany.size()) {
            return listCompany.get(position).companyIcon;
        } else {
            return -1;
        }
    }

    @BindingAdapter({"bind:imageId"})
    public static void loadImage(ImageView view, int imageId) {
        if (imageId < 0) {
            return;
        }
        view.setImageResource(imageId);
    }
}
