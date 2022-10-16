package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sanjay.mhtcet.finalproject2.R;

import java.util.List;

import Models.RankModel;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private final List<RankModel> userList;

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout,parent,false);




        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
            String name=userList.get(position).getName();
            int score=userList.get(position).getScore();
            int rank=userList.get(position).getRank();

            holder.setData(name,score,rank);
    }

    @Override
    public int getItemCount() {
        return Math.min(userList.size(), 10);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTV,rankTV,scoreTV,imgTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV=itemView.findViewById(R.id.name);
            rankTV=itemView.findViewById(R.id.rankTV);
            scoreTV=itemView.findViewById(R.id.scoreTV);
            imgTV=itemView.findViewById(R.id.img_text);
        }
private void setData(String name,int score, int rank)
{
    nameTV.setText(name);
    scoreTV.setText("Score : "+score);
    rankTV.setText("Rank - "+rank);
    imgTV.setText(name.toUpperCase().substring(0,1));


}



    }
}
