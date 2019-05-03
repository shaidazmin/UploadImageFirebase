package com.example.uploadimagefirebase;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button choseImage,saveImage,showImage;
    ImageView imageView;
    EditText commentEditText;

    // chose for image ....
    Uri imageUri;
    private static final int IMAGE_RQUEST = 1;

    // upload image ......

    DatabaseReference databaseReference;
    StorageReference storageReference;

    // akta image upload na howar age jno onno akta upload na hoy....

    StorageTask storageTask;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        storageReference = FirebaseStorage.getInstance().getReference("Image");



        choseImage = findViewById(R.id.choseImageButton);
        saveImage = findViewById(R.id.saveImageButton);
        showImage = findViewById(R.id.showImageButton);

        commentEditText = findViewById(R.id.commentImageEditText);
        imageView = findViewById(R.id.imageViewId);

        choseImage.setOnClickListener(this);
        saveImage.setOnClickListener(this);
        showImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.choseImageButton){
            uploadImage();

        }
        if(view.getId() == R.id.saveImageButton){

            if (storageTask != null && storageTask.isInProgress()){
                Toast.makeText(this," wait a moment! Image is uploading ",Toast.LENGTH_SHORT).show();
            }
            else {
                saveImageDatabase();
            }
        }
        if(view.getId() == R.id.showImageButton){
            Intent intent = new Intent(MainActivity.this,ShowActivity.class);
            startActivity(intent);

        }

    }



    // upload image in databse ....

    // image er Extention  er jonno
    // ***Important ........
    public  String getFileExtention(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    void saveImageDatabase() {
       final String comment = commentEditText.getText().toString().trim();
        if(comment.isEmpty()){
            commentEditText.setError("Writ your wish");
            commentEditText.requestFocus();
            return;
        }

        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Toast.makeText(getApplicationContext(),"Image saved",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        Upload upload = new Upload(comment,taskSnapshot.getStorage().getDownloadUrl().toString());
                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(upload);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(),"Image save failed",Toast.LENGTH_SHORT).show();
                    }
                });

    }// close upload image database


    // find image from mobile ......
    private void uploadImage() {

        Intent intent = new Intent();
        intent.setType("image/video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_RQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_RQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }

    }// close find image from mobile
}
