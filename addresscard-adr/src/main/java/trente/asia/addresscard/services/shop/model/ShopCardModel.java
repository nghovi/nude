package trente.asia.addresscard.services.shop.model;

import android.content.Context;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject
public class ShopCardModel extends AddressCardModel{

	@JsonField
	public int				customerId;
	@JsonField
	public String			customerName;

	@JsonField
	public List<TagModel>	tags;

	public String getTagsString(Context context){
		return TagModel.getSelectedTagsString(context, tags);
	}

	public void setTagSelected(boolean selected){
		if(!CCCollectionUtil.isEmpty(tags)){
			for(TagModel tagModel : tags){
				tagModel.selected = selected;
			}
		}
	}
}
