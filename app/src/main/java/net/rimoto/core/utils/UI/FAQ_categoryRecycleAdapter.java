package net.rimoto.core.utils.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rimoto.android.R;
import net.rimoto.core.models.FAQ_Question;

import java.util.List;

public class FAQ_categoryRecycleAdapter extends RecyclerView.Adapter<FAQ_categoryRecycleAdapter.ViewHolder> {
    private List<FAQ_Question> mQuestions;
    private final Callback mCallback;

    public FAQ_categoryRecycleAdapter(List<FAQ_Question> questions, Callback callback) {
        this.mQuestions = questions;
        this.mCallback = callback;
    }

    @Override
    public FAQ_categoryRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_faq_item, parent, false);
        return new ViewHolder(v, mCallback);
    }

    @Override
    public void onBindViewHolder(FAQ_categoryRecycleAdapter.ViewHolder holder, int position) {
        holder.setQuestion(mQuestions.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Callback mCallback;
        private TextView mTitleView;
        private FAQ_Question mQuestion;

        public ViewHolder(View itemView, Callback callback) {
            super(itemView);
            mCallback = callback;
            mTitleView = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(onClickListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(mQuestion, itemView);
            }
        };

        public void setQuestion(FAQ_Question question) {
            this.mQuestion = question;
            mTitleView.setText(question.getQuestion());
        }
    }

    public interface Callback {
        void click(FAQ_Question question, View view);
    }
}
