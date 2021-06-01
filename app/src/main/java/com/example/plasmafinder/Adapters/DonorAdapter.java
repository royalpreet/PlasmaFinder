package com.example.plasmafinder.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plasmafinder.Dialogs.DeleteDialog;
import com.example.plasmafinder.Models.Donor;
import com.example.plasmafinder.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class DonorAdapter extends FirestoreRecyclerAdapter<Donor, DonorAdapter.DonorHolder> {

    private Context mContext;

    public DonorAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Donor> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull DonorHolder holder, int position, @NonNull @NotNull Donor model) {
        holder.nameOfDonor.setText(model.getName_of_donor());
        holder.age.setText(model.getAge());
        holder.bloodGroup.setText(model.getBlood_group());
        holder.phone.setText(model.getPhone());


        if(model.getTimestamp() != null) {
            String str = model.getTimestamp().toString();
            String[] arr = str.split(" ");
            String postedText = "";
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getUser_id())){
                postedText = "Posted by you on " + arr[1] + " " + arr[2] + ", " + arr[5] + " at " + arr[3];
            }
            else{
                postedText = "Posted on " + arr[1] + " " + arr[2] + ", " + arr[5] + " at " + arr[3];
            }
            holder.date.setText(postedText);
        }

        if(model.getEmail() == null || model.getEmail().isEmpty()) {
            holder.relativeLayoutEmail.setVisibility(View.GONE);
        }
        else{
            holder.relativeLayoutEmail.setVisibility(View.VISIBLE);
            holder.email.setText(model.getEmail());
        }

        if(model.getDescription() == null || model.getDescription().isEmpty()) {
            holder.relativeLayoutDescription.setVisibility(View.GONE);
        }
        else{
            holder.relativeLayoutDescription.setVisibility(View.VISIBLE);
            holder.description.setText(model.getDescription());
        }

        if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getUser_id())){
            holder.deleteButton.setVisibility(View.GONE);
        }
        else{
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDialog deleteDialog = new DeleteDialog();

                    Bundle args = new Bundle();

                    args.putInt("code", 1);
                    args.putString("id", model.getDonor_id());
                    deleteDialog.setArguments(args);

                    deleteDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(), "Delete Dialog");
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public DonorHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_layout, parent, false);
        return new DonorHolder(view);
    }

    class DonorHolder extends RecyclerView.ViewHolder{
        TextView nameOfDonor;
        TextView age;
        TextView bloodGroup;
        TextView phone;
        TextView email;
        TextView description;
        TextView date;
        RelativeLayout relativeLayoutEmail;
        RelativeLayout relativeLayoutDescription;
        Button deleteButton;

        public DonorHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameOfDonor = itemView.findViewById(R.id.name_of_donor);
            age = itemView.findViewById(R.id.age);
            bloodGroup = itemView.findViewById(R.id.blood_group);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            description = itemView.findViewById(R.id.description);
            relativeLayoutEmail = itemView.findViewById(R.id.relLayoutEmail);
            relativeLayoutDescription = itemView.findViewById(R.id.relLayoutDescription);
            date = itemView.findViewById(R.id.date);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
