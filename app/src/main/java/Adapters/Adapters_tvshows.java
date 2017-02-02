package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import Others.Tvshows;
import app.moviebox.niku.moviebox.R;

/**
 * Created by Bir Al Sabia on 12/22/2016.
 */

public class Adapters_tvshows extends RecyclerView.Adapter<Adapters_tvshows.MyViewHolder>  {
    private final Context context;

    private ArrayList<Tvshows> arraytvshows;
    private Tvshows Tvshows;
    Adapter_for_catgories.OnItemClickListener mItemClickListener;
    String baseimg_url="https://bkmalls.com/assets/images/";

    public Adapters_tvshows(Context context, ArrayList<Tvshows> arraytv) {

        this.context = context;
        this.arraytvshows = arraytv;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        // TextView tvcatogory;
        ImageView catimage;
        TextView Tvtitle;
        TextView Tvrating;
        TextView Tvnext;
        TextView Tvdate;
        public CardView cardView;
        LinearLayout holderimage;


        public MyViewHolder(View view) {
            super(view);
            catimage = (ImageView) view.findViewById(R.id.catimage);
            Tvtitle= (TextView) view.findViewById(R.id.texttitle);
            Tvrating= (TextView) view.findViewById(R.id.textrating);
            Tvnext= (TextView) view.findViewById(R.id.textnext);
            Tvdate= (TextView) view.findViewById(R.id.textdate);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view,getPosition());
            }
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tvshowslayout, parent, false);

        return new Adapters_tvshows.MyViewHolder(itemView);
        //    return null;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Tvshows = new Tvshows();
        Tvshows = arraytvshows.get(position);

        Glide.with(context).load(Tvshows.Cattvimage()).asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        holder.catimage.setImageBitmap(bitmap);
                    }
                });


       holder.Tvtitle.setText(Tvshows.Cattvtitle());
        holder.Tvrating.setText(Tvshows.Cattvrating());
        holder.Tvnext.setText(Tvshows.Cattvnext());
        holder.Tvdate.setText(Tvshows.Cattvdate());


    }



    @Override
    public int getItemCount() {
        return arraytvshows.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final Adapter_for_catgories.OnItemClickListener mItemClickListeners) {
        this.mItemClickListener = mItemClickListeners;
    }
}

