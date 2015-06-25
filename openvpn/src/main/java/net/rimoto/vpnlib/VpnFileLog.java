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
    public static final String LOG_FILENAME = "rimoto.log";
    public static final File logFile = new File(Environment.getExternalStorageDirectory(), LOG_FILENAME);
    private static final int MAX_LINES=2500;
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
        resizeLogFile();
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

    private static Thread mThread;
    public static void resizeLogFile() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    VpnFileLog.resizedLogs();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.run();
    }

    public static ArrayList<String> resizedLogs() throws IOException {
        BufferedReader d = new BufferedReader(new FileReader(logFile));

        ArrayList<String> logs = new ArrayList<>();

        String log;
        do {
            log = d.readLine();
            logs.add(log);
        } while(log != null);


        if(logs.size()>MAX_LINES) {
            while(logs.size()>MAX_LINES) logs.remove(0);

            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
            for(String l:logs) {
                bw.append(l);
                bw.newLine();
            }
            bw.flush();
        }

        return logs;
    }

    public static String getLogs() throws IOException {
        ArrayList<String> logsArr = resizedLogs();
        String logs = "";

        for(String log : logsArr) {
            logs += log  + "\r\n";
        }

        return logs;
    }
}