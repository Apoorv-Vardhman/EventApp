package com.apoorv.camera.presentation.dashboard.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apoorv.camera.R;
import com.apoorv.camera.app.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Apoorv Vardhman on 6/29/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class ImageViewHolder extends RecyclerView.Adapter<ImageViewHolder.ViewHolder> {
    private List<String> images;
    private Context context;

    public ImageViewHolder(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    public void setImages(String image)
    {
        images.add(image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(Constants.IMAGE_PATH+images.get(position)).placeholder(R.drawable.ic_baseline_camera).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
