package electronics.hisense.smartdeals.com.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import electronics.hisense.smartdeals.com.Model.AdminUsersInfo;
import electronics.hisense.smartdeals.com.R;

public class UsersInfoActivity extends AppCompatActivity {

    private RecyclerView userDataList;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        userDataList=findViewById(R.id.user_data_list);
        userDataList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart()

    {
        super.onStart();

        FirebaseRecyclerOptions<AdminUsersInfo> options=
                new FirebaseRecyclerOptions.Builder<AdminUsersInfo>()
                        .setQuery(userRef,AdminUsersInfo.class)
                        .build();

        FirebaseRecyclerAdapter<AdminUsersInfo,UserViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminUsersInfo, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull AdminUsersInfo adminUsersInfo)
                    {
                        userViewHolder.userName.setText("Name:"+adminUsersInfo.getName());
                        userViewHolder.userLocation.setText("Location:"+adminUsersInfo.getAddress());
                        userViewHolder.userPhone.setText("Phone:"+adminUsersInfo.getPhone());
                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_data_layout, parent, false);
                        return new UserViewHolder(view);
                    }
                };

        userDataList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {

        public TextView userName,userPhone,userLocation;

        public UserViewHolder(@NonNull View itemView)

        {
            super(itemView);

            userName=itemView.findViewById(R.id.user_data_user_name);
            userPhone=itemView.findViewById(R.id.user_data_phone_number);
            userLocation=itemView.findViewById(R.id.user_location_data);


        }
    }
}