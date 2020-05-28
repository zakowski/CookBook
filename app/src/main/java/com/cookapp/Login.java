package com.cookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cookapp.Common.Common;
import com.cookapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {
    //deklaracja obiektu
    EditText editNumTel,editPass;
    Button btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editPass =  findViewById(R.id.editPass);
        editNumTel= findViewById(R.id.editNumTel);
        btnLog= findViewById(R.id.btnLog);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                //czy polaczony z internetem
                if(Common.connectToInternet(getBaseContext())){

                final ProgressDialog Dialog = new ProgressDialog(Login.this);
                Dialog.setMessage("Czekaj...");
                Dialog.show();

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Sprawdz usera czy istnieje w bazie
                        if(dataSnapshot.child(editNumTel.getText().toString()).exists()) {
                            //Wez dane Usera
                            Dialog.dismiss();
                            User user = dataSnapshot.child(editNumTel.getText().toString()).getValue(User.class);
                            user.setNrTel(editNumTel.getText().toString());
                            if (user.getPassword().equals(editPass.getText().toString()))
                            {
                                //Toast.makeText(Login.this, "Zalogowano pomyslnie !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this,Home.class);
                                Common.currentUser = user;
                                startActivity(intent);
                                finish();
                                //finish(finishActivity(MainActivity.class));
                            }
                            else {
                                Toast.makeText(Login.this, "Logowanie nieudane, złe hasło !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Dialog.dismiss();
                            Toast.makeText(Login.this, "Uzytkownik nie istnieje !", Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                }

                else   // check internet
                {
                    Toast.makeText(Login.this,"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//not working
//    @Override
//    protected void onStart() {
//        super.onStart();
//        //adapter.startListening();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
//    }
}
