package net.rimoto.core.utils.UI.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.rimoto.android.R;
import net.rimoto.core.API;
import net.rimoto.core.models.FAQ_Category;
import net.rimoto.core.models.FAQ_Question;
import net.rimoto.core.utils.UI.adapter.FAQ_mainRecycleAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HelpFaqFragment extends HelpFragment {
    private FAQ_mainRecycleAdapter mAdapter;
    private RecyclerView mFaqRecycler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.help_title_faq);

        mFaqRecycler = (RecyclerView) view.findViewById(R.id.faqRecycle);
        mFaqRecycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mFaqRecycler.setLayoutManager(linearLayoutManager);

        API.getInstance().getFAQ(new Callback<List<FAQ_Question>>() {
            @Override
            public void success(List<FAQ_Question> questions, Response response) {
                mAdapter = new FAQ_mainRecycleAdapter(questions, onSelectCategory);
                mFaqRecycler.setAdapter(mAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private FAQ_mainRecycleAdapter.Callback onSelectCategory = new FAQ_mainRecycleAdapter.Callback() {
        @Override
        public void click(FAQ_Category category, View view) {
            HelpCategoryFragment categoryFragment = new HelpCategoryFragment();
            categoryFragment.setCategory(category);

            changeDialogFragment(categoryFragment);
        }
    };
}
