package com.example.lenovo.belajarretrofit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.belajarretrofit.R;
import com.example.lenovo.belajarretrofit.model.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final ArrayList<Item> listItem = new ArrayList<Item>();
    private Context context;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public void setListItem(ArrayList<Item> listItem) {

        if (listItem.size() > 0) {
            this.listItem.clear();

        }

        this.listItem.addAll(listItem);
        notifyDataSetChanged();

    }

    public void addItem(Item note) {
        this.listItem.add(note);
        notifyItemInserted(listItem.size() - 1);
    }

    public void updateItem(int position, Item item) {
        this.listItem.set(position, item);
        notifyItemChanged(position, item);
    }

    public void removeItem(int position) {
        this.listItem.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listItem.size());
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.tvItemName.setText(listItem.get(position).getName());
        holder.tvItemBrand.setText(listItem.get(position).getBrand());
        holder.tvItemPrice.setText("" + listItem.get(position).getPrice());

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Name : " + listItem.get(position).getName(), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(context, AddItemActivity.class);
//                intent.putExtra("position",position);
//                intent.putExtra("item",listItem.get(position));
//                context.startActivity(intent);


            }
        });

        holder.cvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemBrand, tvItemPrice;

        CardView cvItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemBrand = itemView.findViewById(R.id.tv_item_brand);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            cvItem = itemView.findViewById(R.id.cv_item);
        }
    }
}
