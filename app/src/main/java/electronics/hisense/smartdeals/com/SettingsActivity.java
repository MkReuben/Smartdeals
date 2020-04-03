package electronics.hisense.smartdeals.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import Prevalent.Prevalent;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTxtBtn,closeTxtButton,saveTxtButton;

    private Uri imageUri;
    private String myUri="";
    private StorageReference storageProfilePictureRef;
    private String checker="";
    private StorageTask uploadTask;
    private Button securityQuestionBtn;
    private  static final int PICK_IMAGE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile pictures");

        securityQuestionBtn=findViewById(R.id.security_questions_btn);
        profileImageView=findViewById(R.id.settings_profile_image);
        fullNameEditText=findViewById(R.id.settings_full_name);
        userPhoneEditText=findViewById(R.id.settings_phone_number);
        addressEditText=findViewById(R.id.settings_address);
        profileChangeTxtBtn=findViewById(R.id.profile_image_change_btn);
        closeTxtButton=findViewById(R.id.close_settings_btn);
        saveTxtButton=findViewById(R.id.update_settings);

        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);


        closeTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });


        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });
        saveTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyInfo();
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker="clicked";

                Intent gallery =new Intent();
                gallery.setType("image/");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
            }
        });

    }

    private void updateOnlyInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");


        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("phoneOrder",userPhoneEditText.getText().toString());
        ref.child(Prevalent.currentonlineUsers.getPhone()).updateChildren(userMap);


        TextView userNameTextView=findViewById(R.id.profile_image_change_btn);
        CircleImageView profileImageView=findViewById(R.id.settings_profile_image);

//        userNameTextView.setText(Prevalent.currentonlineUsers.getName());
        // Picasso.get().load(Prevalent.currentonlineUsers.getImage()).placeholder(R.drawable.profile).into(profileImageView);



        startActivity(new Intent( SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile info updated successfully.", Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(SettingsActivity.this,AccountActivity.class);
        startActivity(intent);

        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imageUri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

                profileImageView.setImageBitmap(bitmap);

            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
//
//        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK&& data!=null)
//
//        {
//            CropImage.ActivityResult result =CropImage.getActivityResult(data);
//            imageUri = result.getUri();
//
//            profileImageView.setImageURI(imageUri);
//
//        }
        else
        {
            Toast.makeText(this, "Error:Try Again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }

    }

    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Phone Number is required", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }

    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait,while we are updating you account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri!= null)
        {
            final StorageReference fileRef= storageProfilePictureRef
                    .child(Prevalent.currentonlineUsers.getPhone()+ ".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception


                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return  fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri>task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUri =task.getResult();
                                myUri=downloadUri.toString();

                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");


                                HashMap<String,Object> userMap = new HashMap<>();
                                userMap.put("name",fullNameEditText.getText().toString());
                                userMap.put("address",addressEditText.getText().toString());
                                userMap.put("phoneOrder",userPhoneEditText.getText().toString());
                                userMap.put("image",myUri);
                                ref.child(Prevalent.currentonlineUsers.getPhone()).updateChildren(userMap);
                                progressDialog.dismiss();

                                ref.child(Prevalent.currentonlineUsers.getPhone()).updateChildren(userMap);

                                startActivity(new Intent( SettingsActivity.this,AccountActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile info updated successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else {
            Toast.makeText(this, "image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUsers.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();

                        //Display
                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}