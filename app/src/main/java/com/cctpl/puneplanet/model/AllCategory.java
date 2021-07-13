package com.cctpl.puneplanet.model;

public class AllCategory {
    String Category;
    int imgId;

    public AllCategory(AllCategory[] allCategories){
    }

    public AllCategory(String category, int imgId) {
        Category = category;
        this.imgId = imgId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
