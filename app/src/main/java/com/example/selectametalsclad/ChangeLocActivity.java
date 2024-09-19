package com.example.selectametalsclad;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangeLocActivity extends AppCompatActivity {


    DatabaseReference aluminBoxDataBase,aluminBoxName,aluminProfileDataBase,aluminProfileName,
            aluminSheetDataBase,aluminSheetName,aluminPipeDataBase,aluminPipeName,locationDataBase,zinkSheetName,zinkSheetDataBase,
            nergaSheetName,nergaSheetDataBase;
    DatabaseReference metalBoxDataBase,metalBoxName,metalSheetGkDataBase,metalSheetGkName,
            metalSheetHkDataBase,metalSheetHkName,metalPipeDataBase,metalPipeName;
    TextView textLocation,textSelectProduct, textChangeTotal,textNewLocation;
    Toolbar toolbar;
    List<String> listLoc,listProduct;
    List<Product> productsInLocation,productsInNewLocation;
    Product selectProduct,productInLoc;
    ListView listView;
    String location,changeTotal,newLocation,status;
    Button btnChangeLoc;
    Boolean bool;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_loc);

        initDataBase();
        status = "0";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("ПЕРЕМЕЩЕНИЕ");
        toolbar.setBackgroundColor(getResources().getColor(R.color.editor));

        listLoc = new ArrayList<>();
        listProduct = new ArrayList<>();
        productsInLocation = new ArrayList<>();
        productsInNewLocation = new ArrayList<>();
        initList();



        textLocation = findViewById(R.id.text_location);
        textLocation.setText("ПЕРЕМЕСТИТЬ ИЗ ");
        textSelectProduct = findViewById(R.id.text_select_product);
        textSelectProduct.setText("ЧТО ПЕРЕМЕСТИТЬ");
        textSelectProduct.setVisibility(View.INVISIBLE);
        textNewLocation = findViewById(R.id.text_new_location);
        textNewLocation.setText("КУДА ПЕРЕМЕСТИТЬ");
        textNewLocation.setVisibility(View.INVISIBLE);
        textChangeTotal = findViewById(R.id.text_change_total);
        textChangeTotal.setText("СКОЛЬКО ПЕРЕМЕСТИТЬ");
        textChangeTotal.setVisibility(View.INVISIBLE);
        btnChangeLoc = findViewById(R.id.btn_change_loc);
        btnChangeLoc.setVisibility(View.INVISIBLE);




        textSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeProduct();
            }
        });

        textLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeLoc();
            }
        });
        textNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeNewLoc();
            }
        });
        textChangeTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputChangeTotal();
            }
        });
        btnChangeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(selectProduct.getMaterial().equals("Алюминий") && selectProduct.getName().equals("Труба")){
                 addProductInNewLoc(aluminPipeDataBase);
             }else if ((selectProduct.getMaterial().equals("Алюминий")) && selectProduct.getName().equals("Бокс")){
                 addProductInNewLoc(aluminBoxDataBase);
             }else if ((selectProduct.getMaterial().equals("Алюминий")) && selectProduct.getName().equals("Лист")) {
                 addProductInNewLoc(aluminSheetDataBase);
             }  else if ((selectProduct.getMaterial().equals("Алюминий")) && selectProduct.getName().equals("Профиль")) {
                 addProductInNewLoc(aluminProfileDataBase);
             }else if ((selectProduct.getMaterial().equals("Железо")) && selectProduct.getName().equals("Бокс")) {
                 addProductInNewLoc(metalBoxDataBase);
             }else if ((selectProduct.getMaterial().equals("Железо")) && selectProduct.getName().equals("Лист Гк")) {
                 addProductInNewLoc(metalSheetGkDataBase);
             }else if ((selectProduct.getMaterial().equals("Железо")) && selectProduct.getName().equals("Лист Хк")) {
                 addProductInNewLoc(metalSheetHkDataBase);
             }else if ((selectProduct.getMaterial().equals("Железо")) && selectProduct.getName().equals("Труба")) {
                 addProductInNewLoc(metalPipeDataBase);
             }else if ((selectProduct.getMaterial().equals("Цинк")) && selectProduct.getName().equals("Лист")) {
                 addProductInNewLoc(zinkSheetDataBase);
             }else if ((selectProduct.getMaterial().equals("Нержавейка")) && selectProduct.getName().equals("Лист")) {
                 addProductInNewLoc(nergaSheetDataBase);
             }

            }
        });


    }



    private void addProductInNewLoc(DatabaseReference db) {
        bool = true;

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(bool){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product pr = ds.getValue(Product.class);
                        if(pr.getParam().equals(selectProduct.getParam()) && newLocation.equals(pr.getLocation())){
                            Double summ = Double.parseDouble(changeTotal) + Double.parseDouble(pr.getTotal());
                            db.child(pr.getId()).child("total").setValue(Double.toString(summ)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    finish();
                                }
                            });
                            Toast.makeText(ChangeLocActivity.this,"Перемещено", Toast.LENGTH_SHORT).show();
                            if(Double.parseDouble(selectProduct.getTotal()) == Double.parseDouble(changeTotal)){
                                db.child(selectProduct.getId()).removeValue();
                            }else {
                                Double minus = Double.parseDouble(selectProduct.getTotal()) - Double.parseDouble(changeTotal);
                                db.child(selectProduct.getId()).child("total").setValue(Double.toString(minus));
                            }
                            bool = false;
                            break;
                        }
                    }
                }
                if(bool){
                    String id = db.push().getKey();
                    db.child(id).setValue(new Product(id,selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName(),
                            selectProduct.getParam(),newLocation,changeTotal,selectProduct.getUnit(),selectProduct.getIndiTotal())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ChangeLocActivity.this, "Перемещено", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    if(Double.parseDouble(selectProduct.getTotal()) == Double.parseDouble(changeTotal)){
                        db.child(selectProduct.getId()).removeValue();
                    }else {
                        Double minus = Double.parseDouble(selectProduct.getTotal()) - Double.parseDouble(changeTotal);
                        db.child(selectProduct.getId()).child("total").setValue(Double.toString(minus));
                    }
                    bool = false;

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void showDialogChangeNewLoc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocActivity.this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_change_location,null);
        listView = rl.findViewById(R.id.listview);
        locationDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoc.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    listLoc.add(ds.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter newLocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listLoc);
        listView.setAdapter(newLocAdapter);

        builder.setView(rl);
        AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textNewLocation.setText("ПЕРЕМЕСТИТЬ В : " + listLoc.get(i));
                newLocation = listLoc.get(i);
                btnChangeLoc.setVisibility(View.VISIBLE);



                    dialog.dismiss();
                }

        });


    }

    private void showDialogChangeProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocActivity.this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_change_location,null);
        listView = rl.findViewById(R.id.listview);
        ArrayAdapter productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listProduct);
        listProduct.clear();
        aluminProfileDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+ ", кол-во "+product.getTotal()+product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        aluminBoxDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        aluminPipeDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        aluminSheetDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        metalSheetGkDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        metalSheetHkDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        metalPipeDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        metalBoxDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        zinkSheetDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        nergaSheetDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if(product.getLocation().equals(location)){
                        listProduct.add(product.getMaterial()+" "+product.getName()+" "+product.getParam()+
                                ", кол-во "+product.getTotal() + product.getUnit());
                        productsInLocation.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setAdapter(productAdapter);

        builder.setView(rl);
        AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textSelectProduct.setText("ПЕРЕМЕСТИТЬ: " + listProduct.get(i));
                selectProduct = productsInLocation.get(i);
                textChangeTotal.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

    }

    private void inputChangeTotal() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(ChangeLocActivity.this);
        View lView = getLayoutInflater().inflate(R.layout.dialog_add_new_loc,null);
        EditText edtTxtChangeTotal = lView.findViewById(R.id.edt_txt_new_location);
        edtTxtChangeTotal.setHint("Введите кол-во");
        lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(edtTxtChangeTotal.getText().toString())){
                    Toast.makeText(ChangeLocActivity.this, "введите кол-во в "+selectProduct.getUnit(), Toast.LENGTH_SHORT).show();
                    return;
                }changeTotal = edtTxtChangeTotal.getText().toString();
                textChangeTotal.setText("ПЕРЕМЕСТИТЬ  "+ changeTotal+selectProduct.getUnit());
                textNewLocation.setVisibility(View.VISIBLE);


            }
        });


        lBuilder.setView(lView);
        AlertDialog dialog = lBuilder.create();
        dialog.show();
    }

    private void initList() {
        listLoc.clear();
        locationDataBase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    listLoc.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialogChangeLoc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocActivity.this);
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_change_location,null);
        listView = rl.findViewById(R.id.listview);
        locationDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoc.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    listLoc.add(ds.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listLoc);
        listView.setAdapter(locAdapter);

        builder.setView(rl);
        AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textLocation.setText("ПЕРЕМЕСТИТЬ ИЗ: " + listLoc.get(i));
                location = listLoc.get(i).toString();
                textSelectProduct.setVisibility(View.VISIBLE);
                dialog.dismiss();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return false;
    }


}