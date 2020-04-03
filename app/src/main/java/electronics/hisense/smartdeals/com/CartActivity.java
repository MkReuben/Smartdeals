package electronics.hisense.smartdeals.com;

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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Prevalent.Prevalent;
import electronics.hisense.smartdeals.com.Model.Order;
import electronics.hisense.smartdeals.com.ViewHolder.OrderViewHolder;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalAmount,txtmsgone;

    private  int appTotalPrice= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtmsgone=findViewById(R.id.message_one);
        recyclerView=findViewById(R.id.order_list);
        layoutManager=new LinearLayoutManager(this);
        nextProcessBtn=findViewById(R.id.next_process_btn);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalAmount=findViewById(R.id.total_price);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(CartActivity.this, ConfirmOrderActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart()

    {
        super.onStart();
        CheckOrderState();


        final DatabaseReference enquireListRef= FirebaseDatabase.getInstance().getReference().child("Order List");

        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(enquireListRef.child("User view")
                                .child(Prevalent.currentonlineUsers.getPhone()).child("Products"),Order.class).build();


        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter
                =new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder eholder, int position , @NonNull final Order model)
            {
                eholder.txtServiceName.setText(model.getname());
                eholder.txtServicePrice.setText("Price"+model.getPrice()+"Ksh");




                eholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "No",
                                        "Yes"

                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Remove Product ");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which==0)
                                {
                                    Intent intent=new Intent(CartActivity.this,CartActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }
                                if (which==1)
                                {
                                    enquireListRef.child("User view")
                                            .child(Prevalent.currentonlineUsers.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Product removed successfully", Toast.LENGTH_SHORT).show();

                                                        Intent intent=new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });

                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitems_layout,parent,false);
                OrderViewHolder eholder=new OrderViewHolder(view);
                return eholder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private  void CheckOrderState()
    {
        DatabaseReference enquiryRef;
        enquiryRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineUsers.getPhone());

        enquiryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    String userName=dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("received"))
                    {
                        txtTotalAmount.setText("Dear"+userName+"\n order is received successfully");
                        recyclerView.setVisibility(View.GONE);
                        txtmsgone.setText("Congratulations,your final order has been received successfully.Soon you will receive the feedback");
                        txtmsgone.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can order more products once your first order is received or confirmed", Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingState.equals("pending"))
                    {
                        txtTotalAmount.setText("Service State = Pending");
                        recyclerView.setVisibility(View.GONE);
                        txtmsgone.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can order more products once your first order is received or confirmed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}





