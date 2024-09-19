package com.example.selectametalsclad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

import java.text.DecimalFormat;

public class PriemVidachaActivity extends AppCompatActivity {
    Intent intent;
    Toolbar toolbar;
    TextView textViewMaterial,textViewArticle,textViewParam,textViewTotal;
    Button btnAdd,btnHelp;
    DatabaseReference aluminBoxDataBase,aluminBoxName,aluminProfileDataBase,aluminProfileName,
            aluminSheetDataBase,aluminSheetName,aluminPipeDataBase,aluminPipeName,locationDataBase,zinkSheetName,zinkSheetDataBase,
            nergaSheetName,nergaSheetDataBase;
    DatabaseReference metalBoxDataBase,metalBoxName,metalSheetGkDataBase,metalSheetGkName,
            metalSheetHkDataBase,metalSheetHkName,metalPipeDataBase,metalPipeName;
    String message,messageId,messageDataBase,messageMateria,messageArticle,messageName,messageParam,messageTotal,messageLocation,messageUnit,messageIndiTotal;
    Product selectProduct;
    EditText editTextTotal;
    Double newTotal,total,indiTotal,newT,t;
    DatabaseReference db = null;
    String subject,emailText;
    String[] addresses;
    Boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_priem_vidacha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initDataBase();


        intent = getIntent();
        bool = true;
        message = intent.getStringExtra("message");
        messageId = intent.getStringExtra("messageId");
        messageDataBase = intent.getStringExtra("messageDataBase");
        messageMateria = intent.getStringExtra("messageMaterial");
        messageArticle = intent.getStringExtra("messageArticle");
        messageName = intent.getStringExtra("messageName");
        messageParam = intent.getStringExtra("messageParam");
        messageTotal = intent.getStringExtra("messageTotal");
        messageUnit = intent.getStringExtra("messageUnit");
        messageLocation = intent.getStringExtra("messageLocation");
        messageIndiTotal = intent.getStringExtra("messageIndiTotal");




        toolbar = findViewById(R.id.toolbar_priem_activity);



        editTextTotal = findViewById(R.id.edit_text_total);

        textViewMaterial = findViewById(R.id.text_material);
        textViewParam = findViewById(R.id.text_param);
        textViewArticle = findViewById(R.id.text_article);
        textViewTotal = findViewById(R.id.text_total);
        btnAdd = findViewById(R.id.btn_add_new_total);
        btnHelp = findViewById(R.id.btn_help);
        selecProduct(messageId,messageDataBase);
        setColorToolBar(message);




        textViewMaterial.setText(messageMateria);
        textViewParam.setText(messageParam);
        textViewArticle.setText(messageArticle);
        textViewTotal.setText(messageTotal+" "+messageUnit);
        editTextTotal.setHint("Введите кол-во в "+ messageUnit);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTotal();
            }
        });
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat df = new DecimalFormat("#.#");
                newTotal = Double.parseDouble(editTextTotal.getText().toString());
                indiTotal = Double.parseDouble(selectProduct.getIndiTotal());
                if(selectProduct.getUnit().equals("шт")){
                     t = newTotal*indiTotal;
                }else {
                    t = newTotal/indiTotal;
                }

                String help = df.format(t);
                Toast.makeText(PriemVidachaActivity.this, help, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addNewTotal() {
        intent = new Intent(this,MainActivity.class);
        if(TextUtils.isEmpty(editTextTotal.getText().toString())){
            Toast.makeText(this, "Введите кол-во", Toast.LENGTH_SHORT).show();
            return;
        }
        newTotal = Double.parseDouble(editTextTotal.getText().toString());
        total = Double.parseDouble(selectProduct.getTotal());

        if(message.equals("priem")){
            if(selectProduct.getName().equals("Лист Хк") || selectProduct.getMaterial().equals("Цинк")){
                String id = db.push().getKey();
                db.child(id).setValue(new Product(id,selectProduct.getMaterial(),selectProduct.getArticle(),selectProduct.getName(),
                        selectProduct.getParam(),selectProduct.getLocation(),editTextTotal.getText().toString(),selectProduct.getUnit(),selectProduct.getIndiTotal())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PriemVidachaActivity.this, "Принято " + newTotal + messageUnit, Toast.LENGTH_SHORT).show();
                    }
                });
                bool = false;
                sendToEmailHk();
                finish();
            }else {
                newT = newTotal + total;
            }


        }else if(message.equals("vidacha")){
           newT = total - newTotal;
        }
        if(bool) {
            db.child(selectProduct.getId()).child("total").setValue(String.valueOf(newT)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    if (message.equals("priem")) {
                        Toast.makeText(PriemVidachaActivity.this, "Принято " + newTotal + messageUnit, Toast.LENGTH_SHORT).show();
                        sendToEmailHk();


                    } else if (message.equals("vidacha")) {
                        if(newT == 0 && selectProduct.getName().equals("Лист Хк")){
                            db.child(selectProduct.getId()).removeValue();
                        }
                        if(newT == 0 && selectProduct.getMaterial().equals("Цинк")){
                            db.child(selectProduct.getId()).removeValue();
                        }
                        Toast.makeText(PriemVidachaActivity.this, "Выдано " + newTotal + messageUnit, Toast.LENGTH_SHORT).show();

                    }
                    sendToEmail();
                    finish();

                }
            });
        }
    }

    private void sendToEmailHk() {
        addresses = new String[1];
        addresses[0] = "selectasclad@gmail.com";
        String doIt = "";
        if (message.equals("priem")) {
            doIt = "принято";
            subject = "прием";
        } else if (message.equals("vidacha")) {
            subject = "выдача";
            doIt = "выдано";
        }
        emailText = "" + messageMateria + " " + messageName + " " + messageParam +
                ". " + doIt + " " + newTotal + messageUnit ;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }

    }

    private void sendToEmail() {
        addresses = new String[1];
        addresses[0] = "selectasclad@gmail.com";
        String doIt = "";
        if (message.equals("priem")) {
            doIt = "принято";
            subject = "прием";
        } else if (message.equals("vidacha")) {
            subject = "выдача";
            doIt = "выдано";
        }
        emailText = "" + messageMateria + " " + messageName + " " + messageParam +
                ". " + doIt + " " + newTotal + messageUnit + ", было " + total + ", стало " + newT + messageUnit;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }

    }

    private void selecProduct(String id, String messageDataBase) {

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
                for(DataSnapshot ds:snapshot.getChildren()){
                    Product pr = ds.getValue(Product.class);
                    if(pr.getId().equals(id)){
                        selectProduct = pr;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return false;
    }
    private void setColorToolBar(String message) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        switch (message){
            case "priem":{
                getSupportActionBar().setTitle(R.string.priem);
                toolbar.setBackgroundColor(getResources().getColor(R.color.priem));
                btnAdd.setBackground(getResources().getDrawable(R.drawable.btn_priem));
                btnAdd.setText(R.string.priem);


                break;
            }
            case "vidacha":{
                getSupportActionBar().setTitle(R.string.vidacha);
                toolbar.setBackgroundColor(getResources().getColor(R.color.vidacha));
                btnAdd.setText(R.string.vidacha);
                btnAdd.setBackground(getResources().getDrawable(R.drawable.btn_vidacha));

                break;
            }
        }




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

}