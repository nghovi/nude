package trente.asia.calendar.commons.dialogs;

import java.util.Map;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 3/31/2017.
 */

//// TODO: 3/31/2017 duplicate code with CLOutboud dismiss dialog, how to
// refactor
public class CLOutboundDismissListDialog extends ChiaseListDialog {

    public CLOutboundDismissListDialog(Context context, String title,
                                       Map<String, String> map,
                                       ChiaseTextView txtItem, OnItemClicked
                                               itemClickListener) {
        super(context, title, map, txtItem, itemClickListener);
        Window window = this.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds
                (event)) {
            dismiss();
            return true;
        }
        return false;
    }

    private boolean isOutOfBounds(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        int marginLeftPx = WelfareUtil.dpToPx(40);
        int marginTopPx = WelfareUtil.dpToPx(120);
        final View decorView = getWindow().getDecorView();
        final int decorViewWidth = decorView.getWidth();
        final int decorViewHeight = decorView.getHeight();
        return x < marginLeftPx || x > decorViewWidth - marginLeftPx || y <
                marginTopPx || y > decorViewHeight - marginTopPx;
    }
}
