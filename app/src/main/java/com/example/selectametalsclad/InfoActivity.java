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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnNewZametka,btnAddNewZametki,btnDelete;
    String name;
    ListView listViewZametki;
    DatabaseReference zametkiDataBase;
    EditText editTextZametka,editTextSelectZametka;
    List<String> listStringZametka;
    Zametki selectZametka;
    String selectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        toolbar = findViewById(R.id.toolbar_info_activity);
        btnNewZametka = findViewById(R.id.btn_new_zametka);
        btnAddNewZametki = findViewById(R.id.btn_add_new_zametka);
        editTextZametka = findViewById(R.id.edit_text_zametka);
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.INVISIBLE);
        btnAddNewZametki.setVisibility(View.INVISIBLE);
        listViewZametki = findViewById(R.id.list_view_zametki);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.zametka));
        getSupportActionBar().setTitle(R.string.zametki);
        zametkiDataBase = FirebaseDatabase.getInstance().getReference("zametkiDataBase");
        listStringZametka = new ArrayList<>();
        zametkiDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Zametki zam = ds.getValue(Zametki.class);
                    listStringZametka.add(zam.getNameZametki());
                }
                initListViewZametki(listStringZametka);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnNewZametka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder lBuilder = new AlertDialog.Builder(InfoActivity.this);
                View lView = getLayoutInflater().inflate(R.layout.dialog_add_new_loc,null);
                EditText edtTxtNewLocation = lView.findViewById(R.id.edt_txt_new_location);
                edtTxtNewLocation.setHint("Введите название");
                lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(TextUtils.isEmpty(edtTxtNewLocation.getText().toString())){
                            Toast.makeText(InfoActivity.this, "Ведите место", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        name = edtTxtNewLocation.getText().toString();
                        addNewZametka(name);
                    }
                });


                lBuilder.setView(lView);
                AlertDialog dialog = lBuilder.create();
                dialog.show();


            }
        });
        listViewZametki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              name = listStringZametka.get(position);
                initTextZametki(name);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zametkiDataBase.child(selectZametka.getIdZametki()).removeValue();
                Toast.makeText(InfoActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                finish();
            }

        });



    }

    private void initTextZametki(String name) {
        zametkiDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Zametki zam = ds.getValue(Zametki.class);
                    if(zam.getNameZametki().equals(name)){
                        selectZametka = zam;
                        showAllertDialog(selectZametka);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAllertDialog(Zametki selectZametka) {
        listViewZametki.setVisibility(View.INVISIBLE);
        editTextZametka.setText(selectZametka.getTextZametki());
        btnNewZametka.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
    }

    private void initListViewZametki(List<String> listStringZametka) {
            ArrayAdapter adapterZametki = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listStringZametka);
            listViewZametki.setAdapter(adapterZametki);
        }


    private void addNewZametka(String name) {

        listViewZametki.setVisibility(View.INVISIBLE);
        btnNewZametka.setVisibility(View.INVISIBLE);
        String id = zametkiDataBase.push() .getKey();
        btnAddNewZametki.setVisibility(View.VISIBLE);
        btnAddNewZametki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textZametki = editTextZametka.getText().toString();
                zametkiDataBase.child(id).setValue(new Zametki(id,name,textZametki));
                Toast.makeText(InfoActivity.this, "OK", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




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