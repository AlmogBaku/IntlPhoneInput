package net.rimoto.core.utils.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import net.rimoto.android.R;
import net.rimoto.core.models.FAQ_Question;
import net.rimoto.core.utils.UI.adapter.FAQ_categoryRecycleAdapter;


public class HelpDialogQuestion extends DialogFragment {
    private static final String HTML_WRAPPER =
            "<!DOCTYPE html><html><head>" +
                    "<style>" +
                    "   body { margin-top: 0; font: 14px helvetica; }" +
                    "   h1 { font-size: 1.5em; }" +
                    "</style>" +
                    "</head><body><h1>%s</h1>%s</body></html>";

    private FAQ_Question mQuestion;
    private FAQ_categoryRecycleAdapter mAdapter;

    public void setQuestion(FAQ_Question question) {
        this.mQuestion = question;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.help, container, false);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView faqRecycler = (RecyclerView) view.findViewById(R.id.faqRecycle);
        faqRecycler.setVisibility(View.GONE);


        WebView answerView = (WebView) view.findViewById(R.id.answerWebView);
        answerView.setWebChromeClient(new WebChromeClient());

        String html = String.format(HTML_WRAPPER, mQuestion.getQuestion(), mQuestion.getAnswer());
        answerView.loadData(html, "text/html", null);
        answerView.setBackgroundColor(Color.TRANSPARENT);
        answerView.setVisibility(View.VISIBLE);

    }
}
