package eu.tesseractsoft.mintimedialog.sample;

import android.view.View;

/**
 * Abstract model for items in Recycler View for the purpose of testing different scenarios
 * One item per test case
 * Created by Adam Kuszczak on 19/02/2017.
 */

abstract class TestingAdapterItemModel {

    /**
     * Interface for updating status
     */
    interface StatusUpdateListener {
        /**
         * Invoked when status should be updated
         *
         * @param statusText new status text
         */
        void onStatusUpdated(String statusText);
    }

    private StatusUpdateListener mListener;

    /**
     * Setter for status update listener
     *
     * @param listener listener
     */
    void setListener(StatusUpdateListener listener) {
        this.mListener = listener;
    }

    /**
     * Should return description of test case
     *
     * @return text with description
     */
    public abstract String getDescription();

    /**
     * Should return click listener to invoke action when user press the button
     *
     * @return button click listener
     */
    public abstract View.OnClickListener getOnClickListener();

    /**
     * Notify listener to update status text on this item
     *
     * @param statusText new status
     */
    void updateStatus(String statusText) {
        if (mListener != null) {
            mListener.onStatusUpdated(statusText);
        }
    }
}
