package com.example.whowroteit;

import android.os.AsyncTask;
import android.os.TestLooperManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String,Void,String> {
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    FetchBook(TextView titleText, TextView authorText){
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
    }
    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray= jsonObject.getJSONArray("items");
            int i = 0;
            String title =null;
            String authors= null;
            while(i<itemsArray.length() && (authors == null && title == null)){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeinfo");
                try{
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                }catch (Exception e){
                    e.printStackTrace();
                }
                i++;
            }
            if(title != null && authors != null){
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            }
            else{
                mTitleText.get().setText(R.string.no_result);
                mAuthorText.get().setText("");
            }
        }catch (JSONException e){
            mTitleText.get().setText(R.string.no_result);
            mAuthorText.get().setText("");
            e.printStackTrace();
        }
    }
}
