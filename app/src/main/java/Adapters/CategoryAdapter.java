package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.us4u.R;

import java.util.ArrayList;

import Models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private OnCategoryFoodItemClickListener onCategoryFoodItemClickListener;
    private ArrayList<Category>categories;
    public CategoryAdapter(ArrayList<Category> categories, OnCategoryFoodItemClickListener onCategoryFoodItemClickListener){
        this.categories = categories;
        this.onCategoryFoodItemClickListener=onCategoryFoodItemClickListener;
    }

    @Override
    public int getItemCount()
    {
        return categories.size();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_template,parent,false);
        return new CategoryViewHolder(view,onCategoryFoodItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnCategoryFoodItemClickListener onCategoryFoodItemClickListener;

        ImageView foodImage;
        TextView foodName;
        public CategoryViewHolder(@NonNull View itemView, OnCategoryFoodItemClickListener onCategoryFoodItemClickListener) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_view_food);
            foodName = itemView.findViewById(R.id.text_view_category_name);
            this.onCategoryFoodItemClickListener=onCategoryFoodItemClickListener;

        }
        public void bind(Category category){
           Glide.with(itemView).load(category.imageurl).into(foodImage);
            foodName.setText(category.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        onCategoryFoodItemClickListener.onCategoryFoodItemClick(categories.get(getAdapterPosition()));
        }
    }
    public interface OnCategoryFoodItemClickListener {
        void onCategoryFoodItemClick(Category category);
    }
}
