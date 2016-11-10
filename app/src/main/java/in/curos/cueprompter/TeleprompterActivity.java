package in.curos.cueprompter;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import in.curos.cueprompter.data.Script;
import in.curos.cueprompter.data.ScriptsProvider;

public class TeleprompterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Drawable background;

    public static final String BLACK = "black";
    public static final String CYAN = "cyan";
    public static final String RED = "red";
    public static final String TEAL = "teal";
    public static final String INDIGO = "indigo";
    public static final String PURPLE = "purple";

    public static final String COLOR_SCHEME = "color_scheme";
    public static final String PLAY_SPEED = "play_speed";
    public static final String TEXT_SIZE = "text_size";

    private SharedPreferences sharedPreferences;

    private int backgroundColor;
    private int textSize;
    private boolean isPlaying = false;
    private float playSpeed;

    private BackgroundChooserDialog dialog;

    private String scriptId;

    private ScrollView contentContainer;
    private TextView content;
    private View play, pause;

    private TimerTask timerTask;
    private Timer timer = new Timer();

    private DecimalFormat speedFormat = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teleprompter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        backgroundColor = getSchemeColor(sharedPreferences.getString(COLOR_SCHEME, BLACK));
        textSize = sharedPreferences.getInt(TEXT_SIZE, 46);
        playSpeed = sharedPreferences.getFloat(PLAY_SPEED, 1);

        scriptId = getIntent().getExtras().getString("id");

        content = (TextView) findViewById(R.id.content);
        content.setMovementMethod((new ScrollingMovementMethod()));
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        setupDisplay();

        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void showColorSchemeChooser(View v)
    {
        dialog = new BackgroundChooserDialog();
        dialog.show(getSupportFragmentManager(), "color_scheme_chooser");
    }

    public void scrollText()
    {
        content.scrollBy(0, (int) (2 * playSpeed));
    }

    public void setupDisplay()
    {
        background = new ColorDrawable(getResources().getColor(backgroundColor));
        getWindow().setBackgroundDrawable(background);

        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public int getSchemeColor(String schemeName)
    {
        switch (schemeName) {
            case BLACK:
                return R.color.schemeBlackBg;
            case RED:
                return R.color.schemeRedBg;
            case INDIGO:
                return R.color.schemeIndigoBg;
            case PURPLE:
                return R.color.schemePurpleBg;
            case TEAL:
                return R.color.schemeTealBg;
            case CYAN:
                return R.color.schemeCyanBg;
            default: throw new IllegalArgumentException("Invalid scheme name");
        }
    }

    public void propertyClicked(View v)
    {
        String property = v.getTag().toString();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        String toast = "";

        switch (property) {
            case "text_size_increase":
                textSize += 4;
                editor.putInt(TEXT_SIZE, textSize);
                toast = "Text Size : "+textSize;
                break;
            case "text_size_decrease":
                textSize -= 4;
                editor.putInt(TEXT_SIZE, textSize);
                toast = "Text Size : "+textSize;
                break;
            case "speed_increase":
                playSpeed += 0.5;
                editor.putFloat(PLAY_SPEED, playSpeed);
                toast = "Play Speed : "+ speedFormat.format(playSpeed) +"x";
                break;
            case "speed_decrease":
                playSpeed -= 0.5;
                editor.putFloat(PLAY_SPEED, playSpeed);
                toast = "Play Speed : "+ speedFormat.format(playSpeed) +"x";
                break;
            case "play":
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                isPlaying = true;
                toast = "Playing";
                play();
                break;
            case "pause":
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                isPlaying = false;
                toast = "Pause";
                pause();
                break;
        }

        editor.commit();
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        setupDisplay();
    }

    public void play()
    {
        Log.i("CUEPROMPTER", "played");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                scrollText();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 24);
    }

    public void pause()
    {
        Log.i("CUEPROMPTER", "paused");
        timerTask.cancel();
    }

    public void setScheme(View v)
    {
        String scheme = v.getTag().toString();
        backgroundColor = getSchemeColor(scheme);

        PreferenceManager.getDefaultSharedPreferences(this).edit()
        .putString("color_scheme", scheme)
        .apply();

        setupDisplay();
        dialog.dismiss();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScriptsProvider.SCRIPT_BASE_URI.buildUpon().appendPath(scriptId).build(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        Script script = Script.populate(data);
        content.setText(script.getContent());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public static class BackgroundChooserDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.color_scheme_chooser_dialog, null);

            builder.setTitle(getString(R.string.choose_color));
            builder.setView(view);

            return builder.create();
        }
    }
}
