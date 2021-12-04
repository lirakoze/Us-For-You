package Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Restaurant {
    String Name;
    String Id;
    String Email;
    String Image;
    String Rating;

    String RegistrationDate;

    public Restaurant(String name, String id, String email, String image, String rating, String registrationDate) {
        Name = name;
        Id = id;
        Email = email;
        Image = image;
        Rating = rating;
        RegistrationDate = registrationDate;
    }

    public Restaurant(String name, String image, String rating,String email) {
        Name = name;
        Image = image;
        Rating = rating;
        Id= UUID.randomUUID().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todayDate = simpleDateFormat.format(calendar.getTime());
        RegistrationDate=todayDate;
        Email=email;
    }
    public Restaurant(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }
}
