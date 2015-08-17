package net.rimoto.core.utils.UI.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.rimoto.android.R;

public class HelpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help_faq, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button contactBtn = (Button) view.findViewById(R.id.contactBtn);
        if(contactBtn!=null) {
            contactBtn.setOnClickListener(gotoContact);
        }
    }

    private View.OnClickListener gotoContact = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HelpContactFragment fragment = new HelpContactFragment();
            changeDialogFragment(fragment);
        }
    };

    protected void changeDialogFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
