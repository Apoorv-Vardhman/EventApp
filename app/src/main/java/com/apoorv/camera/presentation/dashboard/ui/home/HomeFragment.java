package com.apoorv.camera.presentation.dashboard.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.apoorv.camera.data.response.Status;
import com.apoorv.camera.databinding.FragmentHomeBinding;
import com.apoorv.camera.presentation.main.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ImageViewHolder holder;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        holder = new ImageViewHolder(new ArrayList<>(),getContext());
        binding.eventRecycle.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.eventRecycle.setAdapter(holder);
        homeViewModel.setImageResponse();
        getResponse();
        View root = binding.getRoot();
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void getResponse() {
        homeViewModel.getImageResponse().observe(getViewLifecycleOwner(),imageDateByResponseApiResource -> {
            switch (imageDateByResponseApiResource.status)
            {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), imageDateByResponseApiResource.message, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), imageDateByResponseApiResource.data.getMessage(), Toast.LENGTH_SHORT).show();
                    if(imageDateByResponseApiResource.data.getData()!=null)
                    {
                        //Toast.makeText(getContext(), imageDateByResponseApiResource.data.getData().getImages(), Toast.LENGTH_SHORT).show();
                        String[] list = imageDateByResponseApiResource.data.getData().getImages().split(",");
                        /*List<String> list = new ArrayList<String>(Arrays.asList(imageDateByResponseApiResource.data.getData().getImages().split(" , ")));
                        holder.setImages(list);
                        holder.notifyDataSetChanged();*/
                        if(list.length==0)
                        {
                            binding.tvNoData.setVisibility(View.VISIBLE);
                            binding.camera.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            //List<String> fixedLenghtList = Arrays.asList(list);
                            for(int i=0;i<list.length;i++)
                            {
                                holder.setImages(list[i]);
                            }
                            holder.notifyDataSetChanged();
                            binding.camera.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                        binding.camera.setVisibility(View.VISIBLE);
                    }

                    break;

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}