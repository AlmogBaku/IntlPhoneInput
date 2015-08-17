package net.rimoto.core.utils.UI.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import net.rimoto.android.R;
import net.rimoto.core.models.FAQ_Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelpQuestionFragment extends HelpFragment {
    private FAQ_Question mQuestion;

    public void setQuestion(FAQ_Question question) {
        this.mQuestion = question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help_faq, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView faqRecycler = (RecyclerView) view.findViewById(R.id.faqRecycle);
        faqRecycler.setVisibility(View.GONE);


        WebView answerView = (WebView) view.findViewById(R.id.answerWebView);
        answerView.setWebChromeClient(new WebChromeClient());

        answerView.loadData(wrap_html(mQuestion.getQuestion(), mQuestion.getAnswer()), "text/html", null);
        answerView.setBackgroundColor(Color.TRANSPARENT);
        answerView.setVisibility(View.VISIBLE);
    }

    private String wrap_html(CharSequence question, CharSequence answer) {
        Resources resources = getActivity().getResources();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(R.raw.help_faq_wrapper)));

        String html = "";
        String line = null;
        do {
            try {
                line = bufferedReader.readLine();
                if(line!=null) {
                    line = line.replace("{{question}}", question);
                    line = line.replace("{{answer}}", answer);
                    html += line + "\r\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while(line != null);

        return html;
    }
}
