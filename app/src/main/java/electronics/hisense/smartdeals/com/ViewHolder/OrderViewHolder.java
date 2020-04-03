package electronics.hisense.smartdeals.com.ViewHolder;


import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import electronics.hisense.smartdeals.com.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtServiceName, txtServicePrice;
    private AdapterView.OnItemClickListener onItemClickListener;

    public OrderViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtServiceName=itemView.findViewById(R.id.order_product_name);
        txtServicePrice=itemView.findViewById(R.id.order_price);

    }

    @Override
    public void onClick(View view)
    {
        ItemClickListener.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener)
    {

        this.onItemClickListener = itemClickListener;
    }
}
