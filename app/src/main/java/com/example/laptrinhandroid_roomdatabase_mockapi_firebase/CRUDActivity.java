package com.example.laptrinhandroid_roomdatabase_mockapi_firebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRUDActivity extends AppCompatActivity implements SendingData{
    AppDatabase database;
    FirebaseAuth auth;
    WoodDAO dao;
    EditText edt_crud_id,edt_crud_price,edt_crud_type,edt_crud_country;
    Button btn_main_add,btn_main_delete,btn_main_update,btn_main_search,btn_main_logout;
    RecyclerView rcv_crud_woods;
    String url = "https://60ad9ae180a61f00173313b8.mockapi.io/wood";
    String type,country,price;
    List<Wood> woods;
    WoodAdapter adapter;
    Wood woodU = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r_u_d);

        database = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"wood-db-test")
                .allowMainThreadQueries()
                .build();
        dao = database.woodDAO();
        auth = FirebaseAuth.getInstance();
//        woods = dao.findALL();


        edt_crud_id = findViewById(R.id.edt_crud_id);
        edt_crud_price = findViewById(R.id.edt_crud_price);
        edt_crud_type = findViewById(R.id.edt_crud_type);
        edt_crud_country = findViewById(R.id.edt_crud_country);
        btn_main_add = findViewById(R.id.btn_main_add);
        btn_main_delete = findViewById(R.id.btn_main_delete);
        btn_main_update = findViewById(R.id.btn_main_update);
        btn_main_search = findViewById(R.id.btn_main_search);
        btn_main_logout = findViewById(R.id.btn_main_logout);
        rcv_crud_woods = findViewById(R.id.rcv_crud_woods);
        edt_crud_id.setEnabled(false);

        setAdapterToRCV();
        rcv_crud_woods.setLayoutManager(new GridLayoutManager(CRUDActivity.this,1));

        btn_main_logout.setOnClickListener(v->{
            auth.signOut();
            finish();
        });

        btn_main_add.setOnClickListener(v->{
            validData();
            addNewToMock();
            getNewWoodFromMockAddToRoomDB();
            setAdapterToRCV();
        });

        btn_main_delete.setOnClickListener(v->{
            if(woodU!=null){
                dao.delete(woodU);
                deleteFromMock();
                setAdapterToRCV();
                woodU=null;
            }
        });

        btn_main_update.setOnClickListener(v->{
            if(woodU!=null || validData()){
                woodU.setCountry(edt_crud_country.getText().toString());
                woodU.setPrice(Double.parseDouble(edt_crud_price.getText().toString()));
                woodU.setType(edt_crud_type.getText().toString());
                dao.update(woodU);
                updateDataMock(woodU);
                setAdapterToRCV();
                woodU=null;
            }
        });


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
        RequestQueue queue = Volley.newRequestQueue(CRUDActivity.this);
        queue.add(request);
    }

    private void deleteFromMock() {
        StringRequest request = new StringRequest(Request.Method.DELETE,url+"/"+woodU.getId(),
                response -> {},error -> {});
        RequestQueue queue = Volley.newRequestQueue(CRUDActivity.this);
        queue.add(request);
    }

    private void setAdapterToRCV() {
        this.adapter = new WoodAdapter(dao.findALL(),CRUDActivity.this,dao);
        this.rcv_crud_woods.setAdapter(adapter);
    }



    private void getNewWoodFromMockAddToRoomDB() {
        woods = dao.findALL();
        int id;
        if(this.woods.size()==0)
            id = 1;
        else
            id = woods.get(woods.size()-1).getId()+1;
       dao.addNew(new Wood(id,type,Double.parseDouble(price),country));

    }

    private void addNewToMock() {
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {}, error -> {}){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("type",type);
                map.put("price",price);
                map.put("country",country);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(CRUDActivity.this);
        queue.add(request);
    }



    private boolean validData() {
        this.type = edt_crud_type.getText().toString();
        if(TextUtils.isEmpty(type)){
            Toast.makeText(CRUDActivity.this,"Nhap type ....",Toast.LENGTH_SHORT).show();
            return false;
        }
        this.price = edt_crud_price.getText().toString();
        if(TextUtils.isEmpty(type)){
            Toast.makeText(CRUDActivity.this,"Nhap Price ....",Toast.LENGTH_SHORT).show();
            return false;
        }
        this.country = edt_crud_country.getText().toString();
        if(TextUtils.isEmpty(country)){
            Toast.makeText(CRUDActivity.this,"Nhap country ....",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void GetArrayJson(){
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                woods = new ArrayList<>();
                                for(int i=0; i<response.length(); i++){
                                    try {
                                        JSONObject object = (JSONObject) response.get(i);
                                        woods.add(new Wood(object.getInt("id"),
                                                object.getString("type"),object.getDouble("price")
                                        ,object.getString("contry")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CRUDActivity.this, "Error by get Json Array!", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void sendData(Serializable serializable) {
        woodU = (Wood) serializable;
        edt_crud_country.setText(woodU.getCountry());
        edt_crud_id.setText("id : "+woodU.getId());
        edt_crud_price.setText(woodU.getPrice()+"");
        edt_crud_type.setText(woodU.getType());
    }
}