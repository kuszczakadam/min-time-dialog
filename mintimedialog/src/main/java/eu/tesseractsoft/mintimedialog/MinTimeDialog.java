package eu.tesseractsoft.mintimedialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Progress Dialog with Minimum Showing Time. It assures that dialog will be shown for
 * at least specified amount of time. It also offers silent dismiss, without firing dismiss listener
 * <p>
 * <p>
 * Created by Adam Kuszczak on 11/02/2017.
 */

public class MinTimeDialog extends ProgressDialog {

    /**
     * Interface to notify about minimum time reached
     */
    public interface MinTimeReachedListener {
        /**
         * Fired when minimum showing time was reached
         */
        void onMinTimeReached();
    }

    // === Configuration option ===
    private int mMinShownTimeMs;
    private boolean mSilentDismiss;
    private boolean mAutoDismissAfterMinShownTime;
    private MinTimeReachedListener mMinTimeReachedListener;

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
        mAutoDismissAfterMinShownTime = false;

        mHandler = new Handler();
        mDismissAlreadyRequested = false;
        mMinShowingTimeAchieved = false;
    }

    /**
     * Setter for specifying minimum showing time. Has to be set before {@link #show()}
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
     * Option to automatically dismiss dialog when minimum showing time is reached
     *
     * @param autoDismissAfterMinShownTime true if dialog should be dismissed automatically after minShownTimeMs
     */
    public void setAutoDismissAfterMinShownTime(boolean autoDismissAfterMinShownTime) {
        this.mAutoDismissAfterMinShownTime = autoDismissAfterMinShownTime;
    }

    /**
     * Setter to provide minimum showing time reached listener. It is called before {@link #dismiss()}
     *
     * @param minTimeReachedListener listener
     */
    public void setMinTimeReachedListener(MinTimeReachedListener minTimeReachedListener) {
        this.mMinTimeReachedListener = minTimeReachedListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (mMinShownTimeMs > 0) {
            mHandler.postDelayed(minShowingTimeTimeout, mMinShownTimeMs);
        } else {
            // No min showing time
            mMinShowingTimeAchieved = true;
            if (mMinTimeReachedListener != null) {
                mMinTimeReachedListener.onMinTimeReached();
            }
            if (mAutoDismissAfterMinShownTime) {
                // FIX, need to delay a bit otherwise no effect
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissForced();
                    }
                }, 50);

            }
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
            // Min time already reached, so dismiss now
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
            if (mMinTimeReachedListener != null) {
                mMinTimeReachedListener.onMinTimeReached();
            }
            if (mDismissAlreadyRequested || mAutoDismissAfterMinShownTime) {
                // dismiss was requested before min time, so dismiss now
                // or when auto dismiss set
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
