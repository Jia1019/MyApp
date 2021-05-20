package com.example.myapp.ui.store;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.model.Background;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends Fragment implements BackgroundItemAdapter.OnListItemClickListener {

    private StoreViewModel storeViewModel;
    RecyclerView recyclerView;
    BackgroundItemAdapter adapter;
    private ArrayList<Background> backgroundList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        storeViewModel =
                new ViewModelProvider(this).get(StoreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerView = root.findViewById(R.id.backgroundRv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.hasFixedSize();
        backgroundList = new ArrayList<>();

        adapter = new BackgroundItemAdapter(backgroundList,this);
        recyclerView.setAdapter(adapter);

        storeViewModel.getAllBackground().observe(getViewLifecycleOwner(),backgrounds -> {
            backgroundList.clear();
            backgroundList.addAll(backgrounds);
            adapter.notifyDataSetChanged();
        });


        return root;
    }

    @Override
    public void update(int position)
    {
       Background background = backgroundList.get(position);
       if (background.isOwned())
       {
           storeViewModel.applyBackground(background);
           Toast.makeText(getActivity(), "APPLY SUCCESSFULLY!!!", Toast.LENGTH_SHORT).show();
           adapter.notifyDataSetChanged();
       }
       else
       {
           if (storeViewModel.getCurrentUser().getValue().getCoinNum()>=background.getCostNum())
           {
               storeViewModel.buyBackground(background);
           }
           else {
               Toast.makeText(getActivity(),"You don't have enough coins", Toast.LENGTH_SHORT).show();
           }
           adapter.notifyDataSetChanged();
       }

    }

    @Override
    public void cancelApply(int position)
    {
        Background background = backgroundList.get(position);
        storeViewModel.cancelBackground(background);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //applyRotation(getView(),0,180,true);

    }




}