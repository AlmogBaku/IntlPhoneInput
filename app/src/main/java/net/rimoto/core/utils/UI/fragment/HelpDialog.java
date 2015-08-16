package net.rimoto.core.utils.UI.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.rimoto.android.R;

public class HelpDialog extends DialogFragment {
    protected TextView mTitle;

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

        mTitle = (TextView) view.findViewById(R.id.help_title);

        Button contactBtn = (Button) view.findViewById(R.id.contactBtn);
        if(contactBtn!=null) {
            contactBtn.setOnClickListener(gotoContact);
        }
    }
    private View.OnClickListener gotoContact = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HelpDialogContact fragment = new HelpDialogContact();
            changeDialogFragment(fragment);
        }
    };

    protected void changeDialogFragment(DialogFragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(fragment, "dialog")
                .commit();
    }
}
