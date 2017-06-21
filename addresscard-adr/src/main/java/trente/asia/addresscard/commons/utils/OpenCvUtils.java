package trente.asia.addresscard.commons.utils;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.commons.activities.CardContour;

/**
 * Created by tien on 5/30/2017.
 */

public class OpenCvUtils {
    public static CardContour findRectangle(Mat src) throws Exception {
        Mat blurred = src.clone();

//        Imgproc.medianBlur(src, blurred, 9);
        long startTime = System.currentTimeMillis();
        Imgproc.blur(src, blurred, new Size(9, 9));
        log("time = " + (System.currentTimeMillis() - startTime));

        Mat gray0 = new Mat(blurred.size(), CvType.CV_8U);
        Mat gray = new Mat();


        List<MatOfPoint> contours = new ArrayList<>();

        List<Mat> blurredChannel = new ArrayList<>();

        blurredChannel.add(blurred);

        List<Mat> gray0Channel = new ArrayList<>();
        gray0Channel.add(gray0);

        MatOfPoint2f approxCurve;

        double maxArea = 0;
        int maxId = -1;

        for(int c = 0; c < 3; c++){
            int ch[] = {c, 0};

            Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));

            int thresholdLevel = 1;
            for(int t = 0; t < thresholdLevel; t++){
                if(t == 0){
                    Imgproc.Canny(gray0, gray, 10, 20, 3, true); // true ?
                    Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1); // 1
                    // ?
                }else{
                    Imgproc.adaptiveThreshold(gray0, gray, thresholdLevel, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                            Imgproc.THRESH_BINARY, (src.width() + src.height()) / 200, t);
                }

                Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

//                log("size = " + contours.size());
                int num = 0;
                for(MatOfPoint contour : contours){
                    MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());

                    double area = Imgproc.contourArea(contour);

                    if (area < 50000) {
                        num++;
                        continue;
                    }
                    approxCurve = new MatOfPoint2f();
                    Imgproc.approxPolyDP(temp, approxCurve, Imgproc.arcLength(temp, true) * 0.02, true);
                    if(approxCurve.total() == 4 && area >= maxArea){
//						double maxCosine = 0;
//						List<Point> curves = approxCurve.toList();
//						for(int j = 2; j < 5; j++){
//							double cosine = Math.abs(angle(curves.get(j % 4), curves.get(j - 2), curves.get(j - 1)));
//							maxCosine = Math.max(maxCosine, cosine);
//						}
//
//						if(maxCosine < 0.3){
                        maxArea = area;
                        maxId = contours.indexOf(contour);

//						}
                    }
                }
//                log("maxarea = " + maxArea);
//                log("ignore = " + num);
            }
        }

        RotatedRect rect = new RotatedRect();
        if(maxId >= 0){

//			Rect rect = Imgproc.boundingRect(contours.get(maxId));
//			Imgproc.rectangle(source, rect.tl(), rect.br(), new Scalar(0, 255, 0), 8);
            MatOfPoint maxMatOfPoint = contours.get(maxId);
            MatOfPoint2f maxMatOfPoint2f = new MatOfPoint2f(maxMatOfPoint.toArray());

            rect = Imgproc.minAreaRect(maxMatOfPoint2f);

            Point points[] = new Point[4];
            rect.points(points);
            for (int i = 0; i < 4; ++i)
            {
                Imgproc.line(src, points[i], points[(i + 1) % 4], new Scalar(255, 0, 0), 2);
            }
        }
        return new CardContour(src, rect);
    }

    private static double angle(Point p1, Point p2, Point p0){
        double dx1 = p1.x - p0.x;
        double dy1 = p1.y - p0.y;
        double dx2 = p2.x - p0.x;
        double dy2 = p2.y - p0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }

    private static void log(String msg) {
        Log.e("Utils", msg);
    }
}
