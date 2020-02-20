package com.example.campusconnect.UI.MainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.fogetPassword;
import com.example.campusconnect.UI.Authentication.signIn;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn_logout=findViewById(R.id.logout_button);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, signIn.class));

            }
        });



    }
}
