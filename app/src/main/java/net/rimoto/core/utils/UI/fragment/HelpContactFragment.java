package net.rimoto.core.utils.UI.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.instabug.library.Instabug;

import net.rimoto.android.R;

public class HelpContactFragment extends HelpFragment {
    private Button mSendBtn;
    private Button mBugBtn;
    private ViewGroup mForm;
    private EditText mMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.help_title_contact);
        mSendBtn = (Button) view.findViewById(R.id.sendBtn);
        mBugBtn  = (Button) view.findViewById(R.id.bugBtn);
        mForm  = (ViewGroup) view.findViewById(R.id.form);
        mMessage = (EditText) view.findViewById(R.id.message);

        mSendBtn.setOnClickListener(onSendClick);
        mBugBtn.setOnClickListener(onBugClick);
    }

    private View.OnClickListener onSendClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mForm.getVisibility()==View.GONE) {
                mForm.setVisibility(View.VISIBLE);
                mBugBtn.setVisibility(View.GONE);
                mSendBtn.setText(R.string.send);
            } else {
                if(mMessage.getText().toString().isEmpty()) {
                    mMessage.setError(getResources().getString(R.string.err_message_required));
                }
                mSendBtn.setEnabled(false);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Thanks, we'll back to you shortly.", Toast.LENGTH_LONG);
                toast.show();
                getActivity().finish();
            }
        }
    };
    private View.OnClickListener onBugClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Instabug.getInstance().invokeBugReporter();
                }
            }, 600);
        }
    };

}
