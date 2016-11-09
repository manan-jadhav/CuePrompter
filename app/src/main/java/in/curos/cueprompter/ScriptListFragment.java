package in.curos.cueprompter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ScriptItemTouchCallback());
        itemTouchHelper.attachToRecyclerView(scriptsRecyclerView);

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

    public void setRecyclerViewVisibility(boolean show)
    {
        if (show) {
            scriptsRecyclerView.setVisibility(View.VISIBLE);
            noScriptsView.setVisibility(View.GONE);
        } else {
            scriptsRecyclerView.setVisibility(View.GONE);
            noScriptsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
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

            setRecyclerViewVisibility(scripts.size() > 0);
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

        private Handler handler = new Handler();

        private ArrayList<Integer> scriptsBeingRemoved = new ArrayList<>();
        private boolean undoShown = false;
        private HashMap<Integer, Runnable> removalRunnables = new HashMap<>();

        ScriptListAdapter(Context context, ArrayList<Script> scripts)
        {
            this.context = context;
            this.scripts = scripts;
        }

        public boolean isUndoShown() {
            return undoShown;
        }

        public boolean isBeingRemoved(int pos) {
            return scriptsBeingRemoved.contains(pos);
        }

        public void remove(final int pos)
        {
            final Script script = scripts.get(pos);
            scriptsBeingRemoved.add(pos);
            notifyItemChanged(pos);
            undoShown = true;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    getContext().getContentResolver()
                            .delete(ScriptsProvider.SCRIPT_BASE_URI.buildUpon().appendPath(script.getId().toString()).build(), null, null);
                    scripts.remove(scripts.indexOf(script));
                    scriptsBeingRemoved.remove(scriptsBeingRemoved.indexOf(pos));
                    undoShown = false;
                    notifyItemRemoved(pos);
                    setRecyclerViewVisibility(scripts.size() > 0);
                }
            };
            handler.postDelayed(runnable, 2000);
            removalRunnables.put(pos, runnable);
        }

        @Override
        public ScriptListAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(context).inflate(R.layout.script_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            Script script = scripts.get(position);

            if (scriptsBeingRemoved.contains(position)) {
                holder.contentContainer.measure(0, 0);
                holder.undo.getLayoutParams().height = holder.contentContainer.getMeasuredHeight();
                holder.undo.setVisibility(View.VISIBLE);
                holder.contentContainer.setVisibility(View.GONE);
                holder.undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        handler.removeCallbacks(removalRunnables.get(pos));
                        int index = scriptsBeingRemoved.indexOf(pos);
                        scriptsBeingRemoved.remove(index);
                        notifyItemChanged(pos);
                    }
                });
            } else {
                holder.undo.setVisibility(View.GONE);
                holder.contentContainer.setVisibility(View.VISIBLE);
                holder.title.setText(script.getTitle());
                holder.excerpt.setText(script.getContent());

                calendar.setTimeInMillis(script.getTimestamp() * 1000);
                holder.date.setText(format.format(calendar.getTime()));
            }
        }

        @Override
        public int getItemCount() {
            return scripts.size();
        }

        class VH extends RecyclerView.ViewHolder {

            public View contentContainer;
            public View undo;

            public TextView title;
            public TextView excerpt;
            public TextView date;

            public VH(View itemView) {
                super(itemView);

                contentContainer = itemView.findViewById(R.id.content_container);
                undo =  itemView.findViewById(R.id.undo);

                title = (TextView) itemView.findViewById(R.id.script_title);
                excerpt = (TextView) itemView.findViewById(R.id.script_content_excerpt);
                date = (TextView) itemView.findViewById(R.id.script_date);
            }
        }
    }

    private class ScriptItemTouchCallback extends ItemTouchHelper.SimpleCallback {

        Context context = ScriptListFragment.this.getContext();

        Drawable deleteIcon = context.getDrawable(R.drawable.ic_delete_sweep_white_24dp);
        ColorDrawable bgColor = new ColorDrawable(context.getResources().getColor(R.color.colorAccent));
        int iconMargin = context.getResources().getDimensionPixelSize(R.dimen.swipe_delete_icon_margin);
        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.swipe_delete_icon_size);

        public ScriptItemTouchCallback() {
            super(0, ItemTouchHelper.LEFT);
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            ScriptListAdapter adapter = (ScriptListAdapter) recyclerView.getAdapter();
            if (adapter.isBeingRemoved(viewHolder.getAdapterPosition())) {
                return 0;
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            ScriptListAdapter adapter = (ScriptListAdapter) scriptsRecyclerView.getAdapter();
            adapter.remove(pos);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View view = viewHolder.itemView;

            if (viewHolder.getAdapterPosition() == -1) return;

            bgColor.setBounds(view.getRight() + (int) dX, view.getTop(), view.getRight(), view.getBottom());
            bgColor.draw(c);

            int viewHeight = view.getBottom() - view.getTop();

            int iconRight = view.getRight() - iconMargin;
            int iconLeft = iconRight - iconSize;
            int iconTop = view.getTop() + (viewHeight - iconSize)/2;
            int iconBottom = iconTop + iconSize;

            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            deleteIcon.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
