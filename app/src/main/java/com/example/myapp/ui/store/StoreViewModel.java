package com.example.myapp.ui.store;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapp.R;
import com.example.myapp.model.Background;
import com.example.myapp.model.User;
import com.example.myapp.repository.BackgroundRepository;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.ui.login.LoginViewModel;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {
    private BackgroundRepository backgroundRepository;
    private UserRepository userRepository;

    public StoreViewModel(Application application) {
        super(application);
        backgroundRepository = BackgroundRepository.getInstance(application);
        userRepository = UserRepository.getInstance(application);
    }

    public LiveData<List<Background>> getAllBackground() {
        return backgroundRepository.getAllBackground();
    }

    public void insert(Background background)
    {
        backgroundRepository.insert(background);
    }

    public LiveData<User> getCurrentUser()
    {
        return userRepository.getCurrentUser();
    }



    public void buyBackground(Background background)
    {
            background.setOwned(true);
            backgroundRepository.update(background);
            getCurrentUser().getValue().setCoinNum(getCurrentUser().getValue().getCoinNum()-background.getCostNum());
            userRepository.update(getCurrentUser().getValue());
    }

    public void applyBackground(Background background)
    {

                Background background1 = backgroundRepository.getCurrentBackground().getValue();
                if (background1!=null)
                {
                    background1.setApplied(false);
                    backgroundRepository.update(background1);
                }



            background.setApplied(true);
            backgroundRepository.update(background);
    }

    public void cancelBackground(Background background)
    {
        if (background.isApplied())
        {
            background.setApplied(false);
            backgroundRepository.update(background);
                for (int i = 0; i<getAllBackground().getValue().size(); i++)
                {
                    if (getAllBackground().getValue().get(i).getCostNum()==0)
                    {
                        Background background1 = getAllBackground().getValue().get(i);
                        background1.setApplied(true);
                        backgroundRepository.update(background1);
                    }
                }

        }
    }


}