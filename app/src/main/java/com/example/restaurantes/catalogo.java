package com.example.restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class catalogo extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText txtEntrada, txtFuerte, txtBebida;
    Spinner listspin;


    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<>();

    List<QueryDocumentSnapshot> listDocumentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        listspin = findViewById(R.id.spinner);

        db = FirebaseFirestore.getInstance();
        listspin = findViewById(R.id.spinner);
        txtFuerte = (EditText) findViewById(R.id.txtFuerte);
        txtEntrada = (EditText) findViewById(R.id.txtEntrada);
        txtBebida = (EditText) findViewById(R.id.txtBebida);

        cargarDatos();
    }

    private void cargarDatos() {

        db.collection("restaurantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listDocumentos.add(document);
                                arrayList.add(document.getId());
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            arrayAdapter = new ArrayAdapter<>(catalogo.this, android.R.layout.simple_dropdown_item_1line, arrayList);
                            listspin.setAdapter(arrayAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void guardar(View view) {

        String id = listspin.getSelectedItem().toString();
        String platoFuerte = txtFuerte.getText().toString();
        String entrada = txtEntrada.getText().toString();
        String bebida = txtBebida.getText().toString();

        // Create a new user with a first and last name
        Map<String, Object> restaurantes = new HashMap<>();
        restaurantes.put("Entrada", entrada);
        restaurantes.put("Fuerte", platoFuerte);
        restaurantes.put("Bebida", bebida);

// Add a new document with a generated ID
        db.collection("platos").document(id)
                .set(restaurantes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Mensaje", "Restaurante almacenado exitosamente");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Mensaje", "Error");
                    }
                });
    }

    public void eliminar(View view) {
        String id = listspin.getSelectedItem().toString();

        db.collection("platos").document(id)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("MESSAGE","Restaurante eliminado!!");
                        Toast.makeText(catalogo.this, "Successful Delete", Toast.LENGTH_SHORT).show();
                        //limpiar();
                    }
                });
    }

    public void actualizar(View view) {
        String id = listspin.getSelectedItem().toString();
        String platoFuerte = txtFuerte.getText().toString();
        String entrada = txtEntrada.getText().toString();
        String bebida = txtBebida.getText().toString();

        // Create a new user with a first and last name
        Map<String, Object> restaurantes = new HashMap<>();
        restaurantes.put("Entrada", entrada);
        restaurantes.put("Fuerte", platoFuerte);
        restaurantes.put("Bebida", bebida);

// Add a new document with a generated ID
        db.collection("platos").document(id)
                .update(restaurantes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Mensaje", "Restaurante actualizado exitosamente");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Mensaje", "Error");
                    }
                });
    }

    public void consultar(View view) {
        String id = listspin.getSelectedItem().toString();

        DocumentReference docRef = db.collection("platos").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        txtEntrada.setText(document.getData().get("Entrada").toString());
                        txtFuerte.setText(document.getData().get("Fuerte").toString());
                        txtBebida.setText(document.getData().get("Bebida").toString());
                    }else {
                        Log.d("TAG", "Restaurante no encontrado");
                        Toast.makeText(catalogo.this, "No se encontró este restaurante", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d("TAG", "Error al consultar");
                }
            }
        });
    }
}