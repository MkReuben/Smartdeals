package electronics.hisense.smartdeals.com.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import electronics.hisense.smartdeals.com.R;

public class AdminAddNewProductsActivity extends AppCompatActivity {
    private String CategoryName,Description,Location,Price,name,saveCurrrentDate,saveCurrentTime;
    private ImageView InputProductImage;
    private Button AddNewServiceButton;
    private EditText InputProductName,InputProductDescription,InputProductLocation,InputProductPrice;
    private static final  int Gallerypick=1;
    private Uri imageUri;
    private Uri ImageUri;
    private String checker="";
    private String productRandomKey,downloadImageuRL;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_products);


        CategoryName = getIntent().getExtras().get("Category").toString();
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("Product images");
        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");
        AddNewServiceButton=findViewById(R.id.add_product_btn);
        InputProductName=findViewById(R.id.product_name);
        InputProductDescription=findViewById(R.id.product_description);
        InputProductLocation=findViewById(R.id.product_location);
        InputProductPrice=findViewById(R.id.product_price);
        InputProductImage=findViewById(R.id.select_product_image);
        LoadingBar = new ProgressDialog(this);

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//            checker = "clicked";
//            CropImage.activity(imageUri)
                //  .setAspectRatio(7,7)
                //.start(AdminAddNewServicesActivity.this);
                OpenGallery();
            }
        });


        AddNewServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateData();
            }
        });

    }

    private void ValidateData()
    {

        Description=InputProductDescription.getText().toString();
        Location=InputProductLocation.getText().toString();
        Price=InputProductPrice.getText().toString();
        name=InputProductName.getText().toString();

        if (ImageUri== null)
        {
            Toast.makeText(this, "Product Image is required", Toast.LENGTH_SHORT).show();
        }else if (Description.isEmpty())
        {
            Toast.makeText(this, "Product Description is required", Toast.LENGTH_SHORT).show();
        }else if (Location.isEmpty())
        {
            Toast.makeText(this, "Your product location is required", Toast.LENGTH_SHORT).show();
        }else if (Price.isEmpty())
        {
            Toast.makeText(this, "Your product price is mandatory", Toast.LENGTH_SHORT).show();
        }else if (name.isEmpty())
        {
            Toast.makeText(this, "Product name is required", Toast.LENGTH_SHORT).show();
        }else
        {
            StoreInformation();
        }
    }

    private void StoreInformation()
    {

        LoadingBar.setTitle("Adding New Product");
        LoadingBar.setMessage("Please wait while we are adding the new service");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();
        Calendar calendar=Calendar.getInstance();


        SimpleDateFormat currentDate= new SimpleDateFormat( "MMM dd,yyyy");
        saveCurrrentDate=currentDate.format(calendar.getTime());


        SimpleDateFormat currentTime= new SimpleDateFormat( "H:mm:ssa");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentTime+saveCurrrentDate;

        final StorageReference filepath= ProductImageRef.child(ImageUri.getLastPathSegment()+productRandomKey + "jpg");

        final UploadTask uploadTask=filepath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message =e.toString();
                Toast.makeText(AdminAddNewProductsActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                LoadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewProductsActivity.this, "Product Image uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageuRL=filepath.getDownloadUrl().toString();
                        return  filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {

                            downloadImageuRL=task.getResult().toString();
                            Toast.makeText(AdminAddNewProductsActivity.this, "Getting product image URL successful", Toast.LENGTH_SHORT).show();


                            SavedServiceInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void SavedServiceInfoToDatabase()
    {
        HashMap<String,Object> serviceMap= new HashMap<>();
        serviceMap.put("pid",productRandomKey);
        serviceMap.put("date",saveCurrrentDate);
        serviceMap.put("time",saveCurrentTime);
        serviceMap.put("description",Description);
        serviceMap.put("image",downloadImageuRL);
        serviceMap.put("location",Location);
        serviceMap.put("price",Price);
        serviceMap.put("name",name);
        serviceMap.put("category",CategoryName);



        ProductRef.child(productRandomKey).updateChildren(serviceMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent=new Intent(AdminAddNewProductsActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            LoadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductsActivity.this, "Service is added successfully..", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            LoadingBar.dismiss();

                            String message=task.getException().toString();
                            Toast.makeText(AdminAddNewProductsActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();


                        }

                    }
                });
    }


    private void OpenGallery()
    {

        Intent galleryIntent =new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallerypick && resultCode== RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
}
