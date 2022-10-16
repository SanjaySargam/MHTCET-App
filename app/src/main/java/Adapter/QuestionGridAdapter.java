package Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import sanjay.mhtcet.finalproject2.DbQuery;
import sanjay.mhtcet.finalproject2.QuestionsActivity;
import sanjay.mhtcet.finalproject2.R;

public class QuestionGridAdapter extends BaseAdapter {
    private final int numOfQues;
    private final Context context;

    public QuestionGridAdapter(Context context,int numOfQues) {
        this.numOfQues = numOfQues;
        this.context=context;
    }

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view ;
        if (convertView==null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_grid_item,parent,false);
        }
        else {
            view=convertView;
        }

        view.setOnClickListener(v -> {
            if (context instanceof QuestionsActivity)
                ((QuestionsActivity)context).goToQuestion(position);

        });

        TextView quesTV=view.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(position+1));

        Log.d("LOGGGGGGGGGGGG",String.valueOf(DbQuery.g_quesList.get(position).getStatus()));
        switch (DbQuery.g_quesList.get(position).getStatus())
        {
            case DbQuery.NOT_VISITED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.grey)));
                break;
            case DbQuery.UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.red)));
                break;

            case DbQuery.ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.green)));
                break;
            case DbQuery.REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.pink)));
                break;

            default:break;

        }

        return view;
    }
}
