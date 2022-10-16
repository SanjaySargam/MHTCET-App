package sanjay.mhtcet.finalproject2;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import sanjay.mhtcet.finalproject2.R;

import Adapter.RankAdapter;


public class LeaderboardFragment extends Fragment {
    private TextView totalUsersTV,myTextImgTV,myScoreTV,myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private Dialog progressDialog;


    public LeaderboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");


        initViews(view);


        progressDialog=new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading");
        progressDialog.show();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter=new RankAdapter(DbQuery.g_usersList);
        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if (DbQuery.myPerformance.getScore()!=0)
                {
                    if (!DbQuery.isMeOnTopList)
                    {
                        calculateRank();
                    }
                    myScoreTV.setText("Score : "+ DbQuery.myPerformance.getScore());
                    myRankTV.setText("Rank : "+DbQuery.myPerformance.getRank());
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT);

                progressDialog.dismiss();

            }
        });


        totalUsersTV.setText("Total Users : "+DbQuery.g_usersCount);
        myTextImgTV.setText(DbQuery.myPerformance.getName().toUpperCase().substring(0,1));

        return view;
    }

    private void initViews(View view)
    {
        totalUsersTV=view.findViewById(R.id.total_users);
        myTextImgTV=view.findViewById(R.id.img_text);
        myScoreTV=view.findViewById(R.id.totalScore);
        myRankTV=view.findViewById(R.id.rankL);
        usersView=view.findViewById(R.id.users_view);

    }
    private void calculateRank()
    {
        int lowTopScore=DbQuery.g_usersList.get(DbQuery.g_usersList.size()-1).getScore();
        int remaining_slots=DbQuery.g_usersCount-10;
        int mySlot=(DbQuery.myPerformance.getScore()*remaining_slots)/lowTopScore;
        int rank;
        if (lowTopScore!=DbQuery.myPerformance.getScore()){
            rank=DbQuery.g_usersCount - mySlot;
        }
        else
        {
            rank=11;

        }
        DbQuery.myPerformance.setRank(rank);
    }
}