package com.cookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cookapp.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Notepad extends AppCompatActivity {

    private Button btnCreate;
    private EditText editTitle, editContent;

    FirebaseDatabase database;
    private DatabaseReference NotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        database = FirebaseDatabase.getInstance();
        NotesDatabase = database.getReference().child("Notepad");

        btnCreate =  findViewById(R.id.btnSendNote);
        editTitle =  findViewById(R.id.NoteTitle);
        editContent =  findViewById(R.id.NoteContent);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                if (Common.connectToInternet(getBaseContext())) {
                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                        sendNote(title, content);
                    } else {
                        Snackbar.make(view, "Pozostały puste pola", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Sprawdź połączenie z internetem !",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNote(String title, String content) {
                // Stworz notatke
                final DatabaseReference newNoteRef = NotesDatabase.push();

                final Map noteMap = new HashMap();
                noteMap.put("title", title);
                noteMap.put("content", content);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Notepad.this, "Przepis został wysłany", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Notepad.this, "Błąd, nie udało się wysłać", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                thread.start();
    }
}
