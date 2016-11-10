package in.curos.cueprompter;

import android.preference.PreferenceManager;

import in.curos.cueprompter.data.CuePrompterContract;

/**
 * Created by curos on 10/11/16.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        boolean hasRun = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("APP_RUN", false);

        if (!hasRun) {
            CuePrompterContract.addDummyContent(this);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("APP_RUN", true).commit();
        }
        super.onCreate();
    }
}
