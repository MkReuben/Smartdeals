package electronics.hisense.smartdeals.com.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import electronics.hisense.smartdeals.com.R;

public class AdminMaintainActivity extends AppCompatActivity {

    private Button applyChangebtn,Deletebtn;
    private EditText name,price,description,location;
    private ImageView imageView;
    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain);

        productID = getIntent().getStringExtra("pid");
        productRef=FirebaseDatabase.getInstance().getReference().child("Products").child(productID);


        applyChangebtn=findViewById(R.id.edit_btn);
        Deletebtn=findViewById(R.id.delete_btn);
        name=findViewById(R.id.admin_maintain_name);
        price=findViewById(R.id.admin_maintain_price);
        description=findViewById(R.id.admin_maintain_description);
        location=findViewById(R.id.admin_maintain_location);
        imageView=findViewById(R.id.admin_maintain_image);

        displaySpecificServiceInfo();

        Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteThisService();

            }
        });

        applyChangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                applyChanges();

            }
        });
    }

    private void deleteThisService()
    {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(AdminMaintainActivity.this, "The Product is deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(AdminMaintainActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void applyChanges()
    {

        String sName=name.getText().toString();
        String sPrice=price.getText().toString();
        String sDescription=description.getText().toString();
        String sLocation=location.getText().toString();

        if (sName.equals(""))
        {
            Toast.makeText(this, "Write down Service Name", Toast.LENGTH_SHORT).show();
        }
        else  if (sPrice.equals(""))
        {
            Toast.makeText(this, "Write down Service Price", Toast.LENGTH_SHORT).show();
        }
        else  if (sDescription.equals(""))
        {
            Toast.makeText(this, "Write down Service Description", Toast.LENGTH_SHORT).show();
        }
        else  if (sLocation.equals(""))
        {
            Toast.makeText(this, "Write down Service Location", Toast.LENGTH_SHORT).show();
        }
        else
        {

            HashMap<String,Object> productMap= new HashMap<>();

            productMap.put("sid",productID);
            productMap.put("description",sDescription);
            productMap.put("location",sLocation);
            productMap.put("price",sPrice);
            productMap.put("name",sName);

            productRef.updateChildren( productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainActivity.this, "Changes applied Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminMaintainActivity.this,AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }


    }

    private void displaySpecificServiceInfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String pName =dataSnapshot.child("name").getValue().toString();
                    String pPrice =dataSnapshot.child("price").getValue().toString();
                    String pDescription =dataSnapshot.child("description").getValue().toString();
                    String pLocation =dataSnapshot.child("location").getValue().toString();
                    String pImage =dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    location.setText(pLocation);
                    Picasso.get().load(pImage).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {


            }
        });
    }
}
