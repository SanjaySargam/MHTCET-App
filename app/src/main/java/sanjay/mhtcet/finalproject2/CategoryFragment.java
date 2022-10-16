package sanjay.mhtcet.finalproject2;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import Adapter.CategoryAdapter;


public class CategoryFragment extends Fragment {
        private Dialog progressDialog;
    public CategoryFragment() {
    }
    private GridView catView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_category, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("MHT-CET");


        progressDialog=new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading");
        progressDialog.show();

        catView=view.findViewById(R.id.cat_grid);

       DbQuery.loadData(new MyCompleteListener() {
           @Override
           public void onSuccess() {
               progressDialog.dismiss();
               CategoryAdapter adapter=new CategoryAdapter(DbQuery.g_catList);
               catView.setAdapter(adapter);
           }

           @Override
           public void onFailure() {
                    progressDialog.dismiss();
               Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
               
           }
       });



        return view;
    }



}