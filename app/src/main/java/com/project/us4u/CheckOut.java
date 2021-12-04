package com.project.us4u;

import static Helpers.Constants.BUSINESS_SHORT_CODE;
import static Helpers.Constants.CALLBACKURL;
import static Helpers.Constants.PARTYB;
import static Helpers.Constants.PASSKEY;
import static Helpers.Constants.TRANSACTION_TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Helpers.DatabaseHelper;
import Models.AccessToken;
import Models.CartItem;
import Models.Order;
import Models.STKPush;
import Services.DarajaApiClient;
import Services.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOut extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView amountTxt, taxTxt, deliveryTxt, totalAmountTxt;
    EditText specificPointEdt, mpesaNumberEdt;
    LinearLayout mpesaLayout;
    Button proceedToPaymentBtn;
    RadioGroup paymentMethodRG;
    RadioButton selectedPaymentOptionRBtn;
    ImageButton backBtn;
    private DatabaseHelper _dbHelper;
    private ArrayList<CartItem> _cartItems;
    Integer amount=0, itemsQuantity=0, totalAmount=0, deliveryPrice=0;
    Double taxAmount=0.0;
    Order order;
    ArrayList<String> locations;
    Spinner selectedLocationSpn;
    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    String PhoneNo,Username,Location,Point, Uid;
    //Preference field
    SharedPreferences sharedPreferences;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initializeViews();
        locations= new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("ORDERS");
        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.
               //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);

        PhoneNo=sharedPreferences.getString("PHONE","");
        Username=sharedPreferences.getString("USERNAME","");
        Uid=sharedPreferences.getString("ID","");

        _dbHelper= new DatabaseHelper(getApplicationContext());
        _cartItems= new ArrayList<>();
        getCartItems();
        getLocations();
        ArrayAdapter<String> locAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        selectedLocationSpn.setAdapter(locAdapter);

        for (CartItem item:_cartItems) {
            System.out.println(item.toString());
            amount+= Integer.parseInt(item.getAmount());
            itemsQuantity +=Integer.parseInt(item.getQuantity());
        }
        amountTxt.setText("KSH "+amount.toString());
        setDeliveryCost(itemsQuantity);
        setTax(amount);
        setTotalAmount(amount,deliveryPrice,taxAmount);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CheckOut.this, CartView.class);
                startActivity(intent);
                finish();
            }
        });

        proceedToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=paymentMethodRG.getCheckedRadioButtonId();
                selectedPaymentOptionRBtn=(RadioButton)findViewById(selectedId);
                Point=specificPointEdt.getText().toString();
                Location = selectedLocationSpn.getSelectedItem().toString();

                String option = selectedPaymentOptionRBtn.getText().toString();

                if (Location.isEmpty()){
                    specificPointEdt.setError("Required");
                    return;
                }
                if (Point.isEmpty()){
                    specificPointEdt.setError("Required");
                    return;
                }
                order= new Order(PhoneNo,
                        Username,Location,Point,_cartItems,totalAmount.toString(),option);

                if (option.equalsIgnoreCase("Mpesa")){

                    getAccessToken();
                    mpesaLayout.setVisibility(View.VISIBLE);
                    String number=mpesaNumberEdt.getText().toString();
                    if (number.isEmpty() || number.length()<10){
                        mpesaNumberEdt.setError("Required");
                        return;
                    }
                    performSTKPush(number,totalAmount.toString());
                    makeOrder(order);
                    System.out.println(order.toString());
                }
                else if(option.equalsIgnoreCase("Card")){
                    mpesaLayout.setVisibility(View.INVISIBLE);
                    System.out.println(order.toString());
                }
                else{
                    Toast.makeText(CheckOut.this,"Select Payment Method",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void initializeViews(){

        backBtn=findViewById(R.id.orderBackBtn);
        amountTxt= findViewById(R.id.amount);
        taxTxt=findViewById(R.id.tax);
        deliveryTxt= findViewById(R.id.deliveryCost);
        totalAmountTxt=findViewById(R.id.totalAmount);
        proceedToPaymentBtn= findViewById(R.id.button_make_payment);
        paymentMethodRG= findViewById(R.id.radioGroupPay);
        selectedLocationSpn= findViewById(R.id.spinnerPlaces);
        specificPointEdt = findViewById(R.id.editTextSpecificPoint);
        mpesaNumberEdt=findViewById(R.id.editTextMpesaNumber);
        mpesaLayout=findViewById(R.id.layoutMpesa);

    }
    private void  setDeliveryCost(int quantity){

        if (quantity <=12 )
        {
            deliveryPrice=100;
            deliveryTxt.setText("KSH "+deliveryPrice.toString());
        }
        else if (quantity >12 && quantity<20){
            deliveryPrice=170;
            deliveryTxt.setText("KSH "+deliveryPrice.toString());
        }
        else if (quantity >20 && quantity <30){
            deliveryPrice=220;
            deliveryTxt.setText("KSH "+deliveryPrice.toString());
        }
        else if (quantity >30 && quantity <50){
            deliveryPrice=290;
            deliveryTxt.setText("KSH "+deliveryPrice.toString());
        }
        else {
            deliveryPrice=360;
            deliveryTxt.setText("KSH "+deliveryPrice.toString());
        }

    }
    private  void setTax(int amount){
        taxAmount= amount*0.05;
        taxTxt.setText("KSH "+taxAmount.toString());
    }
    private void setTotalAmount( int amount, int deliveryCost, double tax){
        int total =amount+deliveryCost+ (int)Math.round(tax);
        totalAmount = total;
        totalAmountTxt.setText("KSH "+totalAmount);
    }
    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing Payment....");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "Us For You", //Account reference
                "Testing"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                System.out.println("post submitted to API. %s "+response.body());
                                _dbHelper.clearCart();

                            } else {
                                System.out.println("Response %s "+response.errorBody().string());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(CheckOut.this, PaymentConfirmation.class);
                        startActivity(intent);
                        finish();
                    }
                },5000);

            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }
    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);

                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    private void getLocations(){
        locations.add("Hostel New Block");
        locations.add("Hostel Old Block");
        locations.add("Lilian Beam");
        locations.add("Admin Office");
        locations.add("School of Humanities");
        locations.add("School of Science");
        locations.add("School of Business");
        locations.add("Wooden Classes");
        locations.add("Little Mumbai");
        locations.add("Basketball Court");
        locations.add("Student Center");
        locations.add("Auditorium");
        locations.add("Cinematic");
        locations.add("Gate A");
        locations.add("Gate B");
        locations.add("Football Pitch");
    }
    private void getCartItems(){
        Cursor res=_dbHelper.getCartItems();
        _cartItems.clear();
        if (res.getCount()==0){
            Toast.makeText(this,"No Cart Items",Toast.LENGTH_SHORT).show();
        }
        while(res.moveToNext()) {
            _cartItems.add(new CartItem(res.getString(0),res.getString(1),
                    res.getString(2),
                    res.getString(3), res.getString(4), res.getString(5)));

        }
    }
    private void makeOrder(Order order){
        databaseReference.child(Uid).child(order.getOrderId()).setValue(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }
                        else {
                            Toast.makeText(CheckOut.this,"Failed To make Order",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}