package com.apoorv.camera.presentation.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apoorv.camera.R;
import com.apoorv.camera.app.Permission;

import com.apoorv.camera.app.Utility;
import com.apoorv.camera.presentation.base.BaseActivity;
import com.apoorv.camera.presentation.dashboard.Dashboard;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity {
    private final  String TAG = MainActivity.class.getName();
    FloatingActionButton click;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private boolean isCameraPermission = false;
    private boolean isStoragePermission = false;
    private boolean isLocationPermission = false;
    private LocationRequest locationRequest;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 201;
    private String mCurrentPhotoPath = "";
    private ImageAdapter imageAdapter;
    private MaterialButton button;
    private File currentFile;
    private double latitude,longitude;
    private String address="";
    private MainViewModel viewModel;
    List<MultipartBody.Part> imageParts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);

        setTitle("Upload Images");
        viewInitialize();
        imageAdapter = new ImageAdapter(MainActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(imageAdapter);
        if(checkAndRequestPermissions())
        {
            getCurrentLocation();
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        /*camera clicked*/
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*check permission*/
                if(checkAndRequestPermissions())
                {
                    if(imageAdapter.getImages().size()<4)
                    {
                        getCurrentLocation();
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                            try {
                                Uri photoURI = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                            } catch (IOException e) {
                                Log.e("err",e.toString());
                                e.printStackTrace();
                                showToast("erro2");
                            }
                        }
                        else
                        {
                            showToast("err1");
                        }
                    }
                    else
                    {
                        showToast("Reached Maximum limit");
                    }


                }


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageAdapter.getImages().size()<4)
                {
                    showToast("Please capture minimum four images");
                }
                else if(imageAdapter.getImages().size()==4)
                {
                    RequestBody current_lat = RequestBody.create(String.valueOf(latitude), MediaType.parse("multipart/form-data"));
                    RequestBody current_long = RequestBody.create(String.valueOf(longitude), MediaType.parse("multipart/form-data"));
                    RequestBody address = RequestBody.create(String.valueOf(longitude), MediaType.parse("multipart/form-data"));
                    RequestBody current_date = RequestBody.create(Utility.getCurrentDate(), MediaType.parse("multipart/form-data"));
                    /*upload */
                    HashMap<String,RequestBody> param = new HashMap<>();
                    param.put("latitude",current_lat);
                    param.put("longitude",current_long);
                    param.put("address",address);
                    param.put("date",current_date);

                    MultipartBody.Part[] parts = new MultipartBody.Part[imageParts.size()];
                    for (int i=0;i<imageParts.size();i++) {
                        parts[i] = imageParts.get(i);
                    }


                    viewModel.setImageUploadResponse(parts,param);
                    subscribeResponse();

                }
                else
                {
                    showToast("Please capture maximum four images");
                }

            }
        });

    }

    private void subscribeResponse() {
        viewModel.getImageResponse().observe(this,response->{
            switch (response.status)
            {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    showToast(response.message);
                    break;
                case SUCCESS:
                    showToast(response.data.getMessage());
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    finish();
                    break;
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        currentFile = image;
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));

                if(mCurrentPhotoPath!=null && mImageBitmap !=null)
                {
                    RequestBody requestBody = RequestBody.create(currentFile, MediaType.parse("image/*"));
                    MultipartBody.Part part = MultipartBody.Part.createFormData("images[]",currentFile.getAbsoluteFile().getName(),requestBody);
                    imageParts.add(part);
                    imageAdapter.setImages(mCurrentPhotoPath,mImageBitmap);
                    imageAdapter.notifyDataSetChanged();
                }
                else
                {
                    showToast("path not found");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){
                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                        //showToast("latitude: "+latitude+"\n longitude: "+longitude);
                                        address = "Latitude: "+ latitude + "\n" + "Longitude: "+ longitude;
                                        //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        isCameraPermission = Permission.Check_CAMERA(this);
        isStoragePermission = Permission.Check_STORAGE(this);
        isLocationPermission = Permission.Check_FINE_LOCATION(this);

        if(!isCameraPermission)
        {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if(!isStoragePermission)
        {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!isLocationPermission)
        {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                    {
                        showToast("All accepted");
                        getCurrentLocation();
                    } else {
                         if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


    private void viewInitialize() {
        click = findViewById(R.id.floating);
        recyclerView = findViewById(R.id.recycler_view);
        button = findViewById(R.id.button_upload);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }
}