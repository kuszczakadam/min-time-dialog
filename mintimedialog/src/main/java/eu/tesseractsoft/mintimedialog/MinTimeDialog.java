package eu.tesseractsoft.mintimedialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Adam Kuszczak on 11/02/2017.
 */

public class MinTimeDialog extends ProgressDialog {

    // === Configuration option ===
    private int mMinShownTimeMs;
    private boolean mSilentDismiss;

    // === Internal flags ===
    private Handler mHandler;
    private boolean mDismissAlreadyRequested;
    private boolean mMinShowingTimeAchieved;


    public MinTimeDialog(Context context) {
        super(context);
        init();
    }

    public MinTimeDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init(){
        mMinShownTimeMs = 0;
        mSilentDismiss = false;

        mHandler = new Handler();
        mDismissAlreadyRequested = false;
        mMinShowingTimeAchieved = false;
    }

    public void setMinShownTimeMs(int minShownTimeMs) {
        this.mMinShownTimeMs = minShownTimeMs;
    }

    public void setSilentDismiss(boolean silentDismiss) {
        this.mSilentDismiss = silentDismiss;
    }

    @Override
    public void show() {
        mHandler.postDelayed(minShowingTimeTimeout, mMinShownTimeMs);
        super.show();
    }

    @Override
    public void dismiss() {
        mDismissAlreadyRequested = true;
        if(mMinShowingTimeAchieved) {
            super.dismiss();
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        if(!mSilentDismiss) {
            super.setOnDismissListener(listener);
        }
    }

    public void dismissForced(){
        mHandler.removeCallbacks(minShowingTimeTimeout);
        super.dismiss();
    }

    Runnable minShowingTimeTimeout = new Runnable() {
        @Override
        public void run() {
            mMinShowingTimeAchieved = true;
            if(mDismissAlreadyRequested){
                MinTimeDialog.super.dismiss();
            }
        }
    };



    public static MinTimeDialog createMinTimeDialog(Context context, String message, int minShownTimeMs){
        MinTimeDialog dialog = new MinTimeDialog(context);
        dialog.setMessage(message);
        dialog.setMinShownTimeMs(minShownTimeMs);
        return dialog;
    }
}
