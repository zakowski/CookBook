package com.cookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cookapp.Common.Common;
import com.cookapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Registration extends AppCompatActivity {

    MaterialEditText editNumTel,editName,editPass;
    Button btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editName =findViewById(R.id.editName);
        editNumTel=findViewById(R.id.editNumTel);
        editPass=findViewById(R.id.editPass);

        btnReg = (Button)findViewById(R.id.btnReg);

        //Połączenie z bazą
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //czy jest internet
                if(Common.connectToInternet(getBaseContext())){

                final ProgressDialog Dialog = new ProgressDialog(Registration.this);
                Dialog.setMessage("Czekaj...");
                Dialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(editName.getText().toString().isEmpty() || editPass.getText().toString().isEmpty()){
                            Dialog.dismiss();
                            Toast.makeText(Registration.this,"Musisz podać imię i hasło !", Toast.LENGTH_SHORT).show();
                        }
                        else{
                        if(dataSnapshot.child(editNumTel.getText().toString()).exists()){
                            Dialog.dismiss();
                            Toast.makeText(Registration.this,"To konto już isntnieje ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Dialog.dismiss();
                            User user = new User(editName.getText().toString(),editPass.getText().toString());
                            table_user.child(editNumTel.getText().toString()).setValue(user);
                            Toast.makeText(Registration.this,"Rejestracja udana ", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
                else
                {
                    Toast.makeText(Registration.this,"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
