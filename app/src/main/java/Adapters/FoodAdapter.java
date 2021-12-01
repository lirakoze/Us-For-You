package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.us4u.R;

import java.util.ArrayList;

import Models.Category;
import Models.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private FoodAdapter.OnFoodItemClickListener onFoodItemClickListener;
    private ArrayList<Food> foods;
    public FoodAdapter(ArrayList<Food> starters, FoodAdapter.OnFoodItemClickListener onFoodItemClickListener){
        this.foods = starters;
        this.onFoodItemClickListener=onFoodItemClickListener;
    }

    @Override
    public int getItemCount()
    {
        return foods.size();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_template,parent,false);
        return new FoodViewHolder(view,onFoodItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bind(foods.get(position));
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnFoodItemClickListener onFoodItemClickListener;
        ImageView foodImage;
        TextView Title;
        TextView Price;
        Button addButton;
        public FoodViewHolder(@NonNull View itemView, OnFoodItemClickListener onFoodItemClickListener) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_view_food);
            Title= itemView.findViewById(R.id.text_view_food_name);
            Price=itemView.findViewById(R.id.text_view_food_price);
            addButton=itemView.findViewById(R.id.addButton);
            this.onFoodItemClickListener=onFoodItemClickListener;

        }
        public void bind(Food food){
            Glide.with(itemView).load(food.imageUrl).into(foodImage);
            Title.setText(food.foodname);
            Price.setText(food.price +" KSH");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFoodItemClickListener.onFoodItemClick(foods.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnFoodItemClickListener {
        void onFoodItemClick(Food food, int position);
    }
}
