package in.curos.cueprompter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by curos on 3/12/16.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.VH> {

    private Context context;
    private JSONArray array = new JSONArray();

    SearchResultAdapter(Context context) {
        this.context = context;
    }

    public void setArray(JSONArray array)
    {
        this.array = array;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        String title = "";
        try {
            JSONObject page = array.getJSONObject(position);
            title = page.getString("page_title");
            title = title.replace('_', ' ');
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView title;

        public VH(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.script_title);
        }
    }
}
