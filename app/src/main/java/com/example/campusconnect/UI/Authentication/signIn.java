


package com.example.campusconnect.UI.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;

import com.example.campusconnect.R;
import com.example.campusconnect.UI.MainPage.home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;


public class signIn extends AppCompatActivity {

    private EditText email, password;
    private KProgressHUD progressDialog;
    private TextView forgotpassword, registernow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        email.addTextChangedListener(emailWatcher);
        password.addTextChangedListener(passWatcher);

        registernow = findViewById(R.id.register_now);
        registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signIn.this, signUp.class));
            }
        });

        forgotpassword = findViewById(R.id.forgot_pass);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signIn.this, fogetPassword.class));
            }
        });

        Button button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = KProgressHUD.create(signIn.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false);

                progressDialog.show();

                String e = email.getText().toString();
                if (!(email.getText().toString().isEmpty() && password.getText().toString().isEmpty())) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(signIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(signIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        boolean emailVerified = user.isEmailVerified();
                                        if (emailVerified) {
                                            progressDialog.dismiss();
                                            Toast.makeText(signIn.this, "sign in Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(signIn.this, home.class));
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            FirebaseAuth.getInstance().signOut(); // Log Out
                                            Toast.makeText(signIn.this, "Email not Verify yet", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(signIn.this, "Our password or Email never be null", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //TextWatcher for Email
    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String check = s.toString();
            if (check.length() < 8 || check.length() > 40) {
                email.setError("Email Must consist of 8 to 40 characters");
            } else if (!check.matches("^[A-za-z0-9.@_]+")) {
                email.setError("Only . and _ and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                email.setError("Enter Valid Email");
            }
        }
    };

    //TextWatcher for Password
    TextWatcher passWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String check = s.toString();
            if (check.length() < 4 || check.length() > 20) {
                password.setError("Password Must consist of 4 to 20 characters");
            }
        }

    };
}
