package Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import Models.CategoryModel;
import sanjay.mhtcet.finalproject2.DbQuery;
import sanjay.mhtcet.finalproject2.R;
import sanjay.mhtcet.finalproject2.TestActivity;

public class CategoryAdapter extends BaseAdapter {
    private final List<CategoryModel> cat_List;
    public CategoryAdapter(List<CategoryModel> cat_list) {
        cat_List = cat_list;
    }


    @Override
    public int getCount() {
        return cat_List.size();
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
        View myView;


        if (convertView==null){
                    myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout,parent,false);
        }
        else {
            myView=convertView;
        }

        myView.setOnClickListener(v -> {
            DbQuery.g_selected_cat_index =position;
            Intent intent =new Intent(v.getContext(), TestActivity.class);
            v.getContext().startActivity(intent);
        });

        TextView catName=myView.findViewById(R.id.catName);

        catName.setText(cat_List.get(position).getName());






return myView;
    }

}
