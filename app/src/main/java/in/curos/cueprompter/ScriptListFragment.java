package in.curos.cueprompter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import in.curos.cueprompter.data.Script;
import in.curos.cueprompter.data.ScriptsProvider;

/**
 * Created by curos on 8/11/16.
 */
public class ScriptListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView scriptsRecyclerView;
    TextView noScriptsView;
    ScriptListAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

        adapter = new ScriptListAdapter(getContext(), new ArrayList<Script>());
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(0, null, this);
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.script_list_fragment, container, false);

        scriptsRecyclerView = (RecyclerView) root.findViewById(R.id.script_list);
        noScriptsView = (TextView) root.findViewById(R.id.no_scripts);

        scriptsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        scriptsRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.add_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddScriptActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), ScriptsProvider.SCRIPTS_BASE_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            scriptsRecyclerView.setVisibility(View.GONE);
            noScriptsView.setVisibility(View.VISIBLE);
        } else {
            scriptsRecyclerView.setVisibility(View.VISIBLE);
            noScriptsView.setVisibility(View.GONE);

            data.moveToFirst();
            ArrayList<Script> scripts = new ArrayList<>();

            do {
                scripts.add(Script.populate(data));
            } while (data.moveToNext());
            data.close();

            adapter = new ScriptListAdapter(getContext(), scripts);

            scriptsRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter = new ScriptListAdapter(getContext(), new ArrayList<Script>());
        scriptsRecyclerView.setAdapter(adapter);
    }

    private class ScriptListAdapter extends RecyclerView.Adapter<ScriptListAdapter.VH> {

        private Context context;
        private ArrayList<Script> scripts = new ArrayList<>();
        private Calendar calendar = new GregorianCalendar();
        private SimpleDateFormat format = new SimpleDateFormat("d MMM");

        ScriptListAdapter(Context context, ArrayList<Script> scripts)
        {
            this.context = context;
            this.scripts = scripts;
        }

        @Override
        public ScriptListAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(context).inflate(R.layout.script_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            Script script = scripts.get(position);

            holder.title.setText(script.getTitle());
            holder.excerpt.setText(script.getContent());

            calendar.setTimeInMillis(script.getTimestamp() * 1000);
            holder.date.setText(format.format(calendar.getTime()));
        }

        @Override
        public int getItemCount() {
            return scripts.size();
        }

        class VH extends RecyclerView.ViewHolder {

            public TextView title;
            public TextView excerpt;
            public TextView date;

            public VH(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.script_title);
                excerpt = (TextView) itemView.findViewById(R.id.script_content_excerpt);
                date = (TextView) itemView.findViewById(R.id.script_date);
            }
        }
    }
}
