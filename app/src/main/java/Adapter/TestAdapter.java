package Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sanjay.mhtcet.finalproject2.DbQuery;
import sanjay.mhtcet.finalproject2.R;
import sanjay.mhtcet.finalproject2.StartTestActivity;

import java.util.List;

import Models.TestModel;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private final List<TestModel> testList;

    public TestAdapter(List<TestModel> testList) {
        this.testList = testList;
    }


    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        int progress=testList.get(position).getTopScore();
        holder.setData(position,progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView testName;
        private final TextView topScore;
        private final ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                topScore=itemView.findViewById(R.id.scoreText);
                progressBar=itemView.findViewById(R.id.testProgressbar);
                testName=itemView.findViewById(R.id.testName);




        }

        private void setData(int pos,int progress)
            {

                testName.setText(testList.get(pos).getTestName());
                topScore.setText(progress +"%");
                progressBar.setProgress(progress);

                itemView.setOnClickListener(v -> {
                    DbQuery.g_selected_test_index=pos;
                            Intent intent =new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                });
            }
    }
}
