package com.example.restadmin;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<ModelClass> modelClassList;
    private OnItemClickListener listener;

    public MyAdapter(Context context, List<ModelClass> modelClassList) {
        this.context = context;
        this.modelClassList = modelClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view= layoutInflater.inflate(R.layout.burger_item_layout,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelClass modelClass = modelClassList.get(position);
        holder.FoodName.setText("Name: "+modelClass.getFoodName());
        holder.FoodPrice.setText("Price: "+modelClass.getFoodPrice());
        holder.FoodDescription.setText("Details: "+modelClass.getFoodDescription());

        Picasso.with(context)
                .load(modelClass.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(holder.FoodImage);

    }

    @Override
    public int getItemCount() {

        return modelClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView FoodName, FoodPrice, FoodDescription;
        ImageView FoodImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            FoodName=itemView.findViewById(R.id.CardViewFoodName_ID);
            FoodPrice=itemView.findViewById(R.id.CardViewFoodPrice_ID);
            FoodDescription=itemView.findViewById(R.id.CardViewFoodDescription_ID);
            FoodImage=itemView.findViewById(R.id.CardImageView_ID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null)
                    {
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION)
                        {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });


            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Choose in action");
                    MenuItem doAnyTask= menu.add(Menu.NONE, 1,1, "update");
                    MenuItem delete= menu.add(Menu.NONE, 2,2, "delete");

                    //do Any Task............................................................................................
                    doAnyTask.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //write code
                            {
                                int position = getAdapterPosition();
                                if (position!=RecyclerView.NO_POSITION)
                                {
                                    listener.onDoAnyTask(position);
                                    return true;

                                }
                            }

                            return false;
                        }
                    });

                    //delete........................................................................................
                    delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //write code
                            {
                                int position = getAdapterPosition();
                                if (position!=RecyclerView.NO_POSITION)
                                {
                                    listener.onDelete(position);
                                    return true;
                                }
                            }

                            return false;
                        }
                    });








                }
            });




        }
    }

//for item view
    public interface OnItemClickListener{
        void OnItemClick(int position);
       void onDoAnyTask(int position);
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;

    }
}
