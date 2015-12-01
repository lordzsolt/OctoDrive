package com.dreamteam.octodrive.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.User;
import com.parse.ParseUser;

import java.util.List;

public class ListItemViewAdapter extends RecyclerView.Adapter<ListItemViewAdapter.ViewHolder> {

    private final List<? extends Listable> mValues;

    Context mContext;

    public ListItemViewAdapter(Context context, List<? extends Listable> items) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listable item = mValues.get(position);
        holder.mValue = item;
        holder.mNameView.setText(holder.mValue.displayText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (item instanceof User) {
                    intent = new Intent(mContext, UserDetailsActivity.class);
                    User user = (User)item;
                    intent.putExtra(KeyConstants.kKEY_CONSTANT_ADMIN_OBJECT_ID, user.objectId());
                }
                else if (item instanceof Question) {
                    intent = new Intent(mContext, QuestionDetailsActivity.class);
                    Question question = (Question)item;
                    intent.putExtra(KeyConstants.kKEY_CONSTANT_ADMIN_OBJECT_ID, question.objectId());
                }
                mContext.startActivity(intent);
            }
        });
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
