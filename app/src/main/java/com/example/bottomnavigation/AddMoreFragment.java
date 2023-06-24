package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddMoreFragment extends Fragment implements Histroy_SelectListener  {

    TextView NoMedReminder;
    CardView Container1, Container2;

    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    History_Adapter myAdapter;
    ArrayList<History_Getter> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_more, container, false);

        Container1 = view.findViewById(R.id.container1_cv);
        Container2 = view.findViewById(R.id.container2_cv);

        Container1.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ContainerScheduleList.class);
            startActivity(intent);
        });

        Container2.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ContainerScheduleList.class);
            startActivity(intent);
        });

        recyclerView = view.findViewById(R.id.history_recyclerview);
        firestore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Pass the fragment's context

        list = new ArrayList<>();
        myAdapter = new History_Adapter(getContext(), list, this); // Pass the fragment's context
        recyclerView.setAdapter(myAdapter);

        firestore.collection("History").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            History_Getter data = document.toObject(History_Getter.class);
                            list.add(data);

                            NoMedReminder = view.findViewById(R.id.no_medication_reminder);
                            NoMedReminder.setVisibility(View.INVISIBLE);
                        }
                        myAdapter.notifyDataSetChanged();
                    } else {
                        NoMedReminder = view.findViewById(R.id.no_medication_reminder);
                        NoMedReminder.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle error
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClicked(History_Getter history) {

        Toast.makeText(getActivity(), history.getActivity(), Toast.LENGTH_SHORT).show();

    }
}
