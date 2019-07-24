package com.example.lenovo.belajarretrofit;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.lenovo.belajarretrofit.adapter.ItemAdapter;
import com.example.lenovo.belajarretrofit.constant.Constant;
import com.example.lenovo.belajarretrofit.model.Item;
import com.example.lenovo.belajarretrofit.model.Result;
import com.example.lenovo.belajarretrofit.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvItem;
    FloatingActionButton fabAdd;
    ArrayList<Item> items = new ArrayList<Item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvItem = findViewById(R.id.rv_item);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.setHasFixedSize(true);
        fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllData();
    }

    private void loadAllData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.URL_API).addConverterFactory(GsonConverterFactory.create()).build();
        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.getAll(Constant.TOKEN);
        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                Result jsonResult = response.body();
                Log.d("MainActivity", jsonResult.toString());

                items = jsonResult.getItems();
                Log.d("MainActivity", "Size : " + items.size());

                ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this);
                rvItem.setAdapter(itemAdapter);

                if (items != null) {
                    itemAdapter.setListItem(items);
                }

            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("MainActivity", t.getMessage());
            }
        });
    }
}
