package com.aptitude.challengetwo.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.challengetwo.R;
import com.aptitude.challengetwo.datamodel.PostModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private List<PostModel> postModelList = new ArrayList<>();
    private Context context;

    public void addItems(List<PostModel> postModel) {
        this.postModelList = postModel;
        notifyDataSetChanged();
    }

    public ProductsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, viewGroup, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostModel postModel = postModelList.get(position);
        holder.title.setText(postModel.getTitle());
        holder.desc.setText(postModel.getDescription());
        holder.price.setText(postModel.getCost().toString());
        holder.imageView.setImageBitmap(StringToBitMap(postModel.getImageString()));
    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.questNo.setText(selectedPosition + 1 + " ");
//    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, desc, price;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}