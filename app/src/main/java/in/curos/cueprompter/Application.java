package in.curos.cueprompter;

import android.preference.PreferenceManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import in.curos.cueprompter.data.CuePrompterContract;

/**
 * Created by curos on 10/11/16.
 */
public class Application extends android.app.Application {
    Tracker tracker;

    @Override
    public void onCreate() {
        boolean hasRun = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("APP_RUN", false);

        if (!hasRun) {
            CuePrompterContract.addDummyContent(this);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("APP_RUN", true).commit();
        }
        super.onCreate();
    }

    public Tracker startTracking()
    {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.analytics);
            tracker.enableAutoActivityTracking(true);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.setLocalDispatchPeriod(10);
        }

        return tracker;
    }
}
