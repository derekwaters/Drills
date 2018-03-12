package com.frisbeeworld.drills;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.frisbeeworld.drills.database.Session;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.frisbeeworld.drills.R.id.fab;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "Anonymous";
    public static final int RC_SIGN_IN = 101;
    public static final int REQUEST_EDIT_SESSION = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mCurrentUser;

    private FirebaseDatabase firebaseDatabase;

    private FloatingActionButton floatingActionButton;

    private SessionAdapter sessionAdapter;
    private RecyclerView sessionList;
    private RecyclerView.LayoutManager sessionListLayoutManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT_SESSION) {
                // I don't think we need to do anything here, do we?
                sessionAdapter.refreshSessionList();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUser = ANONYMOUS;

        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    onSignedInInitialise(user.getDisplayName());
                    // We have a logged in user!
                    floatingActionButton.show();
                }
                else
                {
                    // No logged in user. :(
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                        RC_SIGN_IN
                    );

                    floatingActionButton.hide();
                }
            }
        };

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.floatingActionButton = (FloatingActionButton)findViewById(fab);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSessionIntent = new Intent(getApplicationContext(), AddSessionActivity.class);
                startActivity(addSessionIntent);
            }
        });

        List<Session> sessionsList = new ArrayList<>();
        sessionAdapter = new SessionAdapter(this);
        sessionList = (RecyclerView) findViewById(R.id.session_list);

        sessionListLayoutManager = new LinearLayoutManager(this);
        sessionList.setLayoutManager(sessionListLayoutManager);

        sessionList.setAdapter(sessionAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_sign_out)
        {
            AuthUI.getInstance().signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        detachDatabaseReadListener();
        // sessionAdapter.clear();
    }

    private void onSignedInInitialise(String username) {
        mCurrentUser = username;
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener()
    {
        DrillsDatastore.getDatastore().setupDatabaseListeners(sessionAdapter);
    }

    private void onSignedOutCleanup()
    {
        mCurrentUser = ANONYMOUS;
        // sessionAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener()
    {
        DrillsDatastore.getDatastore().detachDatabaseListeners();
    }

    public void editSession (Session editSession)
    {
        Intent editSessionIntent = new Intent(getApplicationContext(), AddSessionActivity.class);
        editSessionIntent.putExtra(AddSessionActivity.SESSION_ID, editSession.getId());
        startActivityForResult(editSessionIntent, REQUEST_EDIT_SESSION);
    }

    public void removeSession (int position)
    {
        DrillsDatastore.getDatastore().removeSession(position);
        sessionAdapter.refreshSessionList();
    }
}
