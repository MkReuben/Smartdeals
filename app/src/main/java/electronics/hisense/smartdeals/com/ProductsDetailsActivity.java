package electronics.hisense.smartdeals.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Prevalent.Prevalent;
import electronics.hisense.smartdeals.com.Model.Products;

public class ProductsDetailsActivity extends  AppCompatActivity {

    private ImageView productImage;
    private TextView productPrice, productLocation, productdescription, productName;
    private String productID = "",state="Normal";
    private Button call,sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        productID = getIntent().getStringExtra("pid");

//        addtocartBtn = findViewById(R.id.product_add_to_cart);
        call=findViewById(R.id.product_call);
        sms=findViewById(R.id.product_sms);
        productImage = findViewById(R.id.product_image_details);
        productPrice = findViewById(R.id.product_price_details);
        productLocation = findViewById(R.id.product_location_details);
        productdescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+254713442781"));
                if (ContextCompat.checkSelfPermission(ProductsDetailsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductsDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    startActivity(intent);
                }

            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("smsto:+254713442781");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);
            }
        });

        getProductsDetails(productID);

//
//        addtocartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (state.equals("Enquiry Placed")|| state.equals("Enquiry Completed"))
//                {
//                    Toast.makeText(ProductsDetailsActivity.this, "You can enquire more services once your  first enquiry services is completed", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    addingtoEnquireList();
//
//                }
//            }
//        });
//

    }

    @Override
    protected void onStart() {

        super.onStart();
//
//        CheckOrderState();
    }

    private void addingtoEnquireList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HHH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());


        final DatabaseReference enquireListRef = FirebaseDatabase.getInstance().getReference().child("Order List");

        final HashMap<String, Object> enquireMap = new HashMap<>();

        enquireMap.put("pid", productID);
        enquireMap.put("name", productName.getText().toString());
        enquireMap.put("price", productPrice.getText().toString());
        enquireMap.put("date", saveCurrentDate);
        enquireMap.put("time", saveCurrentTime);
        enquireMap.put("discount", "");


        enquireListRef.child("User view").child(Prevalent.currentonlineUsers.getPhone())
                .child("Products").child(productID)
                .updateChildren(enquireMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            enquireListRef.child("Admin view").child(Prevalent.currentonlineUsers.getPhone())
                                    .child("Services").child(productID)
                                    .updateChildren(enquireMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                Toast.makeText(ProductsDetailsActivity.this, "Added to Cart successfully Proceed  to get your product ", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductsDetailsActivity.this,CartActivity.class);

                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductsDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

       productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getName());
                    productPrice.setText(products.getPrice());
                    productdescription.setText(products.getDescription());
                    productLocation.setText(products.getLocation());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//    private  void CheckOrderState()
//    {
//        DatabaseReference enquiryRef;
//        enquiryRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineUsers.getPhone());
//
//        enquiryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//
//                if (dataSnapshot.exists()) {
//                    String completedState = dataSnapshot.child("state").getValue().toString();
//                    //  String userName=dataSnapshot.child("name").getValue().toString();
//
//
//                    if (completedState.equals("completed"))
//                    {
//                        state="Enquiry Received";
//
//
//                    }
//                    else if (completedState.equals("pending"))
//                    {
//                        state="Enquiry Placed";
//
//                    }
//                }

//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}