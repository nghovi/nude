package trente.asia.addresscard.services.business.model;

import java.util.List;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerModel{

	public int						categoryId;
	public String					categoryName;
	public String					customerName;
	public String					customerAddress;
	public String					customerTel;
	public String					customerUrl;
	public String					customerDomain;
	public int						key;
	public AttachmentModel			attachment;
	public List<BusinessCardModel>	cards;
	public int						commentCount;
	public String					lastCommentDate;

	public String getCardNumber(){
		return cards.size() + "";
	}

	public String getCommentNumber() {
        return commentCount + "";
    }

	public int getKey(){
		return key;
	}
}
