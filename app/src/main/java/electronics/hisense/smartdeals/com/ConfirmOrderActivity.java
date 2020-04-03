package electronics.hisense.smartdeals.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Prevalent.Prevalent;


    public class ConfirmOrderActivity extends AppCompatActivity {
        private EditText nameEdt, phoneEdt, LocationEdt;
        private Button confirmBtn;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_confirm_order);

            nameEdt = findViewById(R.id.enquiry_name);
            phoneEdt = findViewById(R.id.enquiry_phone_number);
            LocationEdt = findViewById(R.id.enquiry_location);
            confirmBtn = findViewById(R.id.confirm_btn);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Check();
                }
            });


        }

        private void Check() {
            if (TextUtils.isEmpty(nameEdt.getText().toString())) {
                Toast.makeText(this, "Please provide your full name ", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(phoneEdt.getText().toString())) {
                Toast.makeText(this, "Please provide your phone number ", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(LocationEdt.getText().toString())) {
                Toast.makeText(this, "Please provide your location ", Toast.LENGTH_SHORT).show();
            } else {
                confirmEnquiry();
            }
        }

        private void confirmEnquiry() {


            final String saveCurrentDate, saveCurrentTime;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());


            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ssa");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            final DatabaseReference enquiresRef = FirebaseDatabase.getInstance().getReference()
                    .child("Orders")
                    .child(Prevalent.currentonlineUsers.getPhone());

            HashMap<String, Object> enquiresMap = new HashMap<>();
            enquiresMap.put("name", nameEdt.getText().toString());
            enquiresMap.put("phone", phoneEdt.getText().toString());
            enquiresMap.put("location", LocationEdt.getText().toString());
            enquiresMap.put("date", saveCurrentDate);
            enquiresMap.put("time", saveCurrentTime);
            enquiresMap.put("state", "pending");

            enquiresRef.updateChildren(enquiresMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference().child("Order List")
                                .child("User view")
                                .child(Prevalent.currentonlineUsers.getPhone())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ConfirmOrderActivity.this, "Your final order has been submitted successfully", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(ConfirmOrderActivity.this, "Please  wait fot the the feedback", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                }
            });


        }
    }
