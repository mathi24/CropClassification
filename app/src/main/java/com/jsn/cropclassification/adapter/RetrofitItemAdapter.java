package com.jsn.cropclassification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jsn.cropclassification.model.CropDetails;
import com.jsn.cropclassification.R;
import com.jsn.cropclassification.activity.CameraActivity;

import java.util.List;

public class RetrofitItemAdapter extends BaseAdapter {

    Context context;
    CameraActivity cameraActivity;
    private List<CropDetails.Crops> mCropDetails;
    boolean isChecked= false;
 /*   RecyclerView.ViewHolder vh = null;*/

    public RetrofitItemAdapter(List<CropDetails.Crops> mCropDetails, Context context,CameraActivity cameraActivity){
        this.mCropDetails = mCropDetails;
        this.context = context;
        this.cameraActivity = cameraActivity;
    }

    public class ViewHolder{
        public TextView cropName;
        public TextView cropProbability;
        public ImageView imageRadio;
        RelativeLayout relMain;
    }
    @Override
    public int getCount() {
        return mCropDetails.size();
    }

    @Override
    public Object getItem(int i) {
        return mCropDetails.get(i);
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

            view = inflater.inflate(R.layout.list_item_crop_list,null);

            holder = new ViewHolder();
            holder.cropName = (TextView) view.findViewById(R.id.crop_name);
            holder.cropProbability = (TextView) view.findViewById(R.id.probability);
            holder.imageRadio = (ImageView) view.findViewById(R.id.image_radio);
            holder.relMain = (RelativeLayout) view.findViewById(R.id.rel_main);

            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();
          holder.cropName.setText(mCropDetails.get(i).getCropName());
          holder.cropProbability.setText(mCropDetails.get(i).getCropProbability());

        holder.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isChecked){
                    isChecked = true;
                    holder.imageRadio.setImageResource(R.mipmap.ic_radio_checked);
                    cameraActivity.selectedCropFromList(mCropDetails.get(i).getCropName(),mCropDetails.get(i).getCropProbability());
                }else{
                    isChecked = false;
                    holder.imageRadio.setImageResource(R.mipmap.ic_radio_unchecked);
                    cameraActivity.selectedCropFromList("","");
                }
            }
        });

          return view;

    }

}
