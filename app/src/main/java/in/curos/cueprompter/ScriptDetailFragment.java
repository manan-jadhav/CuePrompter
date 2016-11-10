package in.curos.cueprompter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.curos.cueprompter.data.Script;
import in.curos.cueprompter.data.ScriptsProvider;

/**
 * Created by curos on 9/11/16.
 */
public class ScriptDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String scriptId;
    Script script;

    MainActivity activity;
    ActionBar actionBar;

    TextView timestamp, contents;

    DateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy  hh:mm a");
    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.script_details, container, false);

        timestamp = (TextView) view.findViewById(R.id.timestamp);
        contents = (TextView) view.findViewById(R.id.script_content);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scriptId = getArguments().getString("id");
        activity = ((MainActivity) getActivity());
        actionBar = activity.getSupportActionBar();
        if (! activity.isDualScreen()) {
            actionBar.setTitle(getString(R.string.script_details));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true);

        getActivity().getSupportLoaderManager().initLoader((int) Long.parseLong(scriptId), null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (! activity.isDualScreen())
            menu.setGroupVisible(R.id.add_dummy_content_group, false);
        inflater.inflate(R.menu.script_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity) getActivity()).showMainScreen();
                break;
            case R.id.edit:
                Intent intent = new Intent(getContext(), EditScriptActivity.class);
                intent.putExtra("id", scriptId);
                startActivity(intent);
                break;
            case R.id.play:
                Intent intent2 = new Intent(getContext(), TeleprompterActivity.class);
                intent2.putExtra("id", scriptId);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), ScriptsProvider.SCRIPT_BASE_URI.buildUpon().appendPath(scriptId).build(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        if (data.getCount() != 0) {
            script = Script.populate(data);
            if (! activity.isDualScreen()) {
                actionBar.setTitle(script.getTitle());
            }

            calendar.setTimeInMillis(script.getTimestamp()*1000);
            timestamp.setText(dateFormat.format(calendar.getTime()));
            contents.setText(script.getContent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
