package com.cctpl.puneplanet.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class UploadDocId {
    @Exclude
    public String UploadDocId;
    public <T extends UploadDocId> T withId(@NonNull final String id){
        this.UploadDocId = id;
        return (T)this;
    }
}
