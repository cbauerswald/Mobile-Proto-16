package cecelia.lesson3hw;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Add the MainActivityFragment as the starting Fragment when the app is opened
        fragmentTransaction.replace(R.id.fragment_holder, new MainActivityFragment(), "CURRENT_FRAGMENT");

        fragmentTransaction.commit();
    }

}
