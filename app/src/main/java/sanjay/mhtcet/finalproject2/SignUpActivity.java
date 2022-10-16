package sanjay.mhtcet.finalproject2;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sanjay.mhtcet.finalproject2.R;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
private EditText name,email,pass,confirmPass;
private Button signUp;
private ImageView backB;
private FirebaseAuth mAuth;
private String emailStr,passStr,confirmPassStr,nameStr;
private Dialog progressDialog;
private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.username);
        email=findViewById(R.id.emailID);
        pass=findViewById(R.id.password);
        confirmPass=findViewById(R.id.confirm_pass);
        signUp=findViewById(R.id.signUp_btn);
        backB=findViewById(R.id.backBtn);
        dialogText=findViewById(R.id.dialog_text);
        mAuth=FirebaseAuth.getInstance();

        progressDialog=new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering User...");

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    signupNewUser();
                }


            }
        });
    ;
    }
    private boolean validate(){
        nameStr=name.getText().toString().trim();
        passStr=pass.getText().toString().trim();
        confirmPassStr=confirmPass.getText().toString().trim();
        emailStr=email.getText().toString().trim();

        if (nameStr.isEmpty()){
            name.setError("Enter Your Name");
            return false;
        }
        if (emailStr.isEmpty()){
            email.setError("Enter EmailID");
            return false;
        }

        if (passStr.isEmpty()){
            pass.setError("Enter Password");
            return false;
        }
        if (confirmPassStr.isEmpty()){
            confirmPass.setError("Enter Password");
            return false;
        }
        if (passStr.compareTo(confirmPassStr)!=0){
            Toast.makeText(SignUpActivity.this,"Password and Confirm Password must be same !",Toast.LENGTH_SHORT).show();
            return false;
        }



    return true;
    }
    private void signupNewUser(){
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(SignUpActivity.this,"Sign Up Successfull",Toast.LENGTH_SHORT).show();
                        DbQuery.createUserData(emailStr,nameStr,new MyCompleteListener(){

                            @Override
                            public void onSuccess() {

                                DbQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        SignUpActivity.this.finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });


                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });


                    }
                    else {
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }
}