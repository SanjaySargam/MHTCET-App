package sanjay.mhtcet.finalproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.concurrent.TimeUnit;

import Models.QuestionModel;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTV, timeTV, totalTV, correctQTV, wrongQTV, unattemptedQTV, dialogText;
    private Button leaderBoardB, reAttemptB, viewAnsB;
    private long timeTaken;
    private Dialog progressDialog;
    private int finalScore;
    private InterstitialAd mInterstitialAd;
    private final String TAG = ScoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        setAdd();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading");
        progressDialog.show();



        init();
        loadData();
        setBookmarks();


        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(ScoreActivity.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.");
                            Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                            startActivity(intent);
                            mInterstitialAd=null;
                            setAdd();

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.d("TAG", "The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });


                } else {
                    Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                    startActivity(intent);
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }



            }
        });

        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt();
            }
        });

        leaderBoardB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        saveResult();

    }



    private void init() {
        scoreTV = findViewById(R.id.score);
        totalTV = findViewById(R.id.totalQ);
        correctQTV = findViewById(R.id.correctQ);
        wrongQTV = findViewById(R.id.wrongQ);
        unattemptedQTV = findViewById(R.id.un_attemptedQ);
        leaderBoardB = findViewById(R.id.leaderB);
        reAttemptB = findViewById(R.id.reAttemptB);
        viewAnsB = findViewById(R.id.viewAnsB);
        timeTV = findViewById(R.id.time_taken);

    }

    private void loadData() {
        int correctQ = 0, wrongQ = 0, unAttemptedQ = 0;
        for (int i = 0; i < DbQuery.g_quesList.size(); i++) {
            if (DbQuery.g_quesList.get(i).getSelectedAnswer() == -1) {
                unAttemptedQ++;
            } else {
                if (DbQuery.g_quesList.get(i).getSelectedAnswer() == DbQuery.g_quesList.get(i).getCorrectAns()) {
                    correctQ++;
                } else {
                    wrongQ++;
                }
            }
        }
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unAttemptedQ));

        totalTV.setText(String.valueOf(DbQuery.g_quesList.size()));

        finalScore = (correctQ * 100) / DbQuery.g_quesList.size();
        scoreTV.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);

        String time = String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))

        );
        timeTV.setText(time);


    }

    private void reAttempt() {
        for (int i = 0; i < DbQuery.g_quesList.size(); i++) {
            DbQuery.g_quesList.get(i).setSelectedAnswer(-1);
            DbQuery.g_quesList.get(i).setStatus(DbQuery.NOT_VISITED);

        }
        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong", Toast.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });
    }

    private void setBookmarks() {
        for (int i = 0; i < DbQuery.g_quesList.size(); i++) {
            QuestionModel question = DbQuery.g_quesList.get(i);
            if (question.isBookmarked()) {
                if (!DbQuery.g_bmIdList.contains(question.getqID())) {
                    DbQuery.g_bmIdList.add(question.getqID());
                    DbQuery.myProfile.setBookmarkCount(DbQuery.g_bmIdList.size());
                }

            } else {
                if (DbQuery.g_bmIdList.contains(question.getqID())) {
                    DbQuery.g_bmIdList.remove(question.getqID());
                    DbQuery.myProfile.setBookmarkCount(DbQuery.g_bmIdList.size());

                }

            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            ScoreActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void setAdd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-6144020211629189/3482931580", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }


}