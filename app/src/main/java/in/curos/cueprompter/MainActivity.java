package in.curos.cueprompter;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private int LEFT_FRAGMENT_CONTAINER = R.id.left_fragment_container;
    private int RIGHT_FRAGMENT_CONTAINER = R.id.right_fragment_container;
    private int MAIN_FRAGMENT_CONTAINER;

    private boolean DUAL_SCREEN_MODE;

    private boolean HOME = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DUAL_SCREEN_MODE = findViewById(RIGHT_FRAGMENT_CONTAINER) != null;

        if (DUAL_SCREEN_MODE) {
            MAIN_FRAGMENT_CONTAINER = RIGHT_FRAGMENT_CONTAINER;
        } else {
            MAIN_FRAGMENT_CONTAINER = LEFT_FRAGMENT_CONTAINER;
        }

        showMainScreen();
    }

    public void showMainScreen()
    {
        HOME = true;
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(LEFT_FRAGMENT_CONTAINER, new ScriptListFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    public void showDetailScreen(String id)
    {
        HOME = false;
        ScriptDetailFragment fragment = new ScriptDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString("id", id);
        fragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(MAIN_FRAGMENT_CONTAINER, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!HOME)
            showMainScreen();
        else
            finish();
    }
}
