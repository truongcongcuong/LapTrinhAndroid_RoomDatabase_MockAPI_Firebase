package com.example.laptrinhandroid_roomdatabase_mockapi_firebase;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodAdapter extends RecyclerView.Adapter<WoodAdapter.ViewHolder> {
    List<Wood> woods;
    Context context;
    WoodDAO dao;
    String url = "https://60ad9ae180a61f00173313b8.mockapi.io/wood";

    public WoodAdapter(List<Wood> woods, Context context, WoodDAO dao) {
        this.woods = woods;
        this.context = context;
        this.dao =dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.line_item,parent,false);
        return new ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wood wood = woods.get(position);
        holder.txt_li_id.setText("id: "+wood.getId());
        holder.txt_li_country.setText("country: "+ wood.getCountry());
        holder.txt_li_price.setText("price: "+wood.getPrice());
        holder.txt_li_type.setText("type: "+ wood.getType());

        holder.ibt_li_delete.setImageResource(R.drawable.ic_baseline_block_24);
        holder.ibt_li_update.setImageResource(R.drawable.ic_baseline_update_24);
        holder.itemView.setOnClickListener(v->{
            SendingData sendingData = (SendingData) context;
            sendingData.sendData(wood);
        });

        holder.ibt_li_delete.setOnClickListener(v->{
            deleteFromMock(wood);
            dao.delete(wood);
            woods.remove(wood);
            notifyDataSetChanged();
        });

        holder.ibt_li_update.setOnClickListener(v->{
            showDialogForm(position);
        });
    }

    @Override
    public int getItemCount() {
        return woods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WoodAdapter adapter;
        ImageButton  ibt_li_delete, ibt_li_update;
        TextView txt_li_id, txt_li_type, txt_li_price, txt_li_country;
        public ViewHolder(@NonNull View itemView, WoodAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            ibt_li_delete = itemView.findViewById(R.id.ibt_li_delete);
            txt_li_id = itemView.findViewById(R.id.txt_li_id);
            txt_li_type = itemView.findViewById(R.id.txt_li_type);
            txt_li_price = itemView.findViewById(R.id.txt_li_price);
            txt_li_country = itemView.findViewById(R.id.txt_li_country);
            ibt_li_update = itemView.findViewById(R.id.ibt_li_update);
        }
    }

    private void showDialogForm(int position){
        Wood wood = woods.get(position);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText txt_type = new EditText(context);
        txt_type.setHint("Type");
        txt_type.setText(wood.getType());
        linearLayout.addView(txt_type);
        final EditText txt_price = new EditText(context);
        linearLayout.addView(txt_price);
        txt_price.setHint("Price");
        txt_price.setText(wood.getPrice()+"");
        final EditText txt_country = new EditText(context);
        linearLayout.addView(txt_country);
        txt_country.setHint("Country");
        txt_country.setText(wood.getCountry());

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Update Wood")
                .setMessage("Change.....")
                .setView(linearLayout)
                .setPositiveButton("Update",(dialog, which) -> {
                    String type = txt_type.getText().toString();
                    woods.get(position).setType(type);
                    String price = txt_price.getText().toString();
                    woods.get(position).setPrice(Double.valueOf(price));
                    String country = txt_country.getText().toString();
                    woods.get(position).setCountry(country);
                    notifyDataSetChanged();
                    updateDataMock(woods.get(position));
                })
                .setNegativeButton("Cancel",null)
                .create();
        alertDialog.show();

    }

    private void deleteFromMock(Wood wood) {
        StringRequest request = new StringRequest(Request.Method.DELETE,url+"/"+wood.getId(),
                response -> {},error -> {});
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void updateDataMock(Wood woodU) {
        StringRequest request = new StringRequest(Request.Method.PUT, url +"/"+woodU.getId(),
                response -> {}, error -> {}
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("type",woodU.getType());
                map.put("price",String.valueOf(woodU.getPrice()));
                map.put("country",woodU.getCountry());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
