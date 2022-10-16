package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sanjay.mhtcet.finalproject2.R;

import java.util.List;

import Models.QuestionModel;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    private List<QuestionModel> quesList;

    public BookmarkAdapter(List<QuestionModel> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item_layout,parent,false);

        return new BookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {

        String ques=quesList.get(position).getQuestion();
        String a=quesList.get(position).getOptionA();
        String b=quesList.get(position).getOptionB();
        String c=quesList.get(position).getOptionC();
        String d=quesList.get(position).getOptionD();

        int ans=quesList.get(position).getCorrectAns();

        holder.setData(position,ques,a,b,c,d,ans);

    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quesNo,questionA,optionA,optionB,optionC,optionD,result;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quesNo=itemView.findViewById(R.id.quesNo);
            questionA=itemView.findViewById(R.id.question);
            optionA=itemView.findViewById(R.id.optionA);
            optionB=itemView.findViewById(R.id.optionB);
            optionC=itemView.findViewById(R.id.optionC);
            optionD=itemView.findViewById(R.id.optionD);
            result=itemView.findViewById(R.id.correct);





        }
        private void setData(int pos,String question,String a,String b,String c,String d,int correctAns)
        {
            quesNo.setText("Question No. "+ (pos + 1));
            questionA.setText(question);
            optionA.setText("A. "+a);
            optionB.setText("B. "+b);
            optionC.setText("C. "+c);
            optionD.setText("D. "+d);

            if (correctAns==1)
            {
                result.setText("ANSWER : "+a);


            }
            else if (correctAns==2)
            {
                result.setText("ANSWER : "+b);
            }
            else if (correctAns==3)
            {
                result.setText("ANSWER : "+c);
            }
           else
            {
                result.setText("ANSWER : "+d);
            }



        }




    }
}
