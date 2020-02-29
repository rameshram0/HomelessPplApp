package com.example.homelesspplapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.ViewHolder> {

    private Context context;
    private List<Sponsorslist> list;
    private  OnNoteListener onNoteListener;

    public SponsorsAdapter(Context context, List<Sponsorslist> list,OnNoteListener onNoteListener) {
        this.context = context;
        this.list = list;
        this.onNoteListener=onNoteListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sponsorslist, parent, false);
        return new ViewHolder(v,onNoteListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sponsorslist movie = list.get(position);

        holder.name.setText(movie.getName());
        holder.occupation.setText(movie.getOccupation());
        holder.email.setText(movie.getEmail());






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,occupation,email;
        OnNoteListener onNoteListener;


        public ViewHolder(final View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);


            occupation = itemView.findViewById(R.id.occupation);

            email = itemView.findViewById(R.id.email);



            this.onNoteListener = onNoteListener;



        }

        @Override
        public void onClick(View v) {


            switch (v.getId()) {
               /* case R.id.like:
                    onNoteListener.onLike(this.getLayoutPosition());
                    break;*/
                case R.id.comment:
                    onNoteListener.onComment(this.getLayoutPosition());
                    break;
                default:
                    break;
            }

        }
    }
    public  interface OnNoteListener{
        /*void onNoteclick(int position);
        void comment(View v, int position);*/
        void onLike(int p);
        void onComment(int p);
    }


}
