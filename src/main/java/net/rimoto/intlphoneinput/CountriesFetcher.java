package net.rimoto.intlphoneinput;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountriesFetcher {
    private static CountryList mCountries;

    private static String getCountriesJson(Context context) {
        String json;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.countries);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static CountryList getCountries(Context context) {
        if(mCountries != null) {
            return mCountries;
        }
        mCountries = new CountryList();
        try {
            JSONArray countries = new JSONArray(getCountriesJson(context));
            for(int i=0; i<countries.length(); i++) {
                try {
                    JSONObject country = (JSONObject) countries.get(i);
                    mCountries.add(new Country(country.getString("name"), country.getString("iso2"), country.getInt("dialCode")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mCountries;
    }


    public static class CountryList extends ArrayList<Country> {
        public int indexOfIso(String iso) {
            for(int i=0; i<this.size(); i++) {
                if(this.get(i).getIso().toUpperCase().equals(iso.toUpperCase())) {
                    return i;
                }
            }
            return -1;
        }
        public int indexOfDialCode(int dialCode) {
            for(int i=0; i<this.size(); i++) {
                if(this.get(i).getDialCode() == dialCode) {
                    return i;
                }
            }
            return -1;
        }
    }
}
