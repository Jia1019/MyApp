package com.example.myapp.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapp.AppActivity;
import com.example.myapp.R;
import com.example.myapp.model.Background;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import com.example.myapp.ui.home.HomeViewModel;
import com.example.myapp.ui.store.StoreViewModel;
import com.example.myapp.ui.todoList.TodoListActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private ImageButton button;
    private LoginViewModel loginViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.loginBtn);
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        signIn();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
        {
            loginViewModel.getUsers().observe(this,users -> {
                int n=0;
                for (int i = 0 ; i<loginViewModel.getUsers().getValue().size();i++)
                {
                    if (loginViewModel.getUsers().getValue().get(i).getEmail().equals(currentUser.getEmail()))
                    {
                        n++;
                        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy ALREADY USER");
                        break;
                        //UserRepository.user.setEmail(account.getEmail());
                        //UserRepository.user.setUsername(account.getDisplayName());
                        //UserRepository.user.setCoinNum(loginViewModel.getUsers().getValue().get(i).getCoinNum());
                    }
                    else {
                        n=0;
                    }
                }
                if (n==0)
                {
                    User newUser = new User(currentUser.getEmail(), currentUser.getDisplayName(), 0);
                    loginViewModel.insert(newUser);
                    loginViewModel.insert(new Background(R.drawable.tangyuan, 0, true, true));
                    loginViewModel.insert(new Background(R.drawable.bear, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.burger, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.cat, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.chicken, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.dragon, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.hedgehog, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.hippo, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.koala, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.magiccat, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.rabbit, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.sandwich, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.squirrel, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.tuanzi, 120, false, false));
                    loginViewModel.insert(new Background(R.drawable.whale, 120, false, false));

                    loginViewModel.insertCourse("#BBFFFF");//	PaleTurquoise1
                    loginViewModel.insertCourse("#FFDAB9");//	PeachPuff
                    loginViewModel.insertCourse("#E6E6FA");//lavender
                    loginViewModel.insertCourse("#000000");//black
                    loginViewModel.insertCourse("#2F4F4F");//DarkSlateGray
                    loginViewModel.insertCourse("#708090");//	SlateGrey
                    loginViewModel.insertCourse("#7B68EE");//	MediumSlateBlue
                    loginViewModel.insertCourse("#00CED1");//	DarkTurquoise
                    loginViewModel.insertCourse("#C0FF3E");//	OliveDrab1
                    loginViewModel.insertCourse("#FFF68F");//	Khaki1
                    loginViewModel.insertCourse("#FFC1C1");//RosyBrown1
                    loginViewModel.insertCourse("#FF8247");//	Sienna1
                    loginViewModel.insertCourse("#FF6A6A");//	IndianRed1
                    loginViewModel.insertCourse("#EE82EE");//	Violet
                    loginViewModel.insertCourse("#EEE5DE");//	Seashell2
                    loginViewModel.insertCourse("#4876FF");//	RoyalBlue1
                    System.out.println("sssssssssssssssssssssssssssssssssssss INSERT BACKGROUND"+newUser.getUsername());

                }
            });

            Toast.makeText(LoginActivity.this,"HELLO "+currentUser.getDisplayName(),Toast.LENGTH_SHORT).show();
        }
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END signin]




    private void signOut()
    {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(FirebaseUser user) {

    }

    //public void openLoginView()
   //{
   //    // Choose authentication providers
   //    List<AuthUI.IdpConfig> providers = Arrays.asList(
   //            new AuthUI.IdpConfig.GoogleBuilder().build());

// //reate and launch sign-in intent
   //    startActivityForResult(
   //            AuthUI.getInstance()
   //                    .createSignInIntentBuilder()
   //                    .setAvailableProviders(providers)
   //                    .build(),
   //            RC_SIGN_IN);
   //}



}