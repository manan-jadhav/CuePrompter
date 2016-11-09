package in.curos.cueprompter;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private int LEFT_FRAGMENT_CONTAINER = R.id.left_fragment_container;
    private int RIGHT_FRAGMENT_CONTAINER = R.id.right_fragment_container;
    private int MAIN_FRAGMENT_CONTAINER;

    private boolean DUAL_SCREEN_MODE;

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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(LEFT_FRAGMENT_CONTAINER, new ScriptListFragment())
                .commit();
    }
}
