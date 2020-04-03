package electronics.hisense.smartdeals.com.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import electronics.hisense.smartdeals.com.Model.Order;
import electronics.hisense.smartdeals.com.R;
import electronics.hisense.smartdeals.com.ViewHolder.OrderViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference orderListRef;

    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);


        userID=getIntent().getStringExtra("uid");

        productsList=findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        orderListRef = FirebaseDatabase.getInstance().getReference()
                .child("Order List").child("Admin view").child(userID).child("Services");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Order> options=
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(orderListRef,Order.class)
                        .build();

        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter=new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Order model)
            {
                orderViewHolder.txtServiceName.setText(model.getname());
                orderViewHolder.txtServicePrice.setText("Price" + model.getPrice() + "Ksh");

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitems_layout,parent,false);
                OrderViewHolder eholder=new OrderViewHolder(view);
                return eholder;

            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();

    }
}
