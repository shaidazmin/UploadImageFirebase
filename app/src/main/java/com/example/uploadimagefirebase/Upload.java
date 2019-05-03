package com.example.uploadimagefirebase;

import android.media.Image;
import android.widget.EditText;
import android.widget.ImageView;

public class Upload {

    String comment;
    String imageUri;

    public Upload(String comment, String imageUri) {
        this.comment = comment;
        this.imageUri = imageUri;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


}
