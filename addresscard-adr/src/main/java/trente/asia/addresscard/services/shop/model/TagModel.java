package trente.asia.addresscard.services.shop.model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by viet on 5/22/2017.
 */

@JsonObject
public class TagModel extends BaseObservable{

	@JsonField
	public String					key;

	@JsonField
	public String					tagName;

	@JsonField
	public String					tagNote;

	@JsonField
	public List<AddressCardModel>	cards;

	@Bindable
	public Boolean getSelected(){
		return selected;
	}

	public void setSelected(Boolean selected){
		this.selected = selected;
		notifyPropertyChanged(BR.selected);
	}

	@JsonField
	public Boolean selected = false;

	public static String getCardSize(TagModel tagModel){
		return String.valueOf(tagModel.cards.size());
	}

	public static String getSelectedTagsString(Context context, List<TagModel> tagModels){
		List<String> tagNames = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(tagModels)){
			for(TagModel tagModel : tagModels){
				if(tagModel.selected){
					tagNames.add(tagModel.tagName);
				}
			}
		}
		if(CCCollectionUtil.isEmpty(tagNames)){
			return context.getResources().getString(R.string.shop_cards_no_tag);
		}
		return StringUtils.join(tagNames, ", ");
	}

	public static String getSelectedTagKeys(List<TagModel> tagModels){
		List<String> tagIds = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(tagModels)){
			for(TagModel tagModel : tagModels){
				if(tagModel.selected){
					tagIds.add(tagModel.key);
				}
			}
		}
		return StringUtils.join(tagIds, ",");
	}

}
