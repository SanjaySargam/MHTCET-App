package sanjay.mhtcet.finalproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import Adapter.AnswersAdapter;
import Adapter.BookmarkAdapter;


public class AnswersActivity extends AppCompatActivity {
    private RecyclerView answersView;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        Toolbar toolbar = findViewById(R.id.aa_toolbar);
         answersView = findViewById(R.id.aa_test_recycler_view);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ANSWERS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new Dialog(AnswersActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading");
        progressDialog.show();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(layoutManager);
        DbQuery.loadBookmarks(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                AnswersAdapter adapter=new AnswersAdapter(DbQuery.g_quesList);
                answersView.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            AnswersActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}