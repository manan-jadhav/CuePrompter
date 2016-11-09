package in.curos.cueprompter;

import android.content.ContentValues;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import in.curos.cueprompter.data.CuePrompterContract;
import in.curos.cueprompter.data.Script;
import in.curos.cueprompter.data.ScriptsProvider;

public class AddScriptActivity extends AppCompatActivity {

    private TextView scriptTitle, scriptContent;
    private TextInputLayout scriptTitleLayout, scriptContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_script);

        getSupportActionBar().setTitle(getString(R.string.add_script));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scriptTitle = (TextView) findViewById(R.id.script_title);
        scriptContent = (TextView) findViewById(R.id.script_content);

        scriptTitleLayout = (TextInputLayout) findViewById(R.id.script_title_container);
        scriptContentLayout = (TextInputLayout) findViewById(R.id.script_content_container);
    }

    private void save()
    {
        if (scriptTitle.getText().length() <= 5) {
            scriptTitleLayout.setError(getString(R.string.error_title_length));
            scriptTitleLayout.setErrorEnabled(true);
            return;
        }
        if (scriptContent.getText().length() <= 10) {
            scriptContentLayout.setError(getString(R.string.error_content_length));
            scriptContentLayout.setErrorEnabled(true);
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CuePrompterContract.ScriptEntry.TITLE, scriptTitle.getText().toString());
        contentValues.put(CuePrompterContract.ScriptEntry.CONTENT, scriptContent.getText().toString());
        getContentResolver().insert(ScriptsProvider.SCRIPTS_BASE_URI, contentValues);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_script_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
