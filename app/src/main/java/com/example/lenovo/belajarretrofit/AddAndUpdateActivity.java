package com.example.lenovo.belajarretrofit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.belajarretrofit.constant.Constant;
import com.example.lenovo.belajarretrofit.model.Item;
import com.example.lenovo.belajarretrofit.model.Result;
import com.example.lenovo.belajarretrofit.service.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAndUpdateActivity extends AppCompatActivity {

    EditText edtName, edtBrand, edtPrice;
    Button btnSubmit;

    private Item item;
    private int position;
    private boolean isEdit = false;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_update);

        edtName = findViewById(R.id.edt_Name);
        edtBrand = findViewById(R.id.edt_Brand);
        edtPrice = findViewById(R.id.edt_Price);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    editData();
                }else{
                    addNewData();
                }
            }
        });

        item = getIntent().getParcelableExtra("item");

        if(item != null){
            position = getIntent().getIntExtra("position", 0);
            isEdit = true;
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            if (item != null) {
                edtName.setText(item.getName());
                edtBrand.setText(item.getBrand());
                edtPrice.setText("" + item.getPrice());
            }
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btnTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;

        String dialogTitle, dialogMessage;

        if(isDialogClose){
            dialogTitle = "Cancel";
            dialogMessage = "Do you want to cancel ?";
        }else{
            dialogTitle = "Delete";
            dialogMessage = "DAre you sure to delete this item ?";
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isDialogClose){
                                finish();
                            }else{
                                deleteItem(item.getId());
                            }
                        }
                    })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertID = alertDialog.create();
        alertID.show();
    }

    private void deleteItem(int id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.delete(Constant.TOKEN, id);

        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                Result jsonResult = response.body();

                Toast.makeText(AddAndUpdateActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();

                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void addNewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        String name = edtName.getText().toString().trim();
        String brand = edtBrand.getText().toString().trim();
        Integer price = Integer.parseInt(edtPrice.getText().toString().trim());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.URL_API).addConverterFactory(GsonConverterFactory.create()).build();

        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.create(Constant.TOKEN, name, brand, price);

        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                Result jsonResult = response.body();
                Toast.makeText(AddAndUpdateActivity.this, jsonResult.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void editData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();


        String name = edtName.getText().toString().trim();
        String brand = edtBrand.getText().toString().trim();
        Integer price = Integer.parseInt(edtPrice.getText().toString().trim());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        final Call<Result> result = apiService.update(Constant.TOKEN, item.getId(), name, brand, price);

        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                Result jsonResult = response.body();
                Log.d("MainActivity", jsonResult.toString());

                Toast.makeText(AddAndUpdateActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();

                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }


}
