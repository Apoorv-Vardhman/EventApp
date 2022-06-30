package com.apoorv.camera.presentation.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apoorv.camera.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> images = new ArrayList<>();
    private List<Bitmap> imageBitMap = new ArrayList<>();
    private Context context;

    public ImageAdapter(Context context)
    {
        this.context = context;
    }

    public void setImages(String path,Bitmap bitMap)
    {
        images.add(path);
        imageBitMap.add(bitMap);
    }

    public List<String> getImages()
    {
        return images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageBitmap(imageBitMap.get(position));
    }

    @Override
    public int getItemCount() {
        return imageBitMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
