package trente.asia.addresscard.services.shop.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject
public class ShopCardModel extends AddressCardModel{

	@JsonField
	public int		customerId;
	@JsonField
	public String	customerName;
}
