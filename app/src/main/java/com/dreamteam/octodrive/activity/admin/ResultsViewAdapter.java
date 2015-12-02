package com.dreamteam.octodrive.activity.admin;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.model.Result;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ResultsViewAdapter extends ArrayAdapter<Result> {

    private List<Result> objects;
    private final Context context;

    public ResultsViewAdapter(Context context, List<Result> objects) {
        super(context, R.layout.results_item, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.results_item, parent, false);

        TextView tvDate = (TextView)rowView.findViewById(R.id.textView_date);
        TextView tvScore = (TextView)rowView.findViewById(R.id.textView_score);
        TextView tvType = (TextView)rowView.findViewById(R.id.textView_type);

        Result result = objects.get(position);
        Date resultDate = result.date();
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
        tvDate.setText(dateFormatter.format(resultDate));

        tvScore.setText(Integer.toString(result.score()));

        if (result.isLive()) {
            tvType.setText(context.getString(R.string.result_type_live));
        }
        else {
            tvDate.setText(context.getString(R.string.result_type_practice));
        }
        
        return rowView;
    }
}
