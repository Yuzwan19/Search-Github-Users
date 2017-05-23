package com.yuzwan.githubapi;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yuzwan.githubapi.adapter.UserAdapter;
import com.yuzwan.githubapi.helper.Constant;
import com.yuzwan.githubapi.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AQuery aQuery;
    private EditText edt_search;
    private ListView listView;
    private final static String TAG = MainActivity.class.getSimpleName();
    private ArrayList listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        aQuery = new AQuery(MainActivity.this);
        listItem = new ArrayList<>();

        edt_search.addTextChangedListener(filterTextWatcher);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            checkLimit();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    private void checkLimit() {
        String url = Constant.URL_LIMIT;
        aQuery.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONObject json = new JSONObject(object);
                        JSONObject resource = json.getJSONObject("resources");
                        JSONObject search = resource.getJSONObject("search");
                        int remaining = search.getInt("remaining");
                        if (remaining == 0) {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.limit_alert), Toast.LENGTH_LONG).show();
                        } else {
                            searchUser();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }

                }
            }
        });

    }

    private void searchUser() {
        listItem.clear();
        String username = edt_search.getText().toString();
        Uri builtUri = Uri.parse(Constant.URL_SEARCH).buildUpon()
                .appendQueryParameter("q", username)
                .build();
        String url = builtUri.toString();
        Log.d(TAG, url);

        aQuery.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONObject json = new JSONObject(object);
                        Log.d(TAG, object);
                        UserModel userModel;
                        JSONArray items = json.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject a = items.getJSONObject(i);
                            String login = a.getString("login");
                            String avatar = a.getString("avatar_url");
                            userModel = new UserModel();
                            userModel.setUsername(login);
                            userModel.setImage(avatar);
                            listItem.add(userModel);
                        }

                        UserAdapter adapter = new UserAdapter(MainActivity.this, listItem);
                        listView.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.not_found),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void setupView() {
        edt_search = (EditText) findViewById(R.id.edt_search);
        listView = (ListView) findViewById(R.id.list_user);
    }
}
