package com.example.selectametalsclad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {

    DatabaseReference aluminBoxDataBase,aluminBoxName,aluminProfileDataBase,aluminProfileName,
            aluminSheetDataBase,aluminSheetName,aluminPipeDataBase,aluminPipeName,locationDataBase,zinkSheetName,zinkSheetDataBase,
            nergaSheetName,nergaSheetDataBase;
    DatabaseReference metalBoxDataBase,metalBoxName,metalSheetGkDataBase,metalSheetGkName,
            metalSheetHkDataBase,metalSheetHkName,metalPipeDataBase,metalPipeName;
    Toolbar toolbar;
    Intent intent;
    String message,messageDataBase,messageParam;
    ListView listViewProduct;
    List<Product> listProduct;
    Product selectProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initDataBase();
        intent = getIntent();
        message = intent.getStringExtra("message");


        toolbar = findViewById(R.id.toolbar_list_product_activity);
        setColorToolBar(message);
        listViewProduct = findViewById(R.id.list_view_product);
        initListViewProduct();
        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectProduct = listProduct.get(position);
                goToNextActivity();
            }
        });



    }

    private void goToNextActivity() {
        intent = new Intent(ListProductActivity.this, PriemVidachaActivity.class);
        intent.putExtra("messageId",selectProduct.getId());
        intent.putExtra("messageDataBase",messageDataBase);
        intent.putExtra("messageMaterial",selectProduct.getMaterial());
        intent.putExtra("messageArticle",selectProduct.getArticle());
        intent.putExtra("messageName",selectProduct.getName());
        intent.putExtra("messageParam",selectProduct.getParam());
        intent.putExtra("messageTotal",selectProduct.getTotal());
        intent.putExtra("messageUnit",selectProduct.getUnit());
        intent.putExtra("messageLocation",selectProduct.getLocation());
        intent.putExtra("messageIndiTotal",selectProduct.getIndiTotal());
        intent.putExtra("message",message);
        startActivity(intent);
    }

    private void initListViewProduct() {
        DatabaseReference db = null;
        listProduct = new ArrayList<>();
        ProductViewAdapter adapter = new ProductViewAdapter(listProduct,this);
        listViewProduct.setAdapter(adapter);
        messageParam = intent.getStringExtra("messageParam");
        messageDataBase = intent.getStringExtra("messageDataBase");
        switch (messageDataBase){
            case "aluminProfileDataBase":{
                db = aluminProfileDataBase;
                break;
            }
            case "aluminBoxDataBase":{
                db = aluminBoxDataBase;
                break;
            }
            case "aluminPipeDataBase":{
                db = aluminPipeDataBase;
                break;
            }
            case "aluminSheetDataBase":{
                db = aluminSheetDataBase;
                break;
            }
            case "metalSheetGkDataBase":{
                db = metalSheetGkDataBase;
                break;
            }
            case "metalSheetHkDataBase":{
                db = metalSheetHkDataBase;
                break;
            }
            case "metalBoxDataBase":{
                db = metalBoxDataBase;
                break;
            }
            case "metalPipeDataBase":{
                db = metalPipeDataBase;
                break;
            }
            case "zinkSheetDataBase":{
                db = zinkSheetDataBase;
                break;
            }
            case "nergaSheetDataBase":{
                db = nergaSheetDataBase;
                break;
            }

        }
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              listProduct.clear();
              for(DataSnapshot ds:snapshot.getChildren()){
                  Product pr = ds.getValue(Product.class);
                  if(pr.getParam().equals(messageParam)){
                      listProduct.add(pr);

                  }
              }
              adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void initDataBase() {
        aluminBoxName = FirebaseDatabase.getInstance().getReference("aluminBoxName");
        aluminBoxDataBase = FirebaseDatabase.getInstance().getReference("aluminBoxDataBase");
        aluminProfileName = FirebaseDatabase.getInstance().getReference("aluminProfileame");
        aluminProfileDataBase = FirebaseDatabase.getInstance().getReference("aluminProfileDataBase");
        aluminSheetName = FirebaseDatabase.getInstance().getReference("aluminSheetName");
        aluminSheetDataBase = FirebaseDatabase.getInstance().getReference("aluminSheetDataBase");
        aluminPipeName = FirebaseDatabase.getInstance().getReference("aluminPipeName");
        aluminPipeDataBase = FirebaseDatabase.getInstance().getReference("aluminPipeDataBase");
        zinkSheetName = FirebaseDatabase.getInstance().getReference("zinkSheetName");
        zinkSheetDataBase = FirebaseDatabase.getInstance().getReference("zinkSheetDataBase");
        nergaSheetName = FirebaseDatabase.getInstance().getReference("nergaSheetName");
        nergaSheetDataBase = FirebaseDatabase.getInstance().getReference("nergaSheetDataBase");

        metalBoxName = FirebaseDatabase.getInstance().getReference("metalBoxName");
        metalBoxDataBase = FirebaseDatabase.getInstance().getReference("metalBoxDataBase");
        metalSheetGkName = FirebaseDatabase.getInstance().getReference("metalSheetGkName");
        metalSheetGkDataBase = FirebaseDatabase.getInstance().getReference("metalSheetGkDataBase");
        metalSheetHkName = FirebaseDatabase.getInstance().getReference("metalSheetHkName");
        metalSheetHkDataBase = FirebaseDatabase.getInstance().getReference("metalSheetHkDataBase");
        metalPipeName = FirebaseDatabase.getInstance().getReference("metalPipeName");
        metalPipeDataBase = FirebaseDatabase.getInstance().getReference("metalPipeDataBase");

        locationDataBase = FirebaseDatabase.getInstance().getReference("location");
    }
    private void setColorToolBar(String message) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        switch (message){
            case "priem":{
                getSupportActionBar().setTitle(R.string.priem);
                toolbar.setBackgroundColor(getResources().getColor(R.color.priem));
                break;
            }
            case "vidacha":{
                getSupportActionBar().setTitle(R.string.vidacha);
                toolbar.setBackgroundColor(getResources().getColor(R.color.vidacha));
                break;
            }
        }




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return false;
    }
}