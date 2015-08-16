package net.rimoto.core.utils.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.rimoto.android.R;
import net.rimoto.core.models.FAQ_Category;
import net.rimoto.core.models.FAQ_Question;


public class HelpDialogCategory extends DialogFragment {
    private FAQ_Category mCategory;
    private FAQ_categoryRecycleAdapter mAdapter;
    private RecyclerView mFaqRecycler;

    public void setCategory(FAQ_Category category) {
        this.mCategory = category;
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

        mFaqRecycler = (RecyclerView) view.findViewById(R.id.faqRecycle);
        mFaqRecycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mFaqRecycler.setLayoutManager(linearLayoutManager);

        mAdapter = new FAQ_categoryRecycleAdapter(mCategory.getQuestions(), onSelectQuestion);
        mFaqRecycler.setAdapter(mAdapter);
    }

    private FAQ_categoryRecycleAdapter.Callback onSelectQuestion = new FAQ_categoryRecycleAdapter.Callback() {
        @Override
        public void click(FAQ_Question question, View view) {
            HelpDialogQuestion questionFragment = new HelpDialogQuestion();
            questionFragment.setQuestion(question);

            getChildFragmentManager()
                    .beginTransaction()
                    .add(questionFragment, "dialog")
                    .commit();
        }
    };
}
