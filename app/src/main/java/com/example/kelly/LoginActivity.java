package com.example.kelly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    EditText ed_email, ed_password;
    String str_email, str_password;
    String url = "http://192.168.43.185/kelly-hospital/public/api/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getParent(),
                    new String[]{Manifest.permission.INTERNET},
                    0);
        }

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
    }

    public void fakeLogin(View view){
        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(intent);
    }



    public void Login(View view) {


        if (ed_email.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (ed_password.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");

            progressDialog.show();
            str_email = ed_email.getText().toString().trim();
            str_password = ed_password.getText().toString().trim();



            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();

                    if (response.equalsIgnoreCase("success")) {


                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        ed_email.setText("");
                        ed_password.setText("");
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


                    params.put("email", str_email);
                    params.put("password", str_password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);

        }
    }

}