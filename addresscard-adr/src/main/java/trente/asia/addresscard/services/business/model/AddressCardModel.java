package trente.asia.addresscard.services.business.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.addresscard.BR;

/**
 * Created by tien on 5/23/2017.
 */
@JsonObject
public class AddressCardModel extends BaseObservable {

    @JsonField
    public String cardName;
    @JsonField
    public String cardTel;
    @JsonField
    public String cardEmail;
    @JsonField
    public String createDate;

    public AttachmentModel attachment;
    @JsonField
    public int ownerId;
    @JsonField
    public String lastUpdateUserName;
    @JsonField
    public String lastUpdateDate;
    @JsonField
    public int key;
    @JsonField
    public String cardNote;

    public boolean background;


    public void setBackground(boolean background) {
        this.background = background;
        notifyPropertyChanged(BR.background);
    }

    @Bindable
    public boolean getBackground() {
        return background;
    }
}