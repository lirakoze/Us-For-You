package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.us4u.R;

import java.util.ArrayList;

import Models.CartItem;
import Helpers.DatabaseHelper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {
    private ArrayList<CartItem>cartItems;
    public  DatabaseHelper dbHelper;
    public CartAdapter(ArrayList<CartItem> cartItems, Context context){
        this.cartItems = cartItems;
        dbHelper=new DatabaseHelper(context);
    }

    @Override
    public int getItemCount()
    {
        return cartItems.size();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_template,parent,false);
        return new CartItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
        CartItem item = cartItems.get(holder.getAdapterPosition());



        holder.decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity=Integer.parseInt(holder.Quantity.getText().toString());
                Integer price= Integer.parseInt(item.getPrice());
                Integer amount=0;
                System.out.println("Decrease Button is Clicked");
                if (quantity==1){
                    holder.decreaseBtn.setEnabled(false);
                }
                else{
                    quantity--;
                    holder.Quantity.setText(quantity.toString());
                    amount= price * quantity;
                    holder.Amount.setText("KSH "+amount);
                    boolean isUpdated= dbHelper.updateCartItem(item.getId(),item.getName(),quantity.toString(),amount.toString(),item.getPrice());
                    if (isUpdated){
                        System.out.println(item.getName()+" values have been updated to Cart");
                    }
                    else {
                        System.out.println(item.getName()+" failed to update to Cart");
                    }

                }
            }
        });

        holder.increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity=Integer.parseInt(holder.Quantity.getText().toString());
                Integer price= Integer.parseInt(item.getPrice());
                Integer amount=0;
                System.out.println("Increase Button is Clicked");
                if (quantity<15){
                    quantity++;
                    holder.Quantity.setText(quantity.toString());
                    amount= price * quantity;
                    holder.Amount.setText("KSH "+amount);
                    boolean isUpdated= dbHelper.updateCartItem(item.getId(),item.getName(),quantity.toString(),amount.toString(),item.getPrice());
                    if (isUpdated){
                        System.out.println(item.getName()+" values have been updated to Cart");
                    }
                    else {
                        System.out.println(item.getName()+" failed to update to Cart");
                    }
                }
                else{
                    holder.increaseBtn.setEnabled(false);
                }
            }
        });

        holder.removeItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean isDeleted= dbHelper.deleteCartItem(item.getId());
                if(isDeleted){
                    //cartItems.remove(holder.getAdapterPosition());
                    holder.bind(cartItems.remove(holder.getAdapterPosition()));
                    System.out.println(item.getName()+" has been removed from cart");
                    String message="Item Has Been Removed ";
                    Toast.makeText(v.getContext(), message,Toast.LENGTH_SHORT).show();

                }
                else
                    System.out.println("Failed to remove "+item.getName());

                String message="Failed to remove Item ";
                Toast.makeText(v.getContext(), message,Toast.LENGTH_SHORT).show();

            }
        });

    }


    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        DatabaseHelper _dbHelper;
        ImageView Url;
        TextView Name, Quantity, Amount;
        Button increaseBtn, decreaseBtn, removeItemBTN;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            _dbHelper=new DatabaseHelper(itemView.getContext());
            Name=itemView.findViewById(R.id.name);
            Url = itemView.findViewById(R.id.image);
            Quantity=itemView.findViewById(R.id.quantity);
            Amount=itemView.findViewById(R.id.amount);
            removeItemBTN=itemView.findViewById(R.id.button_remove);
            increaseBtn=itemView.findViewById(R.id.increase);
            decreaseBtn=itemView.findViewById(R.id.decrease);

        }
        public void bind(CartItem cartItem){
            Name.setText(""+cartItem.getName());
           Glide.with(itemView).load(cartItem.getUrl()).into(Url);
            Quantity.setText(""+cartItem.getQuantity());
            Amount.setText(cartItem.getAmount()+" KSH");
            removeItemBTN.setOnClickListener(this);
            /*new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Remove Button Has Been Clicked");
                    _dbHelper.deleteCartItem(cartItem.getUrl());
                    Toast.makeText(itemView.getContext(), cartItem.getName()+" is removed from cart",Toast.LENGTH_SHORT).show();
                }
            }*/
        }

        @Override
        public void onClick(View v) {
            notifyDataSetChanged();
        }
    }

}
