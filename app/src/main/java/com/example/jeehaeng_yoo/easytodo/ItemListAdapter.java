package com.example.jeehaeng_yoo.easytodo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JeeHaeng_Yoo on 12/29/2017.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>  {
    private LayoutInflater mInflater;
    private Context mContext;
    ItemListOpenHelper mDB;

    public ItemListAdapter(Context context, ItemListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public ItemListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.todo_list_item, parent, false);
        return new ItemViewHolder(mItemView, this);    }

    /**
     * Sets the contents of an item at a given position in the RecyclerView.
     * Called by RecyclerView to display the data at a specificed position.
     *
     * @param holder The view holder for that position in the RecyclerView.
     * @param position The position of the item in the RecycerView.
     */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // Retrieve the data for that position.
//        String mCurrent = mItemList.get(position);
        // Add the data to the view holder.
        ToDoItem current = mDB.query(position);
        System.out.println(current.getDate());
        holder.itemItemView.setText(current.getName() +" "+ current.getDate() +" "+ current.getTime() +" "+ current.getDetail());
    }

    @Override
    public int getItemCount() {
        return mDB.dbCount();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView itemItemView;
        final ItemListAdapter mAdapter;

        public ItemViewHolder(View itemView, ItemListAdapter mAdapter) {
            super(itemView);
            this.itemItemView = (TextView) itemView.findViewById(R.id.word);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // All we do here is prepend "Clicked! " to the text in the view, to verify that
            // the correct item was clicked. The underlying data does not change.
            itemItemView.setText (itemItemView.getText() + " Clicked!");
        }
    }
}
