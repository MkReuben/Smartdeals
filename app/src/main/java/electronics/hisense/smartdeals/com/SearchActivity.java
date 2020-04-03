package electronics.hisense.smartdeals.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import electronics.hisense.smartdeals.com.Model.Products;
import electronics.hisense.smartdeals.com.ViewHolder.ProductsViewHolder;

public class SearchActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchlist;
    private String SearchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inputText=findViewById(R.id.search_product_name);
        searchBtn=findViewById(R.id.search_btn);
        searchlist=findViewById(R.id.search_list);
        searchlist.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)


            {
                SearchInput=inputText.getText().toString().toUpperCase();
                onStart();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("name").startAt(SearchInput), Products.class)

                        .build();

        FirebaseRecyclerAdapter<Products, ProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductsViewHolder Holder, int i, @NonNull final Products model)
                    {
                        Holder.txtServiceName.setText(model.getName());
                        Holder.txtServiceDescription.setText(model.getDescription());
                        Holder.txtServicePrice.setText("Price"+model.getPrice()+ " Ksh");
                        Holder.txtServiceLocation.setText(model.getLocation());
                        Picasso.get().load(model.getImage()).into(Holder.imageView);

                        Holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent =new Intent(SearchActivity.this, ProductsDetailsActivity.class);
                                intent.putExtra("sid",model.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductsViewHolder holder= new ProductsViewHolder(view);
                        return holder;
                    }
                };

        searchlist.setAdapter(adapter);
        adapter.startListening();



    }
}

