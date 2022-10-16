package sanjay.mhtcet.finalproject2;
import static sanjay.mhtcet.finalproject2.R.*;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private static final int RC_APP_UPDATE=100;
    private AppUpdateManager mAppUpdateManager;
    private FrameLayout main_frame;

    private final NavigationBarView.OnItemSelectedListener  onNavigationItemSelectedListener=
            item -> {

                switch (item.getItemId()){
                    case id.navigation_home:
                        setFragment(new CategoryFragment());
                        return true;

                    case id.navigation_leaderboard:
                        setFragment(new LeaderboardFragment());
                        return true;

                    case id.navigation_profile:
                        setFragment(new ProfileFragment());
                        return true;
                    default:

                }

                return false;
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        mAppUpdateManager=AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.FLEXIBLE,MainActivity.this,RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



        DbQuery.g_firestore= FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("MHT-CET");
main_frame=findViewById(id.main_frame);
        BottomNavigationView bottom_NavigationView = findViewById(id.bottom_nav_bar);
    bottom_NavigationView.setOnItemSelectedListener(onNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView drawerProfileName = navigationView.getHeaderView(0).findViewById(id.nav_drawer_name);
        TextView drawerProfileText = navigationView.getHeaderView(0).findViewById(id.nav_drawer_text_img);

        String name= DbQuery.myProfile.getName();
        drawerProfileName.setText(name);

        drawerProfileText.setText(name.toUpperCase().substring(0,1));

        setFragment(new CategoryFragment());
    }
    @Override
    public void onBackPressed(){
        DrawerLayout drawer =(DrawerLayout) findViewById(id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

@Override
public boolean onNavigationItemSelected(MenuItem item){
        int id=item.getItemId();

        if (id== R.id.nav_home){
            setFragment(new CategoryFragment());
        }
        else if (id==R.id.nav_leaderboard){
            setFragment(new LeaderboardFragment());
        }else if (id==R.id.nav_profile){
            setFragment(new ProfileFragment());
        }else if (id==R.id.nav_bookmark){
            Intent intent=new Intent(MainActivity.this,BookmarkActivity.class);
            startActivity(intent);
        }



        DrawerLayout drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


return true;
}
private InstallStateUpdatedListener installStateUpdatedListener=new InstallStateUpdatedListener() {
    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus()==InstallStatus.DOWNLOADED){
            showCompletedUpdate();
        }
    }
};

    @Override
    protected void onStop() {
        if (mAppUpdateManager!=null) mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    private void showCompletedUpdate() {
        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"New app is ready",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    private void setFragment(Fragment fragment){
    FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
    transaction.replace(main_frame.getId(),fragment);
    transaction.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        if (requestCode==RC_APP_UPDATE && resultCode!= RESULT_OK ){
            Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode,resultCode,data);

    }


}
