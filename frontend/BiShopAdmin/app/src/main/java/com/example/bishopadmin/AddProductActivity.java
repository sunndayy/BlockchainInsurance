package com.example.bishopadmin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button btnPickImage;
    private ImageView imageView;
    public static final int PICK_IMAGE = 1;
    private Button btnAddProduct;
    private ProgressBar progressBar;
    private File uploadFile = null;
    private EditText edtId, edtName, edtDes, edtPrice, edtAmount, edtProducer;
    private int typeProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progess);
        progressBar.setVisibility(View.GONE);

        edtId = (EditText) findViewById(R.id.edt_add_id);
        edtName = (EditText) findViewById(R.id.edt_add_name);
        edtDes = (EditText) findViewById(R.id.edt_add_describe);
        edtPrice = (EditText) findViewById(R.id.edt_add_price);
        edtAmount = (EditText) findViewById(R.id.edt_add_amount);
        edtProducer = (EditText) findViewById(R.id.edt_add_producer);

        spinner = (Spinner) findViewById(R.id.spinner_type);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Xe số");
        categories.add("Xe tay ga");
        categories.add("Xe côn tay");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        btnPickImage = (Button) findViewById(R.id.btn_add_image);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermistion();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh sản phẩm"), PICK_IMAGE);
            }
        });

        imageView = (ImageView) findViewById(R.id.image_product);

        btnAddProduct = (Button) findViewById(R.id.btn_form_add);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                ApiService apiService = ApiUtils.getApiService();

                RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), edtId.getText().toString());
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString());
                RequestBody describe = RequestBody.create(MediaType.parse("multipart/form-data"), edtDes.getText().toString());
                RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(typeProduct));
                RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), edtPrice.getText().toString());
                RequestBody amount = RequestBody.create(MediaType.parse("multipart/form-data"), edtAmount.getText().toString());
                RequestBody producer = RequestBody.create(MediaType.parse("multipart/form-data"), edtProducer.getText().toString());

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);
                MultipartBody.Part image = MultipartBody.Part.createFormData("image", uploadFile.getName(), requestFile);

                apiService.CreateProduct(
                        Common.AccessToken, image, id, name, describe, type, price, amount, producer
                ).enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        Log.d("asd", "asdsad");
                        if (response.body().getId() != null) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddProductActivity.this, "successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddProductActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        typeProduct = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == AddProductActivity.this.RESULT_OK) {
            //TODO: action
            Glide.with(AddProductActivity.this).load(data.getData())
                    .into(imageView);
            uploadFile = new File(Common.getPathFromUri(AddProductActivity.this, data.getData()));
        }
    }

    private void checkPermistion() {
        if (ContextCompat.checkSelfPermission(AddProductActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(AddProductActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }


}
