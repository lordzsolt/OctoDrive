package com.dreamteam.octodrive.activity.admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.model.User;

import java.util.List;

public class ListItemViewAdapter extends RecyclerView.Adapter<ListItemViewAdapter.ViewHolder> {

    private final List<? extends Listable> mValues;

    public ListItemViewAdapter(List<? extends Listable> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mValue = mValues.get(position);
        holder.mNameView.setText(holder.mValue.displayText());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public Listable mValue;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView)view.findViewById(R.id.textView_name);
        }
    }
}
