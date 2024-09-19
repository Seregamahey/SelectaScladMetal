package com.example.selectametalsclad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnPriem,btnVidacha,btnEditor,btnInfo,btnAddNewProduct,btnMotoClock,btnZametki;
    Intent intent;
    DatabaseReference locationDataBase,motoClockDataBase,zametkiDataBase;
    String clock ="";
    List<String> listStringZametki;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPriem = findViewById(R.id.btn_priem);
        btnVidacha = findViewById(R.id.btn_vidacha);
        btnEditor = findViewById(R.id.btn_editor);
        btnInfo = findViewById(R.id.btn_info);
        btnMotoClock = findViewById(R.id.btn_moto_clock);
        btnZametki = findViewById(R.id.btn_zametki);

        btnAddNewProduct = findViewById(R.id.btn_add_new_pr);
        locationDataBase = FirebaseDatabase.getInstance().getReference("location");
        motoClockDataBase = FirebaseDatabase.getInstance().getReference("motoClockDatabase");
        zametkiDataBase = FirebaseDatabase.getInstance().getReference("zametkiDataBase");
        motoClockDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    clock = ds.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnPriem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenuActivity("priem");
            }
        });
        btnVidacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenuActivity("vidacha");
            }
        });
        btnEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,ChangeLocActivity.class);
                startActivity(intent);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              addNewLocation();
            }
        });
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AddNewProductActivity.class);
                startActivity(intent);
            }
        });
        btnMotoClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String key = "-O6_XszlN5vDpPjeHVft";
            changeMotoclock(key,clock);
            }
        });
        btnZametki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });




    }

    private void changeMotoclock(String key, String clock) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(MainActivity.this);
        View lView = getLayoutInflater().inflate(R.layout.dialog_add_new_loc,null);
        EditText edtTxtNewLocation = lView.findViewById(R.id.edt_txt_new_location);
        edtTxtNewLocation.setHint(clock);
        lBuilder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(edtTxtNewLocation.getText().toString())){
                    Toast.makeText(MainActivity.this, "Ведите часы", Toast.LENGTH_SHORT).show();
                    return;
                }
                motoClockDataBase.child(key).setValue(edtTxtNewLocation.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Часы изменены", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        lBuilder.setView(lView);
        AlertDialog dialog = lBuilder.create();
        dialog.show();
    }


    private void addNewLocation() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(MainActivity.this);
        View lView = getLayoutInflater().inflate(R.layout.dialog_add_new_loc,null);
        EditText edtTxtNewLocation = lView.findViewById(R.id.edt_txt_new_location);
        lBuilder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(edtTxtNewLocation.getText().toString())){
                    Toast.makeText(MainActivity.this, "Ведите место", Toast.LENGTH_SHORT).show();
                    return;
                }
                locationDataBase.push().setValue(edtTxtNewLocation.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Место добавлено", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        lBuilder.setView(lView);
        AlertDialog dialog = lBuilder.create();
        dialog.show();
    }

    private void goToMenuActivity(String message) {
    intent = new Intent(MainActivity.this,MenuActivity.class);
    intent.putExtra("message",message);
    startActivity(intent);

    }
}