package Models;

import com.project.us4u.BuildConfig;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Order implements Serializable {
    public String getOrderId() {
        return OrderId;
    }

    String OrderId;
    String PhoneNo;
    String Username, TotalAmount;
    String Location;
    String Point;
    String Time;

    String PaymentMethod;


    ArrayList<CartItem> cartItems;

    public Order(String phoneNo, String username, String location, String point, ArrayList<CartItem> cartItems, String totalAmount, String paymentMethod) {

        PhoneNo = phoneNo;
        Username = username;
        Location = location;
        TotalAmount=totalAmount;
        this.cartItems = cartItems;
        Point= point;
        OrderId= UUID.randomUUID().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        String todayDate = simpleDateFormat.format(calendar.getTime());
       Time =todayDate;
       PaymentMethod=paymentMethod;
    }
    public Order(){

    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }


    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


    public String getPoint() {
        return Point;
    }

    public void setPoint(String point) {
        Point = point;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    @Override
    public String toString() {
        return "Order{" +
                "OrderId='" + OrderId + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", Username='" + Username + '\'' +
                ", TotalAmount='" + TotalAmount + '\'' +
                ", Location='" + Location + '\'' +
                ", Point='" + Point + '\'' +
                ", Time='" + Time + '\'' +
                ", cartItems=" + cartItems +
                '}';
    }
}
