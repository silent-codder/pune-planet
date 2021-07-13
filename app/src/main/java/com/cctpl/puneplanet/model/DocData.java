package com.cctpl.puneplanet.model;

public class DocData extends DocId{
    String Title;
    String Description;
    String UserId;
    String Status;
    Long TimeStamp;

    public DocData() {
    }

    public DocData(String title, String description, String userId, String status, Long timeStamp) {
        Title = title;
        Description = description;
        UserId = userId;
        Status = status;
        TimeStamp = timeStamp;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }
}
