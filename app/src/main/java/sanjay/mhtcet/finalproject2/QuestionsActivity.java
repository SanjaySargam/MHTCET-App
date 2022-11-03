package sanjay.mhtcet.finalproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.concurrent.TimeUnit;

import Adapter.QuestionGridAdapter;
import Adapter.QuestionsAdapter;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesID, timerTV, catNameTV;
    private Button submitB, markB, clearSelectionB;
    private ImageButton prevQuesB, nextQuesB;
    private ImageView quesListB;
    private int quesID;
    QuestionsAdapter questionsAdapter;
    private DrawerLayout drawerLayout;
    private ImageButton drawerCloseBtn;
    private GridView quesListGV;
    private ImageView markImage;
    private QuestionGridAdapter gridAdapter;
    private CountDownTimer timer;
    private long timeLeft;
    private ImageView bookmarkBtn;
    private InterstitialAd mInterstitialAd;
    private final String TAG = QuestionsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);
        init();
        setSnapHelper();
        setClickListeners();
        startTimer();
        setAd();
        questionsAdapter = new QuestionsAdapter(DbQuery.g_quesList);
        questionsView.setAdapter(questionsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        gridAdapter = new QuestionGridAdapter(this, DbQuery.g_quesList.size());
        quesListGV.setAdapter(gridAdapter);


    }

    private void init() {
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        submitB = findViewById(R.id.submitBtn);
        markB = findViewById(R.id.markB);
        clearSelectionB = findViewById(R.id.clear_selectionB);
        quesListB = findViewById(R.id.ques_list_gridB);
        quesID = 0;
        tvQuesID.setText("1/" + String.valueOf(DbQuery.g_quesList.size()));
        catNameTV.setText(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerCloseBtn = findViewById(R.id.drawerCloseB);
        markImage = findViewById(R.id.mark_img);
        quesListGV = findViewById(R.id.ques_list_gv);
        bookmarkBtn = findViewById(R.id.qa_bookmark);
        DbQuery.g_quesList.get(0).setStatus(DbQuery.UNANSWERED);

        if (DbQuery.g_quesList.get(0).isBookmarked()) {
            bookmarkBtn.setImageResource(R.drawable.ic_bookmark_selected);
        } else {
            bookmarkBtn.setImageResource(R.drawable.ic_bookmark);
        }


    }

    private void setSnapHelper() {

        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                if (DbQuery.g_quesList.get(quesID).getStatus() == DbQuery.NOT_VISITED)
                    DbQuery.g_quesList.get(quesID).setStatus(DbQuery.UNANSWERED);

                if (DbQuery.g_quesList.get(quesID).getStatus() == DbQuery.REVIEW) {
                    markImage.setVisibility(View.VISIBLE);

                } else {
                    markImage.setVisibility(View.GONE);
                }


                tvQuesID.setText((quesID + 1) + "/" + DbQuery.g_quesList.size());

                if (DbQuery.g_quesList.get(quesID).isBookmarked()) {
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark_selected);
                } else {
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void setClickListeners() {


        clearSelectionB.setOnClickListener(v -> {
            DbQuery.g_quesList.get(quesID).setSelectedAnswer(-1);
            DbQuery.g_quesList.get(quesID).setStatus(DbQuery.UNANSWERED);
            markImage.setVisibility(View.GONE);

            questionsAdapter.notifyDataSetChanged();
        });
        quesListB.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                gridAdapter.notifyDataSetChanged();
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        drawerCloseBtn.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END);

        });
        markB.setOnClickListener(v -> {
            if (markImage.getVisibility() != View.VISIBLE) {
                markImage.setVisibility(View.VISIBLE);
                DbQuery.g_quesList.get(quesID).setStatus(DbQuery.REVIEW);
            } else {
                markImage.setVisibility(View.GONE);
                if (DbQuery.g_quesList.get(quesID).getSelectedAnswer() != -1) {
                    DbQuery.g_quesList.get(quesID).setStatus(DbQuery.ANSWERED);

                } else {
                    DbQuery.g_quesList.get(quesID).setStatus(DbQuery.UNANSWERED);
                }
            }
        });
        submitB.setOnClickListener(v -> submitTest()

        );
        bookmarkBtn.setOnClickListener(v -> addToBookmark());


    }

    private void submitTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        Button cancelB = view.findViewById(R.id.cancelB);
        Button confirmB = view.findViewById(R.id.confirmB);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        cancelB.setOnClickListener(v -> alertDialog.dismiss());
        confirmB.setOnClickListener(v -> {
            timer.cancel();
            alertDialog.dismiss();
            if (mInterstitialAd != null) {
                mInterstitialAd.show(QuestionsActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                        startActivity(intent);
                        QuestionsActivity.this.finish();
                        mInterstitialAd=null;
                        setAd();

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
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                long totalTime = DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime() * 60 * 1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }





        });
        alertDialog.show();
    }

    public void goToQuestion(int position) {
        questionsView.smoothScrollToPosition(position);

        if (drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);

    }

    private void startTimer() {
        long total_time = DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime() * 60 * 1000;

        timer = new CountDownTimer(total_time + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {

                timeLeft = remainingTime;

                String time = String.format("%02d : %02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))

                );
                timerTV.setText(time);

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                long totalTime = DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime() * 60 * 1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();


            }
        };
        timer.start();

    }

    private void addToBookmark() {
        if (DbQuery.g_quesList.get(quesID).isBookmarked()) {
            DbQuery.g_quesList.get(quesID).setBookmarked(false);
            bookmarkBtn.setImageResource(R.drawable.ic_bookmark);
            Toast.makeText(QuestionsActivity.this, "Removed from Saved Questions", Toast.LENGTH_SHORT);

        } else {
            DbQuery.g_quesList.get(quesID).setBookmarked(true);
            bookmarkBtn.setImageResource(R.drawable.ic_bookmark_selected);
            Toast.makeText(QuestionsActivity.this, "Added to Saved Questions", Toast.LENGTH_SHORT);

        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        TextView content = view.findViewById(R.id.content);
        content.setText("Do You Really want to Exit Test ?");
        TextView title = view.findViewById(R.id.title);
        title.setText("Exit Test");
        Button cancelB = view.findViewById(R.id.cancelB);
        Button confirmB = view.findViewById(R.id.confirmB);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        cancelB.setOnClickListener(v -> alertDialog.dismiss());
        confirmB.setOnClickListener(v -> {
            alertDialog.dismiss();
            QuestionsActivity.this.finish();
        });
        alertDialog.show();
    }
    public void setAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-6144020211629189/4987584946", adRequest,
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

