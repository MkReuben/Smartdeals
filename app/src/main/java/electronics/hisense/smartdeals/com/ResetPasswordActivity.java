package electronics.hisense.smartdeals.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import Prevalent.Prevalent;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    private TextView title, titleQuestion;
    private EditText phoneNumber, questionOne, questionTwo;
    private Button verifyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        title = findViewById(R.id.activity_title);

        titleQuestion = findViewById(R.id.question_title);
        phoneNumber = findViewById(R.id.search_phone_number);
        questionOne = findViewById(R.id.question_one);
        questionTwo = findViewById(R.id.question_two);
        verifyBtn = findViewById(R.id.verify_btn);


        check = getIntent().getStringExtra("check");



    }

    @Override
    protected void onStart()
    {

        super.onStart();

        phoneNumber.setVisibility(View.GONE);


        if (check.equals("settings"))
        {
            title.setText("Set Questions");
            titleQuestion.setText("Please set Answer the following security questions?");

            verifyBtn.setText("Set");

            displayPreviousAnswers();

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    setAnswers();

                }
            });

        }
        else if (check.equals("login"))
        {

            phoneNumber.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    verifyUser();

                }
            });
        }
    }



    private void setAnswers()
    {
        String answerOne=questionOne.getText().toString().toLowerCase();
        String answerTwo=questionTwo.getText().toString().toLowerCase();

        if (questionOne.equals("")&& questionTwo.equals(""))

        {
            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions?", Toast.LENGTH_SHORT).show();
        }

        else

        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentonlineUsers.getPhone());

            HashMap<String,Object> userdataMap=new HashMap<>();
            userdataMap.put("answerOne",answerOne);
            userdataMap.put("answerTwo",answerTwo);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Security questions set successfully", Toast.LENGTH_SHORT).show();

                        Intent intent =new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void displayPreviousAnswers()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentonlineUsers.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String anwer1=dataSnapshot.child("answerOne").getValue().toString();
                    String anwer2=dataSnapshot.child("answerTwo").getValue().toString();

                    questionOne.setText(anwer1);
                    questionTwo.setText(anwer2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void verifyUser()
    {

        String phone =phoneNumber.getText().toString();
        final String answerOne=questionOne.getText().toString().toLowerCase();
        final String answerTwo=questionTwo.getText().toString().toLowerCase();

        if (!phone.equals("")&& !answerOne.equals("")&& !answerTwo.equals(""))
        {
            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String mphone=dataSnapshot.child("phone").getValue().toString();
                        if (dataSnapshot.hasChild("Security Questions"))
                        {
                            String anwer1=dataSnapshot.child("Security Questions").child("answerOne").getValue().toString();
                            String anwer2=dataSnapshot.child("Security Questions").child("answerTwo").getValue().toString();


                            if (!anwer1.equals(answerOne))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Your first answer is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else  if (!anwer2.equals(answerTwo))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Your second answer is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder =new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final  EditText newPassword =new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write Password here..");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if(!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent= new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);

                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();

                                    }
                                });
                                builder.show();
                            }
                        }

                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });


        }
        else
        {
            Toast.makeText(this, "please complete the form", Toast.LENGTH_SHORT).show();

        }

    }
}
