package com.example.kelly.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelly.Dashboard;
import com.example.kelly.R;
import com.example.kelly.databinding.FragmentHomeBinding;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements LocationListener {

    private  View TODO ;
    private FragmentHomeBinding binding;
    String message;
    Button sosButton, safeButton;
    TextView countDownTimerView;
    CountDownTimer countDownTimer;


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    String url = "http://192.168.43.185/kelly-hospital/public/api/login.php";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        message = "May day, May day ,May day ......";
        sosButton = root.findViewById(R.id.sosButton);
        countDownTimerView = root.findViewById(R.id.countDownTimer);
        safeButton = root.findViewById(R.id.safeButton);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//




        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callSOS();

                countDownTimerView.setVisibility(View.VISIBLE);
                safeButton.setVisibility(View.VISIBLE);

                countDownTimer = new CountDownTimer(40000,2000) {
                    @Override
                    public void onTick(long l) {
                        countDownTimerView.setText("Count Down to send a alert to Ambulance "+((l/1000)%60)+"");
                        sosButton.setText(((l/1000)%60)+"");
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();

                safeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        countDownTimer.cancel();
                    }
                });

            }
        });






        return root;
    }

    public void Login(View view) {


        if (!true) {

        } else {

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait...");

            progressDialog.show();




            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();

                    if (response.equalsIgnoreCase("success")) {



                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


                    params.put("latitude", "value");
                    params.put("longitude", "value");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getContext(),"Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    void getMessage(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }



    }

    void callSOS(){


        try {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+263776137289"));

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        0);
            }

            startActivity(callIntent);


        }catch (Exception e){
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }

        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phoneNumber,null)));
    }



        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}