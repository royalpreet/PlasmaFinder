package com.example.plasmafinder.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.plasmafinder.Models.Request;
import com.example.plasmafinder.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class RequestAdapter extends FirestoreRecyclerAdapter<Request, RequestAdapter.RequestHolder> {

    private static final String TAG = "RequestAdapter";

    private Context mContext;
    private TextView notFound;

    public RequestAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Request> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull RequestHolder holder, int position, @NonNull @NotNull Request model) {
        holder.nameOfPatient.setText(model.getName_of_patient());
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
                    args.putInt("code", 0);
                    args.putString("id", model.getRequest_id());
                    deleteDialog.setArguments(args);

                    deleteDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(), "Delete Dialog");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @NotNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout, parent, false);
        return new RequestHolder(view);
    }

    class RequestHolder extends RecyclerView.ViewHolder{
        TextView nameOfPatient;
        TextView age;
        TextView bloodGroup;
        TextView phone;
        TextView email;
        TextView description;
        TextView date;
        RelativeLayout relativeLayoutEmail;
        RelativeLayout relativeLayoutDescription;
        Button deleteButton;

        public RequestHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameOfPatient = itemView.findViewById(R.id.name_of_patient);
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
