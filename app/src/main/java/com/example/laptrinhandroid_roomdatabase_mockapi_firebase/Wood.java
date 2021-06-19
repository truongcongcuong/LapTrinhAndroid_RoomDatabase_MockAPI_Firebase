package com.example.laptrinhandroid_roomdatabase_mockapi_firebase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Wood implements Serializable {

    @PrimaryKey
    public int id;

    public String type;

    public double price;

    public String country;

    public Wood() {
    }

    public Wood(int id, String type, double price, String country) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Wood{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", country='" + country + '\'' +
                '}';
    }
}
