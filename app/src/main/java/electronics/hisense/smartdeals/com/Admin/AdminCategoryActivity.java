package electronics.hisense.smartdeals.com.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import electronics.hisense.smartdeals.com.HomeActivity;
import electronics.hisense.smartdeals.com.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private Button tv,mobile,laptop,hometheatre,dstv,cables,system,ps;
    private Button checkNewOrders,maintainProducts,userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tv=findViewById(R.id.btn_tv);
        mobile=findViewById(R.id.btn_mobile);
        laptop=findViewById(R.id.btn_laptop);
        hometheatre=findViewById(R.id.btn_hometheatre);
        dstv=findViewById(R.id.btn_dstv);
        cables=findViewById(R.id.btn_cables);
        system=findViewById(R.id.btn_system);
        ps=findViewById(R.id.btn_ps);
//        userinfo=findViewById(R.id.usersInfo_btn);
//        checkNewOrders=findViewById(R.id.check_enquiry_btn);
        maintainProducts=findViewById(R.id.maintain_btn);

//        userinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(AdminCategoryActivity.this, UsersInfoActivity.class);
//                startActivity(intent);
//            }
//        });

        maintainProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

//
//        checkNewOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
//                startActivity(intent);
//            }
//        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","TV");
                startActivity(intent);
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","MOBILE");
                startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","LAPTOP");
                startActivity(intent);
            }
        });
        hometheatre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","HOME THEATRE");
                startActivity(intent);
            }
        });
        dstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","DSTV");
                startActivity(intent);
            }
        });
        cables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","CABLES");
                startActivity(intent);
            }
        });
        system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","SYSTEM");
                startActivity(intent);
            }
        });
        ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("Category","PLAYSTATION");
                startActivity(intent);
            }
        });

    }

}
