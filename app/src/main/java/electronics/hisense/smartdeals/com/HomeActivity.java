package electronics.hisense.smartdeals.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import electronics.hisense.smartdeals.com.Admin.AdminMaintainActivity;
import electronics.hisense.smartdeals.com.Model.Products;
import electronics.hisense.smartdeals.com.ViewHolder.ProductsViewHolder;
import electronics.hisense.smartdeals.com.ui.home.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ProductsRef;
    private TextView userNameTextView;
    private String type ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null)
        {
            type=getIntent().getExtras().get("Admin").toString();
        }

       // userNameTextView=(TextView)findViewById(R.id.user_profile_image);


        HomeFragment homeFragment=new HomeFragment();
//        AccountFragment accountFragment=new AccountFragment();
//        LogoutFragment logoutFragment =new LogoutFragment();
//        SearchFragment searchFragment =new SearchFragment();


        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        ProductsRef.keepSynced(true);
        recyclerView=findViewById(R.id.recycler_smartdeals);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con,
                new HomeFragment()).commit();





    }





    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductsViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder, int i, @NonNull final Products services)
                    {
                        productsViewHolder.txtServiceName.setText(services.getName());
                        productsViewHolder.txtServiceDescription.setText(services.getDescription());
                        productsViewHolder.txtServicePrice.setText("Price"+ " Ksh "+services.getPrice());
                        productsViewHolder.txtServiceLocation.setText(services.getLocation());
                        Picasso.get().load(services.getImage()).into( productsViewHolder.imageView);




                        productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                if (type.equals("Admin"))
                                {

                                   Intent intent= new Intent(HomeActivity.this, AdminMaintainActivity.class);
                                   intent.putExtra("pid",services.getPid());
                                 startActivity(intent);

                                }
                                else
                               {
                                 Intent intent=new Intent(HomeActivity.this,ProductsDetailsActivity.class);
                                  intent.putExtra("pid",services.getPid());
                                   startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        ProductsViewHolder holder=new ProductsViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;


                    switch (menuItem.getItemId())
                    {
                        case R.id.navigation_home:
                            selectedFragment =new HomeFragment();
                            break;
                        case R.id.navigation_search:
                            if (!type.equals("Admin"))
                            {
                                Intent intent=new Intent(HomeActivity.this, SearchActivity.class);
                                startActivity(intent);
                                break;
                            }

                        case R.id.navigation_account:

                            if (!type.equals("Admin")) {
                               Intent start=new Intent(HomeActivity.this, AccountActivity.class);
                              startActivity(start);
                               break;
                            }
//                        case R.id.navigation_settings:
//
//                            if (!type.equals("Admin"))
//                            {
//                               Intent inten = new Intent(HomeActivity.this,SettingsActivity.class);
//                              startActivity(inten);
//                              break;
//                            }
//                        case R.id.navigation_cart:
//
//                            if (!type.equals("Admin"))
//                            {
//                             Intent cart = new Intent(HomeActivity.this,CartActivity.class);
////                                startActivity(cart);
////                              break;
//                            }
                    }



                    return true;
                }
            };
}
