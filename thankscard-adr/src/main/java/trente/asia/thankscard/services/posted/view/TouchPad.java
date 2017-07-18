package trente.asia.thankscard.services.posted.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tien on 7/17/2017.
 */

public class TouchPad extends View {
    private OnTouchPadListener callback;
    private static final int NONE = 0, MOVE = 1, SCALE = 2, CLICK = 3;

    public TouchPad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(onTouchListener);
    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        float oldX, oldY;
        int mode = NONE;
        float oldDistance;
        float slop = 10;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (callback == null) {
                return true;
            }
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    oldX = event.getX();
                    oldY = event.getY();
                    mode = CLICK;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDistance = pointersDistance(event);
                    mode = SCALE;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    callback.onTouchScaleEnd();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == SCALE) {
                        callback.onTouchScale(pointersDistance(event) / oldDistance);
                        break;
                    }

                    if (Math.abs(event.getX() - oldX) > slop && Math.abs(event.getY() - oldY) > slop && mode != NONE) {
                        mode = MOVE;
                    }

                    if (mode == MOVE) {
                        callback.onTouchMove(event.getX() - oldX, event.getY() - oldY);
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mode == CLICK) {
                        callback.onTouchPadClick();
                    }
                    break;
            }
            return true;
        }
    };

    private float pointersDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public void setCallback(OnTouchPadListener callback) {
        this.callback = callback;
    }

    public interface OnTouchPadListener {
        void onTouchMove(float moveX, float moveY);
        void onTouchScale(float scale);
        void onTouchScaleEnd();
        void onTouchPadClick();
    }

    private void log(String msg) {
        Log.e("TouchPad", msg);
    }
}
