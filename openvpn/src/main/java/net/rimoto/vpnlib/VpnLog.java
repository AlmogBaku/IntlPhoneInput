package net.rimoto.vpnlib;

import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.blinkt.openvpn.core.VpnStatus;

/**
 * VpnLog
 */
public class VpnLog {
    private class LogcatListener implements VpnStatus.LogListener {
        public static final String TAG = "OpenVPN.VpnStatus";

        @Override
        public void newLog(VpnStatus.LogItem logItem) {
            String msg = logItem.getString(null);
            switch (logItem.getLogLevel()) {
                case INFO:
                    Log.i(TAG, msg);
                    break;
                case ERROR:
                    Log.e(TAG, msg);
                    break;
                case WARNING:
                    Log.w(TAG, msg);
                    break;
                case VERBOSE:
                    Log.v(TAG, msg);
                default:
                case DEBUG:
                    Log.d(TAG, msg);
                    break;
            }
        }
    }

    private static VpnLog mInstance = null;
    public static VpnLog getInstance() {
        if(mInstance == null)
        {
            mInstance = new VpnLog();
        }
        return mInstance;
    }

    private LogcatListener logcatListener = null;
    public void registerLogcat() {
        if(logcatListener==null) {
            logcatListener = new LogcatListener();
            VpnStatus.addLogListener(logcatListener);
        }
    }
    public void unregisterLogcat() {
        VpnStatus.removeLogListener(logcatListener);
        logcatListener=null;
    }

    /**import android.os.Environment;

     * Helper - Convert logLevel to String
     * @param logLevel LogLevel
     * @return String
     */
    public static String LogLevel_toString(VpnStatus.LogLevel logLevel) {
        switch (logLevel) {
            case INFO:      return "INFO";
            case ERROR:     return "ERROR";
            case WARNING:   return "WARNING";
            case VERBOSE:   return "VERBOSE";
            case DEBUG:     return "DEBUG";
            default:        return "?";
        }
    }

    /**
     * Convert millisecond time to Readable String
     * @param time millisecond long
     * @return String
     */
    private static String time_toReadable(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(time));
    }

    /**
     * Helper Convert LongItem to line string
     * @param logItem LogItem
     * @return String
     */
    public static String LogItem_toLine(VpnStatus.LogItem logItem) {
        return String.format("[%s] [%s] %s",
                time_toReadable(logItem.getLogtime()),
                LogLevel_toString(logItem.getLogLevel()),
                logItem.getString(null)
        );
    }

    public static String getRecentLogs() throws IOException {
        return VpnFileLog.getLogs();
    }
}
