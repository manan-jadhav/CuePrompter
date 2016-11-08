package in.curos.cueprompter.data;


/**
 * Created by curos on 6/11/16.
 */
public class CuePrompterContract {

    public static final String AUTHORITY = "in.curos.cueprompter";

    public static final class ScriptEntry {

        public static final String TABLE = "scripts";

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String TIMESTAMP = "timestamp";

        public static final String TABLE_CREATE_QUERY =
                "create table "+ TABLE + " (" +
                        _ID + " integer primary key autoincrement, "+
                        TITLE + " text not null, "+
                        CONTENT + " text not null, "+
                        TIMESTAMP + " integer not null" +
                        ");";
    }
}
