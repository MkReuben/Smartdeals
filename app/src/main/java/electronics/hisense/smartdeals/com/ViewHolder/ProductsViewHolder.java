package electronics.hisense.smartdeals.com.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import electronics.hisense.smartdeals.com.R;

public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

{

    public TextView txtServiceName,txtServiceDescription,txtServicePrice,txtServiceLocation;
    public ImageView imageView;
    public ItemClickListener listener;
    private AdapterView.OnItemClickListener ItemClickListener;


    public ProductsViewHolder(@NonNull View itemView)
    {

        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.product_image_smartdeals);
        txtServiceDescription=(TextView) itemView.findViewById(R.id.product_description);
        txtServiceName=(TextView) itemView.findViewById(R.id.product_name);
        txtServicePrice=(TextView) itemView.findViewById(R.id.product_price);
        txtServiceLocation=(TextView) itemView.findViewById(R.id.product_location);

    }
 public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener)
 {
     this.ItemClickListener=itemClickListener;
 }
    @Override
    public void onClick(View v)
    {

        //ItemClickListener.onItemClick(getAdapterPosition();
    }
    public  void setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }
}
