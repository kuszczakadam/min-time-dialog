package eu.tesseractsoft.mintimedialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Progress Dialog with Minimum Showing Time. It assures that dialog will be shown for
 * at least specified amount of time. It also offers silent dismiss, without firing dismiss listener
 *
 *
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


    /**
     * Simple constructor
     *
     * @param context context
     */
    public MinTimeDialog(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor with theme
     *
     * @param context context
     * @param theme   theme resource
     */
    public MinTimeDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        mMinShownTimeMs = 0;
        mSilentDismiss = false;

        mHandler = new Handler();
        mDismissAlreadyRequested = false;
        mMinShowingTimeAchieved = false;
    }

    /**
     * Setter for specifying minimum showing time
     *
     * @param minShownTimeMs minimum showing time in milliseconds, default 0
     */
    public void setMinShownTimeMs(int minShownTimeMs) {
        this.mMinShownTimeMs = minShownTimeMs;
    }

    /**
     * Option to enable silent dismiss
     *
     * @param silentDismiss true if silent dismiss should be enabled, default false
     */
    public void setSilentDismiss(boolean silentDismiss) {
        this.mSilentDismiss = silentDismiss;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (mMinShownTimeMs > 0) {
            mHandler.postDelayed(minShowingTimeTimeout, mMinShownTimeMs);
        }
        super.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void dismiss() {
        mDismissAlreadyRequested = true;
        if (mMinShowingTimeAchieved) {
            super.dismiss();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        if (!mSilentDismiss) {
            super.setOnDismissListener(listener);
        }
    }

    /**
     * Immediately dismiss the dialog ignoring minimum showing time
     */
    public synchronized void dismissForced() {
        mHandler.removeCallbacks(minShowingTimeTimeout);
        super.dismiss();
    }

    private Runnable minShowingTimeTimeout = new Runnable() {
        @Override
        public void run() {
            mMinShowingTimeAchieved = true;
            if (mDismissAlreadyRequested) {
                dismissForced();
            }
        }
    };


    // =========== Static builder methods ===============

    /**
     * Simple builder method to create dialog with just message and minimum showing time
     *
     * @param context        context
     * @param message        message inside the standard dialog
     * @param minShownTimeMs minimum showing time
     * @return instance of MinTimeDialog
     */
    public static MinTimeDialog createMinTimeDialog(Context context, String message, int minShownTimeMs) {
        MinTimeDialog dialog = new MinTimeDialog(context);
        dialog.setMessage(message);
        dialog.setMinShownTimeMs(minShownTimeMs);
        return dialog;
    }
}
