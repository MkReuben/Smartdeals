package electronics.hisense.smartdeals.com.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import electronics.hisense.smartdeals.com.Model.AdminOrders;
import electronics.hisense.smartdeals.com.R;

public class AdminNewOrderActivity extends AppCompatActivity {
    private RecyclerView orderlist;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        orderlist=findViewById(R.id.admin_order_list);
        orderlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(orderRef,AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model)
                    {

                        holder.userName.setText("Name:" + model.getName());
                        holder.userPhoneNmber.setText("Phone:" +  model.getPhone());
                        holder.userLocation.setText("Service Location:" +  model.getLocation());
                        holder.userDateTime.setText("Enquiry at " +  model.getTime()  +  ""  +  model.getDate() );

                        holder.showOrderbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String uID=getRef(position).getKey();
                                Intent intent =new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);

                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[]=new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                                builder.setTitle("Have you completed the order");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which==0)
                                        {
                                            String uID=getRef(position).getKey();
                                            RemoveService(uID);

                                        }
                                        else {
                                            finish();
                                        }

                                    }
                                });
                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
                        return  new AdminOrdersViewHolder(view);
                    }
                };
        orderlist.setAdapter(adapter);
        adapter.startListening();
    }


    public  static  class  AdminOrdersViewHolder extends  RecyclerView.ViewHolder
    {

        public TextView userName,userPhoneNmber,userLocation,userDateTime;
        public Button showOrderbtn;

        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNmber=itemView.findViewById(R.id.order_phone_number);
            userLocation=itemView.findViewById(R.id.order_location);
            userDateTime=itemView.findViewById(R.id.order_date);
            showOrderbtn=itemView.findViewById(R.id.show_all_orders);

        }
    }
    private void RemoveService(String uID)
    {
        orderRef.child(uID).removeValue();
    }

}
