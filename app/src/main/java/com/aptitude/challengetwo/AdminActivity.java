package com.aptitude.challengetwo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aptitude.challengetwo.Util.RecordIDGenerator;
import com.aptitude.challengetwo.datamodel.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AdminActivity extends AppCompatActivity {
    ImageView cropImageView;
    EditText edttitle, edtcost, edtdesc;
    Button imageButton;
    String imageString;
    LinearLayout progresslayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cropImageView = findViewById(R.id.cropImageView);
        edttitle = findViewById(R.id.edt_title);
        edtcost = findViewById(R.id.edt_price);
        edtdesc = findViewById(R.id.edt_description);
        imageButton = findViewById(R.id.imageButton);
        progresslayout = findViewById(R.id.progresslayout);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AdminActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            upload();
            return true;
        }else if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                cropImageView.setImageURI(resultUri);
                imageString = getBase64StringFromUri(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                imageString= null;
                Toast.makeText(AdminActivity.this, "Unable to get image, please try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }

    }

    private void upload(){
        PostModel postModel = new PostModel();
        String title = edttitle.getText().toString();
        String cost = edtcost.getText().toString();
        String desc = edtdesc.getText().toString();
        if (title==null||title.isEmpty()){
            Toast.makeText(AdminActivity.this, "Title is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cost==null||cost.isEmpty()){
            Toast.makeText(AdminActivity.this, "Price is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (desc==null||desc.isEmpty()){
            Toast.makeText(AdminActivity.this, "Description is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageString==null||imageString.isEmpty()){
            Toast.makeText(AdminActivity.this, "Image is required", Toast.LENGTH_SHORT).show();
            return;
        }
        progresslayout.setVisibility(View.VISIBLE);
        postModel.setTitle(title);
        Double newcost = Double.valueOf(cost);
        postModel.setCost(newcost);
        postModel.setDescription(desc);
        postModel.setImageString(imageString);

        FirebaseDatabase.getInstance().getReference("posts").child(RecordIDGenerator.generateUniqueId()).setValue(postModel).addOnCompleteListener(AdminActivity.this
                , new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progresslayout.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Toast.makeText(AdminActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AdminActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getBase64StringFromUri(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
        return Base64.encodeToString( ((ByteArrayOutputStream) outputStream).toByteArray(), Base64.DEFAULT);
    }
}
