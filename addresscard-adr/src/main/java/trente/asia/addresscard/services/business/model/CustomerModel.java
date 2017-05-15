package trente.asia.addresscard.services.business.model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerModel extends BaseObservable {
    public String customerName;
    public String customerAddress;
    public String customerTel;
    public String customerUrl;
    public String customerDomain;

    public CustomerModel(String customerName, String customerUrl) {
        this.customerName = customerName;
        this.customerUrl = customerUrl;
    }

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView view, String url){
        WfPicassoHelper.loadImage(view.getContext(), url, view, null);
    }
}
