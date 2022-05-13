package com.jsn.cropclassification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsn.cropclassification.model.CropDetails;
import com.jsn.cropclassification.R;

import java.util.List;

public class CropListAdapter extends BaseAdapter {

    Context context;
    private List<CropDetails.Crops> details;
    boolean isChecked= false;
    /*   RecyclerView.ViewHolder vh = null;*/

    public CropListAdapter(List<CropDetails.Crops> details, Context context){
        this.details = details;
        this.context = context;
    }

    public class ViewHolder{
        public TextView cropName;
        public TextView cropProbability;
//        public ImageView imageRadio;
        RelativeLayout relMain;
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

        View view1 = view;
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
            holder.cropName.setText(details.get(i).getCropName());
            holder.cropProbability.setText(details.get(i).getCropProbability());

        return view;

    }

}
