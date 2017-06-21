package trente.asia.addresscard.commons.activities;

import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;

/**
 * Created by tien on 6/2/2017.
 */

public class CardContour {
    public Mat contourMat;
    public RotatedRect rect;

    public CardContour(Mat contourMat, RotatedRect rect) {
        this.contourMat = contourMat;
        this.rect = rect;
    }
}
