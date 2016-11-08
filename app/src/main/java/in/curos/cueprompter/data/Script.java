package in.curos.cueprompter.data;

import android.content.ContentValues;

/**
 * Created by curos on 6/11/16.
 */
public class Script {
    // This class is a POJO for representing a Script from the Database
    private Long id;
    private String title;
    private String content;
    private Long timestamp;

    public static Script populate(ContentValues contentValues) {
        Script script = new Script();
        script.setId(contentValues.getAsLong(CuePrompterContract.ScriptEntry._ID));
        script.setTitle(contentValues.getAsString(CuePrompterContract.ScriptEntry.TITLE));
        script.setContent(contentValues.getAsString(CuePrompterContract.ScriptEntry.CONTENT));
        script.setTimestamp(contentValues.getAsLong(CuePrompterContract.ScriptEntry.TIMESTAMP));
        return script;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (null == o) return false;
        else {
            Script script = (Script) o;
            try {
                return script.getId().equals(getId()) &&
                        script.getTitle().equals(getTitle()) &&
                        script.getContent().equals(getContent()) &&
                        script.getTimestamp().equals(getTimestamp());
            } catch (NullPointerException e) {
                return false;
            }
        }
    }
}
