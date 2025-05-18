package com.example.orderdeliverytrackingsystem;

import android.os.Parcel;
import android.os.Parcelable;

public class products implements Parcelable {
    String product_name, product_packaging;
    int packaging_price;
    int itemCount = 0;

    public products() {
    }
    protected products(Parcel in) {
        product_name = in.readString();
        product_packaging = in.readString();
        packaging_price = in.readInt();
        itemCount = in.readInt();
    }

    public static final Parcelable.Creator<products> CREATOR = new Parcelable.Creator<products>() {
        @Override
        public products createFromParcel(Parcel in) {
            return new products(in);
        }

        @Override
        public products[] newArray(int size) {
            return new products[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_name);
        dest.writeString(product_packaging);
        dest.writeInt(packaging_price);
        dest.writeInt(itemCount);
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_packaging() {
        return product_packaging;
    }

    public void setProduct_packaging(String product_packaging) {
        this.product_packaging = product_packaging;
    }

    public int getPackaging_price() {
        return packaging_price;
    }

    public void setPackaging_price(int packaging_price) {
        this.packaging_price = packaging_price;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }


}