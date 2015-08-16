package net.rimoto.core.utils.UI.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.rimoto.android.R;
import net.rimoto.core.models.FAQ_Category;
import net.rimoto.core.models.FAQ_Question;
import net.rimoto.core.utils.UI.adapter.FAQ_categoryRecycleAdapter;
import net.rimoto.core.utils.UI.HelpDialogQuestion;


public class HelpDialogCategory extends HelpDialog {
    private FAQ_Category mCategory;
    private FAQ_categoryRecycleAdapter mAdapter;
    private RecyclerView mFaqRecycler;

    public void setCategory(FAQ_Category category) {
        this.mCategory = category;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(R.string.help_title_category);

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

            changeDialogFragment(questionFragment);
        }
    };
}
