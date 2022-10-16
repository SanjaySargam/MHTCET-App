package sanjay.mhtcet.finalproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.Objects;

public class MyProfileActivity extends AppCompatActivity {
    private EditText name, email, phone;
    private TextView profileText;
    private LinearLayout button_layout;
    private String nameStr, phoneStr;
    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.mp_name);
        email = findViewById(R.id.mp_email);
        phone = findViewById(R.id.mp_phone);
        profileText = findViewById(R.id.profile_text);
        LinearLayout editB = findViewById(R.id.editBtn);
        Button cancelB = findViewById(R.id.cancelBtn);
        Button saveB = findViewById(R.id.saveBtn);
        button_layout = findViewById(R.id.button_layout);

        progressDialog = new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Updating Data..");


        disableEditing();

        editB.setOnClickListener(v -> enableEditing());
        cancelB.setOnClickListener(v -> disableEditing());
        saveB.setOnClickListener(v -> {
            if (validate()) {
                saveData();

            }
        });

    }

    private void disableEditing() {
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        button_layout.setVisibility(View.GONE);

        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());
        phone.setText(null);

        if (DbQuery.myProfile.getPhone() != null)
            phone.setText(DbQuery.myProfile.getPhone());
        String profileName = DbQuery.myProfile.getName();

        profileText.setText(profileName.toUpperCase().substring(0, 1));


    }

    private void enableEditing() {
        name.setEnabled(true);
        //email.setEnabled(true);
        phone.setEnabled(true);
        button_layout.setVisibility(View.VISIBLE);

    }

    private boolean validate() {
        nameStr = name.getText().toString();
        phoneStr = phone.getText().toString();

        if (nameStr.isEmpty()) {
            name.setError("Name cannot be empty");
            return false;
        }
        if (!phoneStr.isEmpty()) {
            if (!((phoneStr.length() == 10) && (TextUtils.isDigitsOnly(phoneStr)))) {
                phone.setError("Enter Valid Phone Number");
                return false;

            }
        }


        return true;
    }

    private void saveData() {
        progressDialog.show();
        if (phoneStr.isEmpty())
            phoneStr = null;
        DbQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                disableEditing();
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            MyProfileActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}