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
import net.rimoto.core.API;
import net.rimoto.core.models.FAQ_Category;
import net.rimoto.core.models.FAQ_Question;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HelpDialogMain extends DialogFragment {
    private FAQ_mainRecycleAdapter mAdapter;
    private RecyclerView mFaqRecycler;

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
            HelpDialogCategory categoryFragment = new HelpDialogCategory();
            categoryFragment.setCategory(category);

            getChildFragmentManager()
                    .beginTransaction()
                    .add(categoryFragment, "dialog")
                    .commit();
        }
    };

}
