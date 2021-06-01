package com.example.plasmafinder.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plasmafinder.Adapters.DonorAdapter;
import com.example.plasmafinder.Add.AddDonorActivity;
import com.example.plasmafinder.Add.AddRequestActivity;
import com.example.plasmafinder.Models.Donor;
import com.example.plasmafinder.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class MyDonorsFragment extends Fragment {
    private static final String TAG = "MyDonorsFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DonorAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_my_donors, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_my_donors);
        requireActivity().setTitle("My Donors");
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView(){
        Log.d(TAG, "setUpRecyclerView: ");

        Query query = db.collection("donors")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<Donor> options = new FirestoreRecyclerOptions.Builder<Donor>()
                .setQuery(query, Donor.class).build();

        adapter = new DonorAdapter(options, requireActivity());
        //recyclerView.setHasFixedSize(true); This causes problem. Do not use it.
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId() == R.id.add_donor){
            startActivity(new Intent(requireActivity(), AddDonorActivity.class));
            return true;
        }
        else if(item.getItemId() == R.id.add_request){
            startActivity(new Intent(requireActivity(), AddRequestActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
