package Helpers;

 public class Constants {

    //Food Constant Properties
    public static String FOOD_ID="ID";
    public static String FOOD_NAME="NAME";
    public static String FOOD_DESCRIPTION="DESCRIPTION";
    public static String FOOD_PRICE="PRICE";
    public static String FOOD_STATUS="STATUS";
    public static String FOOD_IMAGE_URL="IMAGE";
    public static String FOOD_CATEGORY="CATEGORY";

    //USER
    public static String USER_FNAME="FNAME";
    public static String USER_LNAME="LNAME";
    public static String USER_PHONE="PHONE";
    public static String USER_EMAIL="EMAIL";

    //MPESA
    public static final int CONNECT_TIMEOUT = 60 * 1000;

    public static final int READ_TIMEOUT = 60 * 1000;

    public static final int WRITE_TIMEOUT = 60 * 1000;

    public static final String BASE_URL = "https://sandbox.safaricom.co.ke/";
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
    public static final String PARTYB = "174379"; //same as business shortcode above
    public static final String CALLBACKURL = "http://mpesa-requestbin.herokuapp.com/16ckgut1";


}
