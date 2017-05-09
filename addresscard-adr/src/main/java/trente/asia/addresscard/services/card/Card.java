package trente.asia.addresscard.services.card;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import trente.asia.addresscard.BR;

/**
 * Created by Windows 10 Gamer on 07/05/2017.
 */

public class Card extends BaseObservable{
    public String name, author;
    private int imageId;
    public boolean background;

    Card (String name, int imageId, String author) {
        this.name = name;
        this.author = author;
        this.imageId = imageId;
        this.background = false;
    }

    public int getImageId() {
        return imageId;
    }

    @BindingAdapter({"bind:imageId"})
    public static void loadImage(ImageView view, int imageId) {
        view.setImageResource(imageId);
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
