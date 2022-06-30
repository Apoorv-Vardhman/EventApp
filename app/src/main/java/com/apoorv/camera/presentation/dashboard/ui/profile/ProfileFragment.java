package com.apoorv.camera.presentation.dashboard.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apoorv.camera.app.Constants;
import com.apoorv.camera.data.local.PrefConnect;
import com.apoorv.camera.databinding.FragmentDashboardBinding;
import com.apoorv.camera.databinding.FragmentProfileBinding;
import com.apoorv.camera.presentation.login.LoginActivity;

/**
 * Created by Apoorv Vardhman on 6/29/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        String name = PrefConnect.readString(getContext(), Constants.NAME,"");
        String email = PrefConnect.readString(getContext(),Constants.EMAIL,"");
        binding.tvName.setText(name);
        binding.tvEmail.setText(email);
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefConnect.clearAllPrefs(getContext());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
