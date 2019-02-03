package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ashish.artistsearh.ArtistDetails;
import com.ashish.artistsearh.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.ButterKnife;
import common.AppController;
import database.DBManager;
import interfaces.DetailsButtonClickListner;
import model.ArtistDetailsModel;
import model.ArtistModel;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class ListAdapter extends BaseAdapter {
    Activity act;
    ArrayList<ArtistDetailsModel> list;

    LayoutInflater inflater;
    DetailsButtonClickListner listner;
    AppController controller;
    DBManager manager;
    public ListAdapter(Activity act,  ArrayList<ArtistDetailsModel> list) {
        this.act = act;
        this.list = list;
        inflater= (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listner=(DetailsButtonClickListner)act;
        controller=(AppController)act.getApplicationContext();
        manager=controller.getDbManager();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        final ArtistDetailsModel model=list.get(i);
        if(view==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.artist_row, null);
            holder.artistImage=(ImageView)view.findViewById(R.id.artistImage);
            holder.bookmarked=(ImageView)view.findViewById(R.id.bookmark);
            holder.progress=(ProgressBar)view.findViewById(R.id.progress);
            holder.name=(TextView) view.findViewById(R.id.name);
           holder.url=(TextView) view.findViewById(R.id.url);
           holder.share=(ImageView)view.findViewById(R.id.share);
           holder.view=(Button)view.findViewById(R.id.view);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.view.setTypeface(controller.getFont());
         final ProgressBar bar=holder.progress;

        if ((model.getImage() != null)&&(model.getImage().length()>6)) {
            Picasso.with(act).load(model.getImage()).resize(80, 80).into(holder.artistImage, new Callback() {
                @Override
                public void onSuccess() {
                    bar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        } else {
            holder.artistImage.setImageResource(R.drawable.no_image);
            holder.progress.setVisibility(View.GONE);

        }
        final ImageView img=holder.bookmarked;
         holder.bookmarked.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 UpdateBookMark(model);
                 handleBookMark(model,img);
                 listner.onBookMarkClicked(model.getName());
                 notifyDataSetChanged();
             }
         });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onDetailsClick(model);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onShareClick(model);
            }
        });
        handleBookMark(model, holder.bookmarked);
        holder.name.setText(model.getName());
        holder.url.setText(model.getUrl());
        SpannableString content = new SpannableString(holder.url.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.url.setText(content);
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model.getUrl()));
                act.startActivity(i);
            }
        });
        view.setTag(holder);
        return view;
    }
    public class ViewHolder{
        ImageView artistImage,bookmarked,share;
       ProgressBar progress;
       TextView name;
       TextView url;
       Button view;

    }


    public void handleBookMark(ArtistDetailsModel model, ImageView img) {
        manager.open();
        if (manager.isBookmarked(model.getName(),model.getImage())) {
            img.setImageResource(R.drawable.bookmark);
        } else {
            img.setImageResource(R.drawable.unmark);
        }
        manager.close();
    }

    public void UpdateBookMark(ArtistDetailsModel model) {
        manager.open();
        if (manager.isBookmarked(model.getName(),model.getImage())) {
            manager.updateRecord(model, false);
        } else {
            manager.updateRecord(model, true);
        }
        manager.close();
    }

}
