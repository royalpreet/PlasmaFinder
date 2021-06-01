package com.example.plasmafinder.Home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plasmafinder.Models.User;
import com.example.plasmafinder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private User mUser;

    private EditText name, email, age, phone;
    private ProgressBar progressBar;
    private TextView editMode, necessaryText;
    private Button saveInfoButton, disableEditModeButton;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        requireActivity().setTitle("My Profile");

        name = view.findViewById(R.id.edit_name);
        email = view.findViewById(R.id.edit_email);
        age = view.findViewById(R.id.edit_age);
        phone = view.findViewById(R.id.edit_phone);
        progressBar = view.findViewById(R.id.progressBar);
        editMode = view.findViewById(R.id.edit_mode);
        saveInfoButton = view.findViewById(R.id.save_info_button);
        disableEditModeButton  = view.findViewById(R.id.disable_edit_mode_button);
        necessaryText = view.findViewById(R.id.necessary_text);

        progressBar.setVisibility(View.VISIBLE);
        disableEditMode();
        setUpFirebaseAuth();
        setInfo();

        disableEditModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditMode();
            }
        });

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

        return view;
    }

    private void disableEditMode(){
        name.setEnabled(false);
        email.setEnabled(false);
        age.setEnabled(false);
        phone.setEnabled(false);
        editMode.setVisibility(View.GONE);
        saveInfoButton.setVisibility(View.GONE);
        disableEditModeButton.setVisibility(View.GONE);
        necessaryText.setVisibility(View.GONE);
    }

    private void enableEditMode(){
        name.setEnabled(true);
        email.setEnabled(true);
        age.setEnabled(true);
        phone.setEnabled(true);
        editMode.setVisibility(View.VISIBLE);
        saveInfoButton.setVisibility(View.VISIBLE);
        disableEditModeButton.setVisibility(View.VISIBLE);
    }

    private void setInfo(){
        Log.d(TAG, "setInfo: ");

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess: user info fetched");
                        mUser = documentSnapshot.toObject(User.class);
                        name.setText(mUser.getName());
                        email.setText(mUser.getEmail());
                        age.setText(mUser.getAge());
                        phone.setText(mUser.getPhone());
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "onFailure: Could not fetch user info");
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void saveInfo(){
        Log.d(TAG, "saveInfo: ");

        closeKeyboard();
        necessaryText.setVisibility(View.GONE);

        String emailText = email.getText().toString().trim();
        String nameText = name.getText().toString().trim();
        String ageText = age.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();

        if(emailText.isEmpty() || nameText.isEmpty()){
            necessaryText.setVisibility(View.VISIBLE);
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            Toast.makeText(requireActivity(), "Please provide a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!ageText.isEmpty() && !isNumeric(ageText)){
            Toast.makeText(requireActivity(), "Please enter valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!phoneText.isEmpty() && !isNumeric(phoneText)){
            Toast.makeText(requireActivity(), "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ageText.isEmpty()) ageText = "";
        if(phoneText.isEmpty()) phoneText = "";

        disableEditMode();
        progressBar.setVisibility(View.VISIBLE);

        User user = new User(mAuth.getCurrentUser().getUid(), nameText, ageText, emailText, phoneText);
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Saved info");
                        Toast.makeText(requireActivity(), "Details saved", Toast.LENGTH_SHORT).show();
                        setInfoAfterEdit(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "onFailure: Error saving info");
                        Toast.makeText(requireActivity(), "Error: Could not save details", Toast.LENGTH_SHORT).show();
                        setInfoAfterEdit(mUser);

                    }
                });
    }

    void setInfoAfterEdit(User user){
        Log.d(TAG, "setInfoAfterEdit: ");

        disableEditMode();
        name.setText(user.getName());
        email.setText(user.getEmail());
        age.setText(user.getAge());
        phone.setText(user.getPhone());
        progressBar.setVisibility(View.GONE);

        mUser = user;
    }

    private boolean isNumeric(String string){
        try {
            long value = Long.parseLong(string);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId() == R.id.edit_profile_icon){
            enableEditMode();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpFirebaseAuth() {
        Log.d(TAG, "setUpFirebaseAuth: setting up firebase auth");
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String signedIn = "onAuthStateChanged: " + user.getUid() + " signed in";
                    Log.d(TAG, signedIn);
                    return;
                }
                Log.d(TAG, "onAuthStateChanged: signed out");
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mAuth.removeAuthStateListener(this.mAuthStateListener);
    }
}
