package com.example.homelesspplapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.ViewHolder> {

    private Context context;
    private List<Shoplist> list;
    private  OnNoteListener onNoteListener;

    public NewsfeedAdapter(Context context, List<Shoplist> list,OnNoteListener onNoteListener) {
        this.context = context;
        this.list = list;
        this.onNoteListener=onNoteListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_newslist, parent, false);
        return new ViewHolder(v,onNoteListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shoplist movie = list.get(position);

        holder.title.setText(movie.getTitle());


        Glide.with(context).load(movie.getImage()).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;

        // Button btn;

        public ImageView imageView, comment;

        LikeButton like;
        TextView onelike;
        OnNoteListener onNoteListener;


        public ViewHolder(final View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);


            imageView = itemView.findViewById(R.id.image);


            like = itemView.findViewById(R.id.like);

            like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if (likeButton.isLiked()) {
                        likeButton.setEnabled(true);


                    }else{
                        likeButton.setEnabled(false);

                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
               // likeButton.setEnabled(false);
                    if (!likeButton.isLiked()) {
                        likeButton.setEnabled(true);
                    }else{
                        likeButton.setEnabled(false);
                    }
                }
            });
           // like.setOnClickListener(this);
            comment = itemView.findViewById(R.id.comment);


            // btn=itemView.findViewById(R.id.btn);

            this.onNoteListener = onNoteListener;


            comment.setOnClickListener(this);
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
