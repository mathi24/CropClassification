package com.jsn.cropclassification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsn.cropclassification.model.Classifier;
import com.jsn.cropclassification.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class CropListOfflineAdapter extends BaseAdapter {

    Context context;
    private final List<Classifier.Recognition> details;

    public CropListOfflineAdapter(List<Classifier.Recognition> details, Context context){
        this.details = details;
        this.context = context;
    }

    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public Object getItem(int i) {
        return details.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null){
            LayoutInflater inflater;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item_crop_list_new,null);

            holder = new ViewHolder();
            holder.cropName = (TextView) view.findViewById(R.id.crop_name_new);
            holder.cropProbability = (TextView) view.findViewById(R.id.crop_probability_new);
            holder.relMain = (RelativeLayout) view.findViewById(R.id.rel_main);

            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();

        holder.cropName.setText(details.get(i).getTitle());

        float val = (details.get(i).getConfidence())*100;
//        holder.cropProbability.setText(String.valueOf(val));
       int b = (int)Math.round(val);
       holder.cropProbability.setText(String.valueOf(b));
//       holder.cropProbability.setText(String.format("%2f",b));


      /*  double d = val;,
        holder.cropProbability.setText(String.valueOf(d));

*/
        return view;
    }

    public static class ViewHolder{
        public TextView cropName;
        public TextView cropProbability;
        public ImageView imageRadio;
        RelativeLayout relMain;
    }
}
