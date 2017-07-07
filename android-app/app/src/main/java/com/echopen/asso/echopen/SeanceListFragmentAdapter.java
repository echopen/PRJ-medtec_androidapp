package com.echopen.asso.echopen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echopen.asso.echopen.model.Seance;

import java.util.ArrayList;

/**
 * Created by gary on 07/07/2017.
 */

public class SeanceListFragmentAdapter extends RecyclerView.Adapter<SeanceListFragmentAdapter.ViewHolder> {
    ArrayList<Seance> mArrayseance;
    private SeanceListItemClicked mlistener;

    public SeanceListFragmentAdapter(ArrayList<Seance> arrayseance, SeanceListItemClicked listener) {
        mArrayseance = arrayseance;
        mlistener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.seance_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.Name.setText(mArrayseance.get(position).getName());
        holder.Description.setText(mArrayseance.get(position).getDescription());
        holder.rl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mlistener.onSeanceClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayseance.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl;
        private TextView Name;
        private TextView Description;

        public ViewHolder(View itemView) {
            super(itemView);

            rl = (RelativeLayout)itemView;
            Name = (TextView) itemView.findViewById(R.id.name);
            Description = (TextView) itemView.findViewById(R.id.description);
        }

    }

    public interface SeanceListItemClicked{
        public void onSeanceClicked(int position);
    }
}
