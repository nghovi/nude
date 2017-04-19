package trente.asia.addresscard.services.card;

/**
 * Created by tien on 4/18/2017.
 */

public class CardModel {
    String date;
    int imageId;
    String updateBy;
    CardModel(String date, int imageId, String updateBy) {
        this.date = date;
        this.imageId = imageId;
        this.updateBy = updateBy;
    }
}
