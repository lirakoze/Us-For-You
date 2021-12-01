package Models;

import java.io.Serializable;
import java.util.UUID;

public class Food implements Serializable {
    public String imageUrl;
    public  String foodname,description,category,status;
    public int price;
    public String id;


    public Food(String imageUrl, String foodname, String description, String category, String status, int price) {
        this.imageUrl = imageUrl;
        this.foodname = foodname;
        this.description = description;
        this.category = category;
        this.price = price;
        this.status=status;
        this.id= UUID.randomUUID().toString();
    }

    public Food() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Food{" +
                "imageUrl='" + imageUrl + '\'' +
                ", foodname='" + foodname + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", id='" + id + '\'' +
                '}';
    }
}
