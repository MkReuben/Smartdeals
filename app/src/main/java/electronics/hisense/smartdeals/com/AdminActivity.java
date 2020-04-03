package electronics.hisense.smartdeals.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Prevalent.Prevalent;
import electronics.hisense.smartdeals.com.Admin.AdminCategoryActivity;
import electronics.hisense.smartdeals.com.Model.Users;

public class AdminActivity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog LoadingBar;
    private String parentdbName = "Admins";
    private CheckBox CheckBoxRememberMe;
    private TextView AdminLink,NotAdminLink,forgetpasswordlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        InputNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        LoginButton = findViewById(R.id.login_btn);
        LoadingBar = new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

    }

    private void LoginUser()
    {

        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();


        if (phone.isEmpty()) {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoadingBar.setTitle("Login Account");
            LoadingBar.setMessage("Please wait while we are checking the credentials");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();


            AllowAccessToAccount(phone,password);

        }

    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
//
//        if (CheckBoxRememberMe.isChecked())
//        {
//            Paper.book().write(Prevalent.userPhoneKey,phone);
//            Paper.book().write(Prevalent.userPasswordKey,password);
//
//        }

        final DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentdbName).child(phone).exists())
                {
                    Users usersData=dataSnapshot.child(parentdbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {

                        if (usersData.getPassword().equals(password))
                        {
                            if (parentdbName.equals("Admins"))
                            {
                                Toast.makeText(AdminActivity.this, "Welcome Admin you are logged in successfully", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();

                                Intent intent=new Intent(AdminActivity.this, AdminCategoryActivity.class);
                                Prevalent.currentonlineUsers=usersData;
                                startActivity(intent);
                            }
                            else if (parentdbName.equals("Admins"))
                            {
                                Toast.makeText(AdminActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent=new Intent(AdminActivity.this, AdminCategoryActivity.class);
                                Prevalent.currentonlineUsers=usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            LoadingBar.dismiss();
                            Toast.makeText(AdminActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(AdminActivity.this, "Account with this " + phone + "number do not exist", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
