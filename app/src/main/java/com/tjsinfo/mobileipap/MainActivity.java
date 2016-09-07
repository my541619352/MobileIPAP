package com.tjsinfo.mobileipap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tjsinfo.mobileipap.entity.User;
import com.tjsinfo.mobileipap.util.VolleyManager;

public class MainActivity extends AppCompatActivity {

    private TextView hellotxt;
    private Button tologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hellotxt = (TextView) findViewById(R.id.hello);
        tologin = (Button) findViewById(R.id.toLogin);
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleyManager.newInstance().jacksonPostRequest("test", "http://www.mocky.io/v2/57cfc114260000f71a64ff91", User.class, "{\"req\":\"test\"}", new Response.Listener<User>() {
                    @Override
                    public void onResponse(User response) {
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        hellotxt.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
            }
        });
    }
}
