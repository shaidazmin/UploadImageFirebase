package com.example.uploadimagefirebase;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    Context context;
    List<Upload> uploadList;

    public MyAdapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    // method ........

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
      View view =   layoutInflater.inflate(R.layout.item_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        Upload upload = uploadList.get(position);

        // set text View ......

        holder.textView.setText(upload.getComment());

        // set Image View ...

        Picasso.with(context)
                .load(upload.getImageUri())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }


    // View holder Class


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
          itemView.findViewById(R.id.textViewId);
          itemView.findViewById(R.id.imageViewId);
        }
    }

}
