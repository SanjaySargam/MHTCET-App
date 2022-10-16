package Adapter;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sanjay.mhtcet.finalproject2.DbQuery;
import sanjay.mhtcet.finalproject2.R;

import java.util.List;

import Models.QuestionModel;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private final List<QuestionModel> questionList;

    public QuestionsAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView ques;
        private final Button optionA;
        private final Button optionB;
        private final Button optionC;
        private final Button optionD;
        private Button prevSelectedBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ques=itemView.findViewById(R.id.tv_queston);
            optionA=itemView.findViewById(R.id.optionA);
            optionB=itemView.findViewById(R.id.optionB);
            optionC=itemView.findViewById(R.id.optionC);
            optionD=itemView.findViewById(R.id.optionD);
            prevSelectedBtn= null ;

            ques.setMovementMethod(new ScrollingMovementMethod());

        }
        private void setData(final int pos){
            ques.setText(questionList.get(pos).getQuestion());
            optionA.setText(questionList.get(pos).getOptionA());
            optionB.setText(questionList.get(pos).getOptionB());
            optionC.setText(questionList.get(pos).getOptionC());
            optionD.setText(questionList.get(pos).getOptionD());

            setOption(optionA,1,pos);
            setOption(optionB,2,pos);
            setOption(optionC,3,pos);
            setOption(optionD,4,pos);


            optionA.setOnClickListener(v -> selectOption(optionA,1,pos));
            optionB.setOnClickListener(v -> selectOption(optionB,2,pos));
            optionC.setOnClickListener(v -> selectOption(optionC,3,pos));
            optionD.setOnClickListener(v -> selectOption(optionD,4,pos));


        }
        private void selectOption(Button btn,int optionNum,int quesID)
        {
            if (prevSelectedBtn==null)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(quesID).setSelectedAnswer(optionNum);
                changeStatus(quesID,DbQuery.ANSWERED);
                prevSelectedBtn=btn;

            }
            else
                {
                    if (prevSelectedBtn.getId()==btn.getId())
                    {
                        btn.setBackgroundResource(R.drawable.unselected_btn);
                        DbQuery.g_quesList.get(quesID).setSelectedAnswer(-1);
                        changeStatus(quesID,DbQuery.UNANSWERED);
                        prevSelectedBtn=null;

                    }
                    else
                    {
                        prevSelectedBtn.setBackgroundResource(R.drawable.unselected_btn);
                        btn.setBackgroundResource(R.drawable.selected_btn);
                        DbQuery.g_quesList.get(quesID).setSelectedAnswer(optionNum);
                        changeStatus(quesID,DbQuery.ANSWERED);
                        prevSelectedBtn=btn;
                    }

            }


        }
        private void changeStatus(int id,int status)
        {
            if (DbQuery.g_quesList.get(id).getStatus()!=DbQuery.REVIEW)
            {
                DbQuery.g_quesList.get(id).setStatus(status);

            }
        }


        private void setOption(Button btn, int optionNum,int quesID)
        {
            if (DbQuery.g_quesList.get(quesID).getSelectedAnswer()==optionNum){
                btn.setBackgroundResource(R.drawable.selected_btn);

            }
            else
            {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }

        }

    }

}
