package eu.tesseractsoft.mintimedialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
         *
         * @param totalMinShownTime current total minimum showing time up to this point in time
         */
        void onMinTimeReached(long totalMinShownTime);
    }

    /**
     * Interface for debugging purposes - logging messages
     */
    public interface Logger {
        /**
         * Should log message
         *
         * @param msg message
         */
        void log(String msg);
    }

    // === Configuration option ===
    private int mMinShownTimeMs;
    private boolean mSilentDismiss;
    private boolean mAutoDismissAfterMinShownTime;
    private MinTimeReachedListener mMinTimeReachedListener;

    // === Internal fields ===
    private Handler mHandler;
    private boolean mDismissAlreadyRequested;
    private boolean mMinShowingTimeReached;
    private long mExtendMinTimeMs;
    private long mTotalMinShownTime;
    private boolean mIsShowCalled;
    private Logger mDebugLogger;


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
        mMinShowingTimeReached = false;
        mExtendMinTimeMs = 0;
        mTotalMinShownTime = 0;
        mIsShowCalled = false;
        mDebugLogger = new Logger() {
            @Override
            public void log(String msg) {
                // Empty default logger
            }
        };
        mDebugLogger.log("init() [mMinShownTimeMs=" + mMinShownTimeMs +
                ", mSilentDismiss=" + mSilentDismiss +
                ", mAutoDismissAfterMinShownTime=" + mAutoDismissAfterMinShownTime + "]");
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
     * Sets debug logger for the purpose of logging messages
     *
     * @param debugLogger logger
     */
    public void setDebugLogger(Logger debugLogger) {
        this.mDebugLogger = debugLogger;
    }

    /**
     * Returns information if minimum showing time was already reached
     *
     * @return true if dialog was already shown for minimum showing time + all extends
     */
    public boolean isMinTimeReached() {
        return mMinShowingTimeReached;
    }

    /**
     * Calling this method twice has no additional effect - second and more calls are ignored
     * {@inheritDoc}
     */
    @Override
    public void show() {
        mDebugLogger.log("show() [already shown=" + mIsShowCalled + "]");
        if (!mIsShowCalled) {// Prevent for calling multiple times
            mIsShowCalled = true;

            mDebugLogger.log("[mMinShownTimeMs = " + mMinShownTimeMs +
                    ", mMinTimeReachedListener is not null? = " + (mMinTimeReachedListener != null) +
                    ", mAutoDismissAfterMinShownTime=" + mAutoDismissAfterMinShownTime +
                    ", mSilentDismiss=" + mSilentDismiss + "]");
            if (mMinShownTimeMs > 0) {
                mDebugLogger.log("schedule timeout");
                mTotalMinShownTime = mMinShownTimeMs;
                mHandler.postDelayed(minShowingTimeTimeout, mMinShownTimeMs);
            } else {
                // No min showing time
                mMinShowingTimeReached = true;
                // Notify about min time reached straight away
                if (mMinTimeReachedListener != null) {
                    mDebugLogger.log("notify MinTimeReachedListener");
                    mMinTimeReachedListener.onMinTimeReached(mTotalMinShownTime);
                }
                // Auto dismiss if requested
                if (mAutoDismissAfterMinShownTime) {
                    mDebugLogger.log("dismissing...");
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void dismiss() {
        mDismissAlreadyRequested = true;
        mDebugLogger.log("dismiss() [mDismissAlreadyRequested=" + mDismissAlreadyRequested +
                ", mMinShowingTimeReached=" + mMinShowingTimeReached + "]");
        if (mMinShowingTimeReached) {
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
            mDebugLogger.log("setting OnDismissListener");
            super.setOnDismissListener(listener);
        } else {
            mDebugLogger.log("not setting OnDismissListener - silent dismiss requested");
        }
    }

    /**
     * Immediately dismiss the dialog ignoring minimum showing time
     */
    public synchronized void dismissForced() {
        mDebugLogger.log("forced dismissing");
        // Cancel scheduler
        mHandler.removeCallbacks(minShowingTimeTimeout);
        super.dismiss();
    }

    /**
     * Extends minimum showing time by specified amount. Total minimum showing time become
     * sum of original time and time specified here. Calling this method multiple time extend
     * time multiple times - additive. Calling this method before {@link #show()} has no effect
     *
     * @param extendTimeMs time in milliseconds, has to be greater than 0 (otherwise ignored)
     */
    public synchronized void extendMinShownTimeByMs(int extendTimeMs) {
        if (mIsShowCalled && extendTimeMs > 0) {
            mExtendMinTimeMs += extendTimeMs;
            mTotalMinShownTime += extendTimeMs;
        }
        mDebugLogger.log("extendMinShownTimeByMs() new values [mExtendMinTimeMs=" + mExtendMinTimeMs +
                ", mTotalMinShownTime=" + mTotalMinShownTime + "]");
    }

    // =========== Private methods ===============

    private synchronized void subtractFromExtendMinTime(long time) {
        mExtendMinTimeMs -= time;
    }

    private Runnable minShowingTimeTimeout = new Runnable() {
        @Override
        public void run() {

            if (mExtendMinTimeMs > 0) {
                mDebugLogger.log("min showing time was extended by " + mExtendMinTimeMs);
                // Someone extended minimum showing time, schedule again, and subtract time
                mHandler.postDelayed(minShowingTimeTimeout, mExtendMinTimeMs);
                subtractFromExtendMinTime(mExtendMinTimeMs);
                // do not continue this method
                return;
            }

            mMinShowingTimeReached = true;

            // Notify about min time reached
            if (mMinTimeReachedListener != null) {
                mDebugLogger.log("notify MinTimeReachedListener");
                mMinTimeReachedListener.onMinTimeReached(mTotalMinShownTime);
                if (mExtendMinTimeMs > 0) {
                    mDebugLogger.log("min showing time was extended by " + mExtendMinTimeMs);
                    // During onMinTimeReached someone extended min time, need schedule again
                    mMinShowingTimeReached = false;
                    mHandler.postDelayed(minShowingTimeTimeout, mExtendMinTimeMs);
                    subtractFromExtendMinTime(mExtendMinTimeMs);
                    // do not continue this method
                    return;
                }
            }
            mDebugLogger.log("min time reached [mDismissAlreadyRequested=" + mDismissAlreadyRequested +
                    ", mMinShowingTimeReached=" + mMinShowingTimeReached +
                    ", mAutoDismissAfterMinShownTime=" + mAutoDismissAfterMinShownTime + "]");
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

    /**
     * Simple builder method to create dialog with just message and minimum showing time.
     * Logs all internal messages
     *
     * @param context        context
     * @param message        message inside the standard dialog
     * @param minShownTimeMs minimum showing time
     * @return instance of MinTimeDialog
     */
    public static MinTimeDialog createMinTimeDialogDebug(Context context, String message, int minShownTimeMs) {
        MinTimeDialog dialog = new MinTimeDialog(context);
        dialog.setMessage(message);
        dialog.setMinShownTimeMs(minShownTimeMs);
        dialog.setDebugLogger(new Logger() {
            @Override
            public void log(String msg) {
                Log.d("MinTimeDialog", msg);
            }
        });
        return dialog;
    }
}
