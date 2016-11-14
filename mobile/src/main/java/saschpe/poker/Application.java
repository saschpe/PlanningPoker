package saschpe.poker;

import android.os.StrictMode;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
        super.onCreate();
    }
}
