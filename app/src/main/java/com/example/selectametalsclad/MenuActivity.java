package com.example.selectametalsclad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

public class MenuActivity extends AppCompatActivity {
    Intent intent;
    String message,messageDataBase,messageParam;
    Toolbar toolbar;
    Button btnAl,btnMet,btnZink,btnNerga;
    ListView listViewMaterialName;
    TextView textViewSelectProduct;
    DatabaseReference aluminBoxDataBase,aluminBoxName,aluminProfileDataBase,aluminProfileName,
            aluminSheetDataBase,aluminSheetName,aluminPipeDataBase,aluminPipeName,locationDataBase,zinkSheetName,zinkSheetDataBase,
    nergaSheetName,nergaSheetDataBase;
    DatabaseReference metalBoxDataBase,metalBoxName,metalSheetGkDataBase,metalSheetGkName,
            metalSheetHkDataBase,metalSheetHkName,metalPipeDataBase,metalPipeName;
    List<String> listMaterialName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initDataBase();
        btnAl = findViewById(R.id.btn_al_menu_activity);
        btnMet = findViewById(R.id.btn_met_menu_activity);
        btnZink = findViewById(R.id.btn_zink_menu_activity);
        btnNerga = findViewById(R.id.btn_nerga_menu_activity);
        listViewMaterialName = findViewById(R.id.list_material_name);
        textViewSelectProduct = findViewById(R.id.text_view_select_product);
        listMaterialName = new ArrayList<>();

        intent = getIntent();
        message = intent.getStringExtra("message");


        toolbar = findViewById(R.id.toolbar_menu_activity);
        setColorToolBar(message);

        btnAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                PopupMenu alPopupMenu = new PopupMenu(MenuActivity.this,btnAl);
                alPopupMenu.getMenuInflater().inflate(R.menu.popup_al_menu,alPopupMenu.getMenu());
                alPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.al_profile){
                            textViewSelectProduct.setText(R.string.alumin_profile);
                            messageDataBase = "aluminProfileDataBase";
                            initListViewMaterial(aluminProfileName);
                        }else if(menuItem.getItemId()==R.id.al_box){
                            textViewSelectProduct.setText(R.string.alumin_box);
                            messageDataBase = "aluminBoxDataBase";
                            initListViewMaterial(aluminBoxName);
                        }else if(menuItem.getItemId()==R.id.al_pipe){
                            textViewSelectProduct.setText(R.string.alumin_pipe);
                            messageDataBase = "aluminPipeDataBase";
                            initListViewMaterial(aluminPipeName);
                        }else if ((menuItem.getItemId()==R.id.al_sheet)){
                            textViewSelectProduct.setText(R.string.alumin_sheet);
                            messageDataBase = "aluminSheetDataBase";
                            initListViewMaterial(aluminSheetName);
                        }

                        return true;
                    }
                });
                alPopupMenu.show();

            }
        });
        btnMet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu metPopupMenu = new PopupMenu(MenuActivity.this,btnMet);
                metPopupMenu.getMenuInflater().inflate(R.menu.popup_met_menu,metPopupMenu.getMenu());
                metPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                         if(item.getItemId()==R.id.met_sheet_hk){
                            textViewSelectProduct.setText(R.string.hk_metal);
                            messageDataBase = "metalSheetHkDataBase";
                            initListViewMaterial(metalSheetHkName);
                        }else if (item.getItemId()==R.id.met_box){
                            textViewSelectProduct.setText(R.string.metal_box);
                            messageDataBase = "metalBoxDataBase";
                            initListViewMaterial(metalBoxName);
                        }else if (item.getItemId()==R.id.met_pipe){
                            textViewSelectProduct.setText(R.string.metal_pipe);
                            messageDataBase = "metalPipeDataBase";
                            initListViewMaterial(metalPipeName);
                        }else if (item.getItemId()==R.id.met_sheet_gk){
                             textViewSelectProduct.setText("Горячекатанная сталь");
                             messageDataBase = "metalSheetGkDataBase";
                             initListViewMaterial(metalSheetGkName);

                         }

                        return true;
                    }
                });
                metPopupMenu.show();
            }
        });
        btnZink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu zinkPopupMenu = new PopupMenu(MenuActivity.this,btnZink);
                zinkPopupMenu.getMenuInflater().inflate(R.menu.popup_zink_menu,zinkPopupMenu.getMenu());
                zinkPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.zink_sheet){
                            textViewSelectProduct.setText(R.string.zink);
                            messageDataBase = "zinkSheetDataBase";
                            initListViewMaterial(zinkSheetName);
                        }
                        return true;
                    }
                });
                zinkPopupMenu.show();
            }
        });
        btnNerga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu nergaPopupMenu = new PopupMenu(MenuActivity.this,btnNerga);
                nergaPopupMenu.getMenuInflater().inflate(R.menu.popup_nerga_menu,nergaPopupMenu.getMenu());
                nergaPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.nerga_sheet){
                            textViewSelectProduct.setText(R.string.nerga);
                            messageDataBase = "nergaSheetDataBase";
                            initListViewMaterial(nergaSheetName);
                        }
                        return true;
                    }
                });
                nergaPopupMenu.show();
            }
        });
        listViewMaterialName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageParam = listMaterialName.get(position).toString();
                goToNextActivity();
            }
        });


    }

    private void goToNextActivity() {
        intent = new Intent(MenuActivity.this, ListProductActivity.class);
        intent.putExtra("message",message);
        intent.putExtra("messageDataBase",messageDataBase);
        intent.putExtra("messageParam",messageParam);
        startActivity(intent);
    }

    private void initListViewMaterial(DatabaseReference db) {
        listMaterialName.clear();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    listMaterialName.add(ds.getValue(String.class));
                }
                initListViewMaterialName(listMaterialName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void initListViewMaterialName(List<String> list) {
        ArrayAdapter<String> adapterMaterialName = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        listViewMaterialName.setAdapter(adapterMaterialName);
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