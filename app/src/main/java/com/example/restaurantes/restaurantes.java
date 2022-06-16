package com.example.restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class restaurantes extends AppCompatActivity {

    EditText txtNit, txtRazon, txtUbicacion, txtTematica, txtNumero;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantes);

        txtNit = (EditText) findViewById(R.id.txtEntrada);
        txtUbicacion = (EditText) findViewById(R.id.txtFuerte);
        txtRazon = (EditText) findViewById(R.id.txt_search);
        txtTematica = (EditText) findViewById(R.id.txtBebida);
        txtNumero = (EditText) findViewById(R.id.txtNumero);

    }

    public void save(View view) {

        String nit = txtNit.getText().toString();
        String razon = txtRazon.getText().toString();
        String ubicación = txtUbicacion.getText().toString();
        String tematica = txtTematica.getText().toString();
        String numero = txtNumero.getText().toString();

        if(txtNit.getText().toString().length() == 0){
            Toast.makeText(this, "Empty Nit sheet, is'nt allowed ", Toast.LENGTH_SHORT).show();
        }else{
            // Create a new user with a first and last name
            Map<String, Object> restaurantess = new HashMap<>();
            restaurantess.put("Nit", nit);
            restaurantess.put("Razón social", razon);
            restaurantess.put("Dirección", ubicación);
            restaurantess.put("Tematica", tematica);
            restaurantess.put("Numero", numero);


// Add a new document with a generated ID
            db.collection("restaurantes").document(razon)
                    .set(restaurantess)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Toast.makeText(restaurantes.this, "Restaurante Almacenado!!", Toast.LENGTH_SHORT).show();
                            Log.d("MESSAGE", "Dato Almacenado");
                            limpiar();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("MESSAGE", "Error adding document", e);
                        }
                    });
        }
    }

    public void delete(View view) {
        String razon = txtRazon.getText().toString();

        db.collection("restaurantes").document(razon).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("MESSAGE","Restaurante eliminado!!");
                Toast.makeText(restaurantes.this, "Successful Delete", Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MESSAGE", "Data Not Found");
            }
        });

    }

    private void limpiar() {
        txtNit.setText("");
        txtRazon.setText("");
        txtUbicacion.setText("");
        txtTematica.setText("");
        txtNumero.setText("");
    }

    public void update(View view) {
        String nit = txtNit.getText().toString();
        String razon = txtRazon.getText().toString();
        String ubicación = txtUbicacion.getText().toString();
        String tematica = txtTematica.getText().toString();
        String numero = txtNumero.getText().toString();

        if(txtNit.getText().toString().length() == 0){
            Toast.makeText(this, "Empty Nit sheet, is'nt allowed ", Toast.LENGTH_SHORT).show();
        }else{
            // Create a new user with a first and last name
            Map<String, Object> restaurantess = new HashMap<>();
            restaurantess.put("Nit", nit);
            restaurantess.put("Razón social", razon);
            restaurantess.put("Dirección", ubicación);
            restaurantess.put("Tematica", tematica);
            restaurantess.put("Numero", numero);


// Add a new document with a generated ID
            db.collection("restaurantes").document(razon)
                    .update(restaurantess)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Toast.makeText(restaurantes.this, "Restaurante Actualizado!!", Toast.LENGTH_SHORT).show();
                            Log.d("MESSAGE", "Dato Almacenado");
                            limpiar();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("MESSAGE", "Error adding document", e);
                        }
                    });
        }
    }

    public void consult(View view) {
        String razon = txtRazon.getText().toString();

        DocumentReference docRef = db.collection("restaurantes").document(razon);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        txtNit.setText(document.getData().get("Nit").toString());
                        txtUbicacion.setText(document.getData().get("Dirección").toString());
                        txtRazon.setText(document.getData().get("Razón social").toString());
                        txtTematica.setText(document.getData().get("Tematica").toString());
                        txtNumero.setText(document.getData().get("Numero").toString());

                    }else {
                        Log.d("TAG", "Restaurante no encontrado");
                        Toast.makeText(restaurantes.this, "No se encontró este restaurante", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d("TAG", "Error al consultar");
                }
            }
        });
    }
}