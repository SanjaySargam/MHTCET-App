package sanjay.mhtcet.finalproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView app_name = findViewById(R.id.app_name);
        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist);
        app_name.setTypeface(typeface);


        Animation anim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        app_name.setAnimation(anim);

        mAuth=FirebaseAuth.getInstance();
        DbQuery.g_firestore=FirebaseFirestore.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null){

                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    });


                }
                else {
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        },2000);


    }
    }
