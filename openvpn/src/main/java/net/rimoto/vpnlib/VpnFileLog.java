package net.rimoto.vpnlib;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import de.blinkt.openvpn.core.VpnStatus;

/**
 * Log VPN to file
 */
public class VpnFileLog implements VpnStatus.LogListener {
    private static final String LOG_FILENAME = "rimoto.log";
    private static final File logFile = new File(Environment.getExternalStorageDirectory(), LOG_FILENAME);
    private static final int MAX_LINES=3000;
    private static BufferedWriter bufferedWriter;
    static {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void init() throws IOException {
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));

        BufferedReader d = new BufferedReader(new FileReader(logFile));
        ArrayList<String> logs = new ArrayList<>();

        String log;
        do {
            log = d.readLine();
            logs.add(log);
        } while(log != null);


        if(logs.size()>MAX_LINES) {
            while(logs.size()>MAX_LINES) logs.remove(0);

            for(String l:logs) {
                bufferedWriter.append(l);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        }
    }

    @Override
    public void newLog(VpnStatus.LogItem logItem) {
        String text = VpnLog.LogItem_toLine(logItem);
        try {
            bufferedWriter.append(text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLogs() throws IOException {
        BufferedReader d = new BufferedReader(new FileReader(logFile));
        String logs = "";

        String log;
        do {
            log = d.readLine();
            logs += log  + "\r\n";
        } while(log != null);

        return logs;
    }
}