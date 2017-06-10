package sample.getdatafromserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sample.getdatafromserver.R;
import sample.getdatafromserver.model.ContactsItem;

/**
 * Created by Dell on 06-06-2017.
 */

public class ConAdapter extends RecyclerView.Adapter<ConAdapter.MyHolder> {

    ArrayList<ContactsItem> conList;
    Context context;

    public ConAdapter(ArrayList<ContactsItem> conList, Context context) {
        this.conList = conList;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View v=layoutInflater.inflate(R.layout.row_contact_activity,parent,false);
        MyHolder mh=new MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.txtname.setText(conList.get(position).getName());
        holder.txtEmail.setText(conList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return conList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtname,txtEmail;
        public MyHolder(View itemView) {

            super(itemView);
            txtname= (TextView) itemView.findViewById(R.id.txtName);
            txtEmail= (TextView) itemView.findViewById(R.id.txtEmail);
        }
    }
}
