package trente.asia.addresscard.services.business.model;

import java.util.List;

/**
 * Created by tien on 5/8/2017.
 */

public class CategoryModel {
    public      String                      categoryName;
    public      String                      categoryNote;
    public      List<CustomerModel>         customers;
    public      int                         key;

    public String getCustomerCount() {
        return customers.size() + "";
    }
}
