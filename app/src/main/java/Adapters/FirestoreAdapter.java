package Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.us4u.R;

import Helpers.DatabaseHelper;
import Models.Food;

public class FirestoreAdapter extends FirestoreRecyclerAdapter<Food, FirestoreAdapter.FoodViewHolder> {
    private OnListItemClick onListItemClick;
    public DatabaseHelper dbHelper;

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<Food> options, OnListItemClick onListItemClick, Context context) {
        super(options);
        this.onListItemClick=onListItemClick;
        dbHelper=new DatabaseHelper(context);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food food) {
        Glide.with(holder.itemView).load(food.imageUrl).into(holder.Icon);
        if (food.getStatus().equalsIgnoreCase("Not Available")){
            holder.addBtn.setEnabled(false);
            holder.addBtn.setBackgroundColor(Color.parseColor("#BD9393"));
        }
        holder.Title.setText(food.getFoodname());
        //holder.Description.setText(food.getDescription());
        holder.Price.setText(""+food.getPrice()+"" );
        System.out.println("FOOD NAME: "+ food.getFoodname());

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res= dbHelper.getCartItem(food.getId());
                System.out.println(res.toString());
                if (res.getCount()==0){
                    boolean isAddedToCart= dbHelper.insertCartItem(food.getId(),food.getFoodname(),food.getImageUrl(),"1",""+food.price,""+food.getPrice());
                    if (isAddedToCart){
                        String message="Item Has Been Added To Cart";
                        Toast.makeText(v.getContext(), message,Toast.LENGTH_LONG).show();
                    }
                    else {
                        String message="Failed To Add Item To Cart";
                    }
                }
                else{
                    String message="Item Is Already Added";
                    Toast.makeText(v.getContext(), message,Toast.LENGTH_LONG).show();
                }
            }
        });



    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_template,parent,false);

        return new FoodViewHolder(view);
    }
    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView Icon;
        TextView Title;
        //TextView Description;
        TextView Price;
        Button addBtn;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            Icon= itemView.findViewById(R.id.image_view_food);
            Title= itemView.findViewById(R.id.text_view_food_name);
            //Description=itemView.findViewById(R.id.text_view_description_prod_detail);
            Price=itemView.findViewById(R.id.text_view_food_price);
            addBtn=itemView.findViewById(R.id.addButton);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemclick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
    public interface OnListItemClick{
        void onItemclick(Food foodItem, int position);
    }
}
