package electronics.hisense.smartdeals.com.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtServiceName,txtServiceDescription,txtServicePrice,txtServiceLocation,txtServiceStatus;
    public ImageView imageView;
    public ItemClickListener listener;

    public ItemViewHolder(@NonNull View itemView)
    {
        super(itemView);

//        imageView=(ImageView) itemView.findViewById(R.id.con_service_image);
//        txtServiceDescription=(TextView) itemView.findViewById(R.id.con_service_description);
//        txtServiceName=(TextView) itemView.findViewById(R.id.con_service_name);
//        txtServicePrice=(TextView) itemView.findViewById(R.id.con_service_price);
//        txtServiceLocation=(TextView) itemView.findViewById(R.id.con_service_location);
//        txtServiceStatus=(TextView)itemView.findViewById(R.id.con_service_state);
    }

    @Override
    public void onClick(View view)
    {
        ItemClickListener.onClick(view,getAdapterPosition(),false);

    }
    public  void setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }


}

