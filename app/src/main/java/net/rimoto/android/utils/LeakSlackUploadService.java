package net.rimoto.android.utils;

import android.util.Log;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public final class LeakSlackUploadService extends DisplayLeakService {

    /** See https://api.slack.com/ for documentation. */
    public interface SlackApi {
        String TOKEN = "xoxb-6762386405-RgquQTgsXh5pGogThtkhO6RI";

        String MEMORY_LEAK_CHANNEL = "C06MN9EAC";

        @Multipart
        @POST("/api/files.upload")
        UploadFileResponse uploadFile(@Part("token") String token,
                                      @Part("file") TypedFile file,
                                      @Part("filetype") String filetype,
                                      @Part("filename") String filename,
                                      @Part("title") String title,
                                      @Part("initial_comment") String initialComment,
                                      @Part("channels") String channels
        );
    }

    public static class UploadFileResponse {
        boolean ok;
        String error;

        @Override public String toString() {
            return "UploadFileResponse{" +
                    "ok=" + ok +
                    ", error='" + error + '\'' +
                    '}';
        }
    }

    private static final String TAG = "LeakListenerService";

    private static String classSimpleName(String className) {
        int separator = className.lastIndexOf('.');
        return separator == -1 ? className : className.substring(separator + 1);
    }

    private SlackApi slackApi;

    @Override
    public void onCreate() {
        super.onCreate();
        slackApi = new RestAdapter.Builder()
                .setEndpoint("https://slack.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(SlackApi.class);
    }

    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        if (!result.leakFound || result.excludedLeak) {
            return;
        }
        String name = classSimpleName(result.className);
        if (!heapDump.referenceName.equals("")) {
            name += "(" + heapDump.referenceName + ")";
        }

        String title = name + " has leaked";
        String initialComment = leakInfo;
        try {
            TypedFile file = new TypedFile("application/octet-stream", heapDump.heapDumpFile);
            String fileName = heapDump.heapDumpFile.getName();
            slackApi.uploadFile(SlackApi.TOKEN, file, null, fileName, title, initialComment, SlackApi.MEMORY_LEAK_CHANNEL);
        } catch (RetrofitError e) {
            Log.e(TAG, "Error when uploading heap dump", e);
            e.printStackTrace();
        }
    }
}