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
import Models.Order;

public class OrdersAdapter extends FirestoreRecyclerAdapter<Order, OrdersAdapter.OrdersViewHolder> {
    private OnListItemClick onListItemClick;
    public DatabaseHelper dbHelper;

    public OrdersAdapter(@NonNull FirestoreRecyclerOptions<Order> options, OnListItemClick onListItemClick, Context context) {
        super(options);
        this.onListItemClick=onListItemClick;
        dbHelper=new DatabaseHelper(context);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull Order order) {
        holder.PaymentMethod.setText(""+order.getPaymentMethod());
        holder.Time.setText(""+order.getTime());
        holder.Amount.setText("Total "+order.getTotalAmount());

    }
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_template,parent,false);

        return new OrdersViewHolder(view);
    }
    public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView OrderId, Amount, Time, PaymentMethod;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderId= itemView.findViewById(R.id.orderId);
            Amount= itemView.findViewById(R.id.textViewAmount);
            Time=itemView.findViewById(R.id.textOrderTime);
            PaymentMethod=itemView.findViewById(R.id.textViewPaymentMode);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemclick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
    public interface OnListItemClick{
        void onItemclick(Order OrderItem, int position);
    }
}
