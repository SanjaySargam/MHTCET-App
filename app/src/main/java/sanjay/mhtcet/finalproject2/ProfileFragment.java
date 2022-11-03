package sanjay.mhtcet.finalproject2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {
    private TextView profile_img_text,name,score,rank;
    private LinearLayout leaderboardB,profileB,bookmarkB;
    private LinearLayout logoutB;
    private BottomNavigationView bottomNavigationView;
    private Dialog progressDialog;
    private TextView dialogText;
    private InterstitialAd mInterstitialAd;
    private final String TAG = ProfileFragment.class.getSimpleName();


    public ProfileFragment() {
        // Required empty public constructor
        Log.d("ddf","df");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setAdss();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        progressDialog=new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading");


        String userName=DbQuery.myProfile.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0,1));

        name.setText(userName);
        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if (DbQuery.g_usersList.size()==0)
        {
            progressDialog.show();
            DbQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {


                    if (DbQuery.myPerformance.getScore()!=0)
                    {
                        if (!DbQuery.isMeOnTopList)
                        {
                            calculateRank();
                        }
                        score.setText("Score : "+ DbQuery.myPerformance.getScore());

                        rank.setText("Rank : "+DbQuery.myPerformance.getRank());


                    }
                    progressDialog.dismiss();

                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT);

                    progressDialog.dismiss();

                }
            });


        }
        else
        {
            score.setText(String.valueOf(DbQuery.myPerformance.getScore()));
            if (DbQuery.myPerformance.getScore()!=0)

                rank.setText(String.valueOf(DbQuery.myPerformance.getRank()));
        }


        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("1070116726465-r22unrq9kv012uukqaeiorfvq09futnf.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleClient= GoogleSignIn.getClient(getContext(),gso);
                mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent=new Intent(getContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });
        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.");
                            Intent intent = new Intent(getContext(), BookmarkActivity.class);
                            startActivity(intent);
                            mInterstitialAd=null;
                            setAdss();

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
                    Intent intent=new Intent(getContext(),BookmarkActivity.class);
                    startActivity(intent);

                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.");
                            Intent intent = new Intent(getContext(), MyProfileActivity.class);
                            startActivity(intent);
                            mInterstitialAd=null;
                            setAdss();

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
                    Intent intent=new Intent(getContext(),MyProfileActivity.class);
                    startActivity(intent);

                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });
        leaderboardB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.d("TAG", "The ad was dismissed.");
                            Intent intent = new Intent(getContext(), LeaderboardFragment.class);
                            startActivity(intent);
                            mInterstitialAd=null;
                            setAdss();

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
                    bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);

                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }


            }
        });


        return view;
    }
    private void initViews(View view)
    {
        logoutB=view.findViewById(R.id.logoutB);
        profile_img_text=view.findViewById(R.id.profile_img_text);
        name=view.findViewById(R.id.name);
        score=view.findViewById(R.id.total_score);
        rank=view.findViewById(R.id.rank);
        leaderboardB=view.findViewById(R.id.leaderBoardB);
        bookmarkB=view.findViewById(R.id.bookmarkB);
        profileB=view.findViewById(R.id.profileB);
        bottomNavigationView=getActivity().findViewById(R.id.bottom_nav_bar);
    }
    private void calculateRank()
    {
        int lowTopScore=DbQuery.g_usersList.get(DbQuery.g_usersList.size()-1).getScore();
        int remaining_slots=DbQuery.g_usersCount-100;
        int mySlot=(DbQuery.myPerformance.getScore()*remaining_slots)/lowTopScore;
        int rank;
        if (lowTopScore!=DbQuery.myPerformance.getScore()){
            rank=DbQuery.g_usersCount-mySlot;
        }
        else
        {
            rank=101;

        }
        DbQuery.myPerformance.setRank(rank);
    }
    public void setAdss(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),"ca-app-pub-6144020211629189/3935245776", adRequest,
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