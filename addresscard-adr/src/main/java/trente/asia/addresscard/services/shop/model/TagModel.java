package trente.asia.addresscard.services.shop.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by viet on 5/22/2017.
 */

@JsonObject
public class TagModel{

	@JsonField
	public String			key;

	@JsonField
	public String			tagName;

	@JsonField
	public String			tagNote;

	@JsonField
	public List<AddressCardModel>	cards;

	@JsonField
	public Boolean			selected	= true;

	public static String getCardSize(TagModel tagModel){
		return String.valueOf(tagModel.cards.size());
	}
}
