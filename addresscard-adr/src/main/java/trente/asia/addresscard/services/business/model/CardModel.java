package trente.asia.addresscard.services.business.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.addresscard.BR;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CardModel extends BaseObservable{
    @JsonField
    public              int                     customerId;
    @JsonField
    public              String                  customerName;
    @JsonField
    public              String                  cardName;
    @JsonField
    public              String                  cardTel;
    @JsonField
    public              String                  cardEmail;
    @JsonField
    public              String                  createDate;

    public              AttachmentModel         attachment;
    @JsonField
    public              int                     ownerId;
    @JsonField
    public              int                     lastUpdateUser;
    @JsonField
    public              String                  lastUpdateDate;
    @JsonField
    public              int                     key;

    public              boolean                 background;


    public void setBackground(boolean background) {
        this.background = background;
        notifyPropertyChanged(BR.background);
    }

    @Bindable
    public boolean getBackground() {
        return background;
    }
}
