package com.example.plasmafinder.Add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plasmafinder.Home.MainActivity;
import com.example.plasmafinder.Models.Donor;
import com.example.plasmafinder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AddDonorActivity extends AppCompatActivity {

    private static final String TAG = "AddDonorActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Spinner bloodGroupSpinner, rhdSpinner;
    private EditText nameOfDonor, age, phone, email, description;
    private TextView necessaryText;
    private Button addDonorButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Donor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bloodGroupSpinner = findViewById(R.id.blood_group_spinner);
        rhdSpinner = findViewById(R.id.rhd_spinner);
        nameOfDonor = findViewById(R.id.donor_name);
        age = findViewById(R.id.age);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        description = findViewById(R.id.description);
        necessaryText = findViewById(R.id.necessary_text);
        addDonorButton = findViewById(R.id.add_donor);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        necessaryText.setVisibility(View.GONE);

        setSpinners();

        addDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDonor();
            }
        });
    }

    private void addDonor(){
        Log.d(TAG, "addDonor: ");

        necessaryText.setVisibility(View.GONE);

        String nameOfDonorText = nameOfDonor.getText().toString().trim();
        String ageText = age.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String descriptionText = description.getText().toString().trim();

        String sign = "";
        if(rhdSpinner.getSelectedItem().toString().equals("Positive")) sign = "+";
        else sign = "-";

        String bloodGroupText = bloodGroupSpinner.getSelectedItem().toString() + sign;

        if(nameOfDonorText.isEmpty() || ageText.isEmpty() || phoneText.isEmpty()){
            necessaryText.setVisibility(View.VISIBLE);
            return;
        }

        if(!isNumeric(ageText)){
            Toast.makeText(this, "Please enter valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isNumeric(phoneText)){
            Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!emailText.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(emailText.isEmpty()) emailText = "";
        if(descriptionText.isEmpty()) descriptionText = "";

        progressBar.setVisibility(View.VISIBLE);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Donor donor = new Donor(nameOfDonorText, ageText, bloodGroupText, phoneText,
                emailText, descriptionText, new Date(), "", userId);

        db.collection("donors").add(donor)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Donor uploaded");
                        Toast.makeText(AddDonorActivity.this, "Donor uploaded successfully", Toast.LENGTH_SHORT).show();

                        documentReference.update("donor_id", documentReference.getId());

                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(AddDonorActivity.this, MainActivity.class);
                        intent.putExtra("donor", 1);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "onFailure: Couldn't upload donor:\n" + e.getMessage());
                        Toast.makeText(AddDonorActivity.this, "Failed to upload ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private boolean isNumeric(String string){
        try {
            long value = Long.parseLong(string);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setSpinners(){
        Log.d(TAG, "setSpinners: ");

        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_groups, android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);

        ArrayAdapter<CharSequence> rhdAdapter = ArrayAdapter.createFromResource(this,
                R.array.rhds, android.R.layout.simple_spinner_item);
        rhdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rhdSpinner.setAdapter(rhdAdapter);
    }
}