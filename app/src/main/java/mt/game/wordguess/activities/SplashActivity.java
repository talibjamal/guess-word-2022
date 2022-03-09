package mt.game.wordguess.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import mt.game.wordguess.R;
import mt.game.wordguess.models.DicitionaryModel;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initRealmDb();
        init();
        callHandler();

    }

    /**
     * initilizing
     * views
     */
    public void init(){
        mHandler = new Handler(getMainLooper());
    }

    /**
     * setting
     * handler
     * for next screen after
     * short time in sec
     */
    public void callHandler(){
        mHandler.postDelayed(() -> callMainScreen(), 3000);
    }

    /**
     * calling main
     * screen
     */
    public void callMainScreen(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    /**
     * initilizing
     * realm db for
     * storing user data
     */
    public void initRealmDb() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(getString(R.string.app_name) + ".realm")
                .schemaVersion(1) // Must be bumped when the schema changes
                .modules(new DicitionaryModel())
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

}