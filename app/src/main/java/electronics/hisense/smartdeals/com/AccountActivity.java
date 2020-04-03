package electronics.hisense.smartdeals.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {


    private ImageView contactus,enquires,help,logout,location,admin;
    private TextView tContactus,tEnquires,tServices,thelp,tlogout,tlocation,tadmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        TextView userNameTextView=findViewById(R.id.user_profile_name);
        CircleImageView profileImageView=findViewById(R.id.user_profile_image);
//
//        userNameTextView.setText(Prevalent.currentonlineUsers.getName());
//        Picasso.get().load(Prevalent.currentonlineUsers.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        contactus=findViewById(R.id.contactus_image);

        tadmin=findViewById(R.id.tv_admin);
        admin=findViewById(R.id.admin_image);
//        enquires=findViewById(R.id.order_image);
        help=findViewById(R.id.help_image);
//        logout=findViewById(R.id.logout_image);
        location=findViewById(R.id.loc_image);
        tlocation=findViewById(R.id.tv_location);

        tContactus=findViewById(R.id.tv_contactus);
//        tEnquires=findViewById(R.id.tv_order);
        thelp=findViewById(R.id.tv_help);
//        tlogout=findViewById(R.id.tv_logout);


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AccountActivity.this,AdminActivity.class);
                startActivity(intent);

            }
        });
        tadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AccountActivity.this,AdminActivity.class);
                startActivity(intent);

            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent location=new Intent(AccountActivity.this,LocationActivity.class);
                startActivity(location);
            }
        });
        tlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent location=new Intent(AccountActivity.this,LocationActivity.class);
                startActivity(location);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+254713442781"));
                if (ContextCompat.checkSelfPermission(AccountActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    startActivity(intent);
                }
            }
        });
        tContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+254713442781"));
                if (ContextCompat.checkSelfPermission(AccountActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    startActivity(intent);
                }
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Uri uri = Uri.parse("smsto:+254713442781");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);
            }
        });
        thelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Uri uri = Uri.parse("smsto:+254713442781");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent go = new Intent(AccountActivity.this, MainActivity.class);
//                go.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(go);
//                finish();
//
//            }
//        });
//        tlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent go = new Intent(AccountActivity.this, MainActivity.class);
//                go.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(go);
//                finish();
//
//            }
//        });
//        enquires.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent start=new Intent(AccountActivity.this,CartActivity.class);
//                startActivity(start);
//            }
//        });
//        tEnquires.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent start=new Intent(AccountActivity.this, CartActivity.class);
//                startActivity(start);
//            }
//        });


    }
}
