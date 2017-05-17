package trente.asia.addresscard.services.business.model;

import java.util.List;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerModel {
    public      int                 categoryId;
    public      String              categoryName;
    public      String              customerName;
    public      String              customerAddress;
    public      String              customerTel;
    public      String              customerUrl;
    public      String              customerDomain;
    public      int                 key;
    public      AttachmentModel     attachment;
    public      List<CardModel>     cards;

    public String getCardNumber() {
        return cards.size() + "";
    }
}
