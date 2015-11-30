package net.rimoto.intlphoneinput;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CountrySpinnerAdapter extends ArrayAdapter<Country> {
    private LayoutInflater mLayoutInflater;

    public CountrySpinnerAdapter(Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_country, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.intl_phone_edit__country__item_image);
            viewHolder.mNameView = (TextView) convertView.findViewById(R.id.intl_phone_edit__country__item_name);
            viewHolder.mDialCode = (TextView) convertView.findViewById(R.id.intl_phone_edit__country__item_dialcode);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Country country = getItem(position);
        viewHolder.mImageView.setImageResource(getFlagResource(country));
        viewHolder.mNameView.setText(country.getName());
        viewHolder.mDialCode.setText(String.format("+%s", country.getDialCode()));
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Country country = getItem(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.selected_country, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.intl_phone_edit__country__selected_image);
        imageView.setImageResource(getFlagResource(country));

        return convertView;
    }

    private int getFlagResource(Country country) {
        return getContext().getResources().getIdentifier("country_" + country.getIso().toLowerCase(), "drawable", getContext().getPackageName());
    }


    private static class ViewHolder {
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mDialCode;
    }
}
