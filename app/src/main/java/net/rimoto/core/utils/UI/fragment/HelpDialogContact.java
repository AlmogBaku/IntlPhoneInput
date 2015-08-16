package net.rimoto.core.utils.UI.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.rimoto.android.R;
import net.rimoto.core.API;
import net.rimoto.core.models.FAQ_Category;
import net.rimoto.core.models.FAQ_Question;
import net.rimoto.core.utils.UI.adapter.FAQ_mainRecycleAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HelpDialogContact extends HelpDialog {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.help_contact, container, false);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(R.string.help_title_contact);


    }

}
