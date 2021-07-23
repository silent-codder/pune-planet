package com.cctpl.puneplanet.model;

public class UploadDocData extends UploadDocId{
    String Agreement;
    String Category;
    String CoverImg;
    String Description;
    String Status;
    String SubCategory;
    Long TimeStamp;
    String Title;
    String UserId;

    public UploadDocData() {
    }

    public UploadDocData(String agreement, String category, String coverImg, String description, String status, String subCategory, Long timeStamp, String title, String userId) {
        Agreement = agreement;
        Category = category;
        CoverImg = coverImg;
        Description = description;
        Status = status;
        SubCategory = subCategory;
        TimeStamp = timeStamp;
        Title = title;
        UserId = userId;
    }

    public String getAgreement() {
        return Agreement;
    }

    public void setAgreement(String agreement) {
        Agreement = agreement;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCoverImg() {
        return CoverImg;
    }

    public void setCoverImg(String coverImg) {
        CoverImg = coverImg;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
