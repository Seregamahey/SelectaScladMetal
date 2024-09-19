package com.example.selectametalsclad;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewProductActivity extends AppCompatActivity {
    Toolbar toolbar;

    Spinner spinnerMaterial,spinnerType,spinnerUnit,spinnerLocation;
    Button btnAdd;
    DatabaseReference aluminBoxDataBase,aluminBoxName,aluminProfileDataBase,aluminProfileName,
            aluminSheetDataBase,aluminSheetName,aluminPipeDataBase,aluminPipeName,locationDataBase,zinkSheetName,zinkSheetDataBase,
            nergaSheetName,nergaSheetDataBase;
    DatabaseReference metalBoxDataBase,metalBoxName,metalSheetGkDataBase,metalSheetGkName,
            metalSheetHkDataBase,metalSheetHkName,metalPipeDataBase,metalPipeName;
    EditText editTextParam,editTextArticle,editTextTotal,editTextIndiTotal;
    List<String> listLocation;
    private String id, material,article,name,param,location,total,unit,indiTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_product);


        initDataBase();
        toolbar = findViewById(R.id.toolbar_add_new_product_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.add_new_pr));



        spinnerMaterial = findViewById(R.id.spinner_material);
        spinnerType = findViewById(R.id.spinner_type);
        spinnerUnit = findViewById(R.id.spinner_unit);
        spinnerLocation = findViewById(R.id.spinner_location);

        editTextArticle = findViewById(R.id.edit_text_article);
        editTextParam = findViewById(R.id.edit_text_param);
        editTextTotal = findViewById(R.id.edit_text_total);
        editTextIndiTotal = findViewById(R.id.edit_text_indiTotal);

        btnAdd = findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewProduct();
            }
        });
        initSpinners();






    }



    private void addNewProduct() {
        material = spinnerMaterial.getSelectedItem().toString();
        name = spinnerType.getSelectedItem().toString();
        unit = spinnerUnit.getSelectedItem().toString();
        location = spinnerLocation.getSelectedItem().toString();

        article = editTextArticle.getText().toString();
        param = editTextParam.getText().toString();
        total = editTextTotal.getText().toString();
        indiTotal = editTextIndiTotal.getText().toString();


        if(material.equals("Алюминий") && name.equals("Бокс")){
            saveInDataBase(aluminBoxDataBase,aluminBoxName);
        }else if(material.equals("Алюминий") && name.equals("Труба")){
            saveInDataBase(aluminPipeDataBase,aluminPipeName);
        }else if(material.equals("Алюминий") && name.equals("Лист")){
            saveInDataBase(aluminSheetDataBase,aluminSheetName);
        }else if(material.equals("Алюминий") && name.equals("Профиль")){
            saveInDataBase(aluminProfileDataBase,aluminProfileName);
        }else if(material.equals("Железо") && name.equals("Бокс")){
            saveInDataBase(metalBoxDataBase,metalBoxName);
        }else if(material.equals("Железо") && name.equals("Труба")){
            saveInDataBase(metalPipeDataBase,metalPipeName);
        }else if(material.equals("Железо") && name.equals("Лист Гк")){
            saveInDataBase(metalSheetGkDataBase,metalSheetGkName);
        }else if(material.equals("Железо") && name.equals("Лист Хк")){
            saveInDataBase(metalSheetHkDataBase,metalSheetHkName);
        }else if(material.equals("Цинк") && name.equals("Лист")){
            saveInDataBase(zinkSheetDataBase,zinkSheetName);
        }else if(material.equals("Нержавейка") && name.equals("Лист")){
            saveInDataBase(nergaSheetDataBase,nergaSheetName);
        }

    }

    private void saveInDataBase(DatabaseReference dataBaseProduct, DatabaseReference dataBaseName) {
        id = dataBaseProduct.push().getKey();
        dataBaseProduct.child(id).setValue(new Product(id,material,article,name,param,location,total,unit,indiTotal));
        dataBaseName.push().setValue(param).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddNewProductActivity.this, "Создано", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initSpinners() {
        String[] material = {"Алюминий","Железо","Цинк","Нержавейка"};
        String [] type = {"Лист","Труба","Бокс","Пофиль","Лист Гк","Лист Хк"};
        String [] unit = {"кг","мм","шт","м"};
        ArrayAdapter<String> adapterMaterial = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,material);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,type);
        ArrayAdapter<String> adapterUnit = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,unit);


        spinnerMaterial.setAdapter(adapterMaterial);
        spinnerType.setAdapter(adapterType);
        spinnerUnit.setAdapter(adapterUnit);

        listLocation = new ArrayList<>();
        locationDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLocation.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    listLocation.add(ds.getValue(String.class));
                }
                initListLocation(listLocation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void initListLocation(List<String> listLocation) {
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listLocation);
        spinnerLocation.setAdapter(adapterLocation);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return false;
    }
}
