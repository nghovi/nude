package trente.asia.addresscard.services.card.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CardModel extends BaseObservable{
    public int customerId;
    public String customerName;
    public String cardName;
    public String cardTel;
    public String cardEmail;
    public String createDate;
    public Attachment attachment;
    @JsonIgnore
    public boolean background;

    @BindingAdapter({"bind:image"})
    public static void loadImage(ImageView view, Attachment image) {
        WfPicassoHelper.loadImage(view.getContext(), BuildConfig.HOST + image.fileUrl, view, null);
    }

    public void setBackground(boolean background) {
        this.background = background;
        notifyPropertyChanged(BR.background);
    }

    @Bindable
    public boolean getBackground() {
        return background;
    }
}
