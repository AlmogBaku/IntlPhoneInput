package net.rimoto.core.utils.UI.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rimoto.android.R;
import net.rimoto.core.models.FAQ_Category;
import net.rimoto.core.models.FAQ_Question;

import java.util.ArrayList;
import java.util.List;

public class FAQ_mainRecycleAdapter extends RecyclerView.Adapter<FAQ_mainRecycleAdapter.ViewHolder> {
    private final Callback mCallback;
    private List<FAQ_Category> mCategories;

    /**
     * Constructor
     * @param questions List<FAQ_Question>
     */
    public FAQ_mainRecycleAdapter(List<FAQ_Question> questions, Callback callback) {
        this.mCategories = questionsToCategoriesList(questions);
        this.mCallback = callback;
    }

    /**
     * Convert policy list to tags->services list
     * @param questions List<FAQ_Question>
     * @return HashMap<String, List<FAQ_Question>>
     */
    private List<FAQ_Category> questionsToCategoriesList(List<FAQ_Question> questions) {
        List<FAQ_Category> categories = new ArrayList<>();
        for(FAQ_Question q:questions) {
            FAQ_Category cat = new FAQ_Category(q.getCategory());
            int contains = categories.indexOf(cat);
            if(contains==-1) {
                categories.add(cat);
            } else {
                cat = categories.get(contains);
            }

            cat.getQuestions().add(q);
        }
        return categories;
    }

    @Override
    public FAQ_mainRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_faq_item, parent, false);

        return new ViewHolder(v, mCallback);
    }

    @Override
    public void onBindViewHolder(FAQ_mainRecycleAdapter.ViewHolder holder, int position) {
        holder.setCategory(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Callback mCallback;
        private TextView mTitleView;
        private FAQ_Category mCategory;

        public ViewHolder(View itemView, Callback callback) {
            super(itemView);
            mCallback = callback;

            mTitleView = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(onClickListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(mCategory, itemView);
            }
        };

        public void setCategory(FAQ_Category cat) {
            mTitleView.setText(cat.getName());
            mCategory = cat;
        }
    }

    public interface Callback {
        void click(FAQ_Category category, View view);
    }
}
