package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import Others.Categoriesimages;
import app.moviebox.niku.moviebox.R;


/**
 * Created by Bir Al Sabia on 11/30/2016.
 */

public class Adapter_for_catgories extends RecyclerView.Adapter<Adapter_for_catgories.MyViewHolder>  {
    private final Context context;

    private ArrayList<Categoriesimages> arraycatimages;
    private Categoriesimages catimages;
    OnItemClickListener mItemClickListener;
    String baseimg_url="https://bkmalls.com/assets/images/";
    public Adapter_for_catgories(Context context, ArrayList<Categoriesimages> arraycat) {

        this.context = context;
        this.arraycatimages = arraycat;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
       // TextView tvcatogory;
        ImageView catimage;



        public MyViewHolder(View view) {
            super(view);
            catimage = (ImageView) view.findViewById(R.id.catimage);
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
                .inflate(R.layout.adapter_category, parent, false);

        return new Adapter_for_catgories.MyViewHolder(itemView);
    //    return null;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        catimages = new Categoriesimages();
        catimages = arraycatimages.get(position);

       // holder.tvcatogory.setText(professionals.Proname());
        //holder.tvsubject.setText(inboxdetails.Message());

        Glide.with(context).load(catimages.Catimage()).asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        holder.catimage.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return arraycatimages.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListeners) {
        this.mItemClickListener = mItemClickListeners;
    }
}
