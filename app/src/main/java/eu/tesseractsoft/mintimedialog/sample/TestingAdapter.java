package eu.tesseractsoft.mintimedialog.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for RecyclerView for testing scenarios.
 * Items contain simple button and description
 * Created by Adam Kuszczak on 19/02/2017.
 */

class TestingAdapter extends RecyclerView.Adapter<TestingAdapter.ViewHolder> {

    private TestingAdapterItemModel[] mDataset;

    /**
     * Holder with button and text
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtLabel)
        TextView txtLabel;

        @BindView(R.id.txtStatus)
        TextView txtStatus;

        @BindView(R.id.btnShow)
        Button btnShow;

        /**
         * Constructor
         * Binds with ButterKnife
         *
         * @param v view to bind
         */
        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        /**
         * Populate item. Binds button and change text on label
         *
         * @param item test case item
         */
        void populate(TestingAdapterItemModel item) {
            txtLabel.setText(item.getDescription());
            btnShow.setOnClickListener(item.getOnClickListener());
            // Set update status listener
            item.setListener(new TestingAdapterItemModel.StatusUpdateListener() {
                @Override
                public void onStatusUpdated(String statusText) {
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText(statusText);
                }
            });
        }

    }

    /**
     * Constructor
     *
     * @param mDataset list of test cases
     */
    TestingAdapter(TestingAdapterItemModel[] mDataset) {
        this.mDataset = mDataset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_testing, parent, false);
        return new ViewHolder(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populate(mDataset[position]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
