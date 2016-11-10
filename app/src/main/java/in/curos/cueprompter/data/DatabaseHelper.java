package in.curos.cueprompter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by curos on 6/11/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 5;
    public static final String DB_NAME = "cueprompter";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + CuePrompterContract.ScriptEntry.TABLE);
        db.execSQL(CuePrompterContract.ScriptEntry.TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }
}
