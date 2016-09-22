package cecelia.lesson5hw;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeBackground();
        addMainPageFragment();
    }

    private void initializeBackground() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int background = sharedPref.getInt(getString(R.string.saved_background), R.color.white);
        this.setBackgroundColor(background);
    }

    public void setBackgroundColor(int colorId) {
        int color = ContextCompat.getColor(this, colorId);
        View view = this.getWindow().getDecorView(); //changes the background of the activity, so that it stays for all fragments
        view.setBackgroundColor(color);
        this.backgroundColor = colorId;
    }

    public void addMainPageFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Add the MainActivityFragment as the starting Fragment when the app is opened
        fragmentTransaction.replace(R.id.fragment_holder, new MainActivityFragment(), "CURRENT_FRAGMENT");
        fragmentTransaction.commit();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "butts");
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.saved_background), this.backgroundColor);
        editor.commit();
    }
}
