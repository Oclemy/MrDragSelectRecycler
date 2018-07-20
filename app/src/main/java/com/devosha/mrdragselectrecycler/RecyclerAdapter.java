package com.devosha.mrdragselectrecycler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.afollestad.dragselectrecyclerview.IDragSelectAdapter;
import java.util.ArrayList;
import java.util.List;

/*
RecyclerAdapter class. Also contains our ViewHolder class. Also implements dragselectrecyclerview.IDragSelectAdapter
 */
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MainViewHolder> implements IDragSelectAdapter {

    /*
    interface to define for us several callbacks
     */
    interface Listener {
        void onClick(int index);
        void onLongClick(int index);
        void onSelectionChanged(int count);
    }
    private final Listener callback;

    //this will hold all the selected indices.
    private final List<Integer> selectedIndices;

    private static final String[] ALPHABET =("Atlantis Casini Spitzer Chandra Galileo Kepler Wise Apollo Saturn-5 Hubble Challenger" +
            " James-Web Huygens Enterprise New-Horizon Opportunity Pioneer Curiosity Spirit Orion Mars-Explorer WMAP Columbia Voyager Juno").split(" ");

    private static final int[] COLORS =
            new int[] {
                    Color.parseColor("#F44336"),
                    Color.parseColor("#E91E63"),
                    Color.parseColor("#9C27B0"),
                    Color.parseColor("#673AB7"),
                    Color.parseColor("#3F51B5"),
                    Color.parseColor("#2196F3"),
                    Color.parseColor("#03A9F4"),
                    Color.parseColor("#00BCD4"),
                    Color.parseColor("#009688"),
                    Color.parseColor("#4CAF50"),
                    Color.parseColor("#8BC34A"),
                    Color.parseColor("#CDDC39"),
                    Color.parseColor("#FFEB3B"),
                    Color.parseColor("#FFC107"),
                    Color.parseColor("#FF9800"),
                    Color.parseColor("#FF5722"),
                    Color.parseColor("#795548"),
                    Color.parseColor("#9E9E9E"),
                    Color.parseColor("#607D8B"),
                    Color.parseColor("#F44336"),
                    Color.parseColor("#E91E63"),
                    Color.parseColor("#9C27B0"),
                    Color.parseColor("#673AB7"),
                    Color.parseColor("#3F51B5"),
                    Color.parseColor("#2196F3"),
                    Color.parseColor("#03A9F4")
            };



    /*
    RecyclerAdapter constructor
     */
    RecyclerAdapter(Listener callback) {
        super();
        this.selectedIndices = new ArrayList<>(16);
        this.callback = callback;
    }

    /*
    Return a single recyclerview item
     */
    String getItem(int index) {
        return ALPHABET[index];
    }

    /*
    Return all selected indices
     */
    List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    /*
    Toggle selection status of a DragSelectRecyclerview item
     */
    void toggleSelected(int index) {
        if (selectedIndices.contains(index)) {
            selectedIndices.remove((Integer) index);
        } else {
            selectedIndices.add(index);
        }
        notifyItemChanged(index);
        if (callback != null) {
            callback.onSelectionChanged(selectedIndices.size());
        }
    }

    /*
    Clear selected DragSelectRecyclerView items
     */
    void clearSelected() {
        if (selectedIndices.isEmpty()) {
            return;
        }
        selectedIndices.clear();
        notifyDataSetChanged();
        if (callback != null) {
            callback.onSelectionChanged(0);
        }
    }

    /*
    Inflate our griditem_main.xml
     */
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_main, parent, false);
        return new MainViewHolder(v, callback);
    }

    /*
    OnBindViewHolder method - bind data to our RecyclerView items.
     */
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.label.setText(getItem(position));

        final Drawable d;
        final Context c = holder.itemView.getContext();

        if (selectedIndices.contains(position)) {
            d = new ColorDrawable(ContextCompat.getColor(c, R.color.grid_foreground_selected));
            holder.label.setTextColor(ContextCompat.getColor(c, R.color.grid_label_text_selected));
        } else {
            d = null;
            holder.label.setTextColor(ContextCompat.getColor(c, R.color.grid_label_text_normal));
        }

        //noinspection RedundantCast
        ((FrameLayout) holder.colorSquare).setForeground(d);
        holder.colorSquare.setBackgroundColor(COLORS[position]);
    }

    /*
    Add selected items in a List and notify the adapter of the selection. Remove the
    unselected item from the list.
     */
    @Override
    public void setSelected(int index, boolean selected) {
        Log.d("MainAdapter", "setSelected(" + index + ", " + selected + ")");

        if (!selected) {
            selectedIndices.remove((Integer) index);
        } else if (!selectedIndices.contains(index)) {
            selectedIndices.add(index);
        }
        notifyItemChanged(index);
        //raise a callback
        if (callback != null) {
            callback.onSelectionChanged(selectedIndices.size());
        }
    }

    /*
    Whether current item can be selected.
     */
    @Override
    public boolean isIndexSelectable(int index) {
        return true;
    }

    /*
    Return number of items in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return ALPHABET.length;
    }

    /*********************** VIEW HOLDER CLASS*****************************/
    static class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView label;
        final RectangleView colorSquare;
        private final Listener callback;

        MainViewHolder(View itemView, Listener callback) {
            super(itemView);
            this.callback = callback;
            this.label = itemView.findViewById(R.id.label);
            this.colorSquare = itemView.findViewById(R.id.colorSquare);
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        /*
        Handle RecyclerView Item Click
         */
        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onClick(getAdapterPosition());
            }
        }

        /*
        Handle RecyclerView LongClick
         */
        @Override
        public boolean onLongClick(View v) {
            if (callback != null) {
                callback.onLongClick(getAdapterPosition());
            }
            return true;
        }
    }
    /*****************************END VIEWHOLDER ********************************/
}
