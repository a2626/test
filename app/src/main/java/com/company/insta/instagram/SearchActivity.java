package com.company.insta.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.insta.instagram.adapter.SearchListAdapter;
import com.company.insta.instagram.helper.SharedPrefrenceManger;
import com.company.insta.instagram.helper.URLS;
import com.company.insta.instagram.helper.VolleyHandler;
import com.company.insta.instagram.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    EditText search_et;
    ListView search_results_lv;
    ArrayList<User> arrayListUsers;
    SearchListAdapter searchListAdapter;
    int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_et = (EditText) findViewById(R.id.search_et);
        search_results_lv = (ListView) findViewById(R.id.search_results_lv);

        arrayListUsers = new ArrayList<User>();
        searchListAdapter = new SearchListAdapter(SearchActivity.this,R.layout.user_single_item,arrayListUsers);
        search_results_lv.setAdapter(searchListAdapter);


        User user = SharedPrefrenceManger.getInstance(getApplicationContext()).getUserData();
        user_id = user.getId();


        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //getSuggestedUsers();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 0) {
                    //searchListAdapter.clear();
                    getSuggestedUsers();
                }
                else{
                    getSimilarUsers(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length() > 0){
                    getSimilarUsers(s.toString());
                }else{
                    getSuggestedUsers();
                }


            }
        });

    }



    private void getSimilarUsers(String text){
        searchListAdapter.clear();
        //get_suggested_users
        // get_similar_users

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLS.get_similar_users+text,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                JSONArray jsonArrayUsers =  jsonObject.getJSONArray("users");

                                Log.i("arrayUsers",jsonArrayUsers.toString());

                                for(int i = 0 ; i<jsonArrayUsers.length(); i++){
                                    JSONObject jsonObjectSingleUser = jsonArrayUsers.getJSONObject(i);
                                    Log.i("jsonsingleUser",jsonObjectSingleUser.toString());

                                    if(user_id != jsonObjectSingleUser.getInt("id")) {

                                        if (!searchListContainsUser(searchListAdapter, jsonObjectSingleUser.getInt("id")))
                                        {

                                            User user = new User(jsonObjectSingleUser.getInt("id"), jsonObjectSingleUser.getString("email")
                                                    , jsonObjectSingleUser.getString("username"), jsonObjectSingleUser.getString("image")
                                                    , jsonObjectSingleUser.getInt("following"), jsonObjectSingleUser.getInt("followers")
                                                    , jsonObjectSingleUser.getInt("posts"));


                                            searchListAdapter.add(user);
                                        }
                                    }

                                }


                               // searchListAdapter.notifyDataSetChanged();

                            }else{

                                Toast.makeText(SearchActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }

                           // Log.i("searchListAdapter COUNT","" + searchListAdapter.getCount());
                           // for(int i = 0 ; i<searchListAdapter.getCount(); i++){
                            //    Log.i("searchListAdapter","" + searchListAdapter.getItem(i).getId() +  searchListAdapter.getItem(i).getUsername());
                         //   }


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }


        );

        VolleyHandler.getInstance(getApplicationContext()).addRequetToQueue(stringRequest);



    }


    private void getSuggestedUsers(){
        searchListAdapter.clear();
        //get_suggested_users
        // get_similar_users

        User user = SharedPrefrenceManger.getInstance(this).getUserData();
        int id = user.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLS.get_suggested_users+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                    /*" select COUNT(u.id) as common, u.id, u.username, u.image from users u, following f
                                        where u.id = f.user_id and f.user_id != ? and
                                        other_user_id in (select other_user_id from following where user_id = ?)
                                        and user_id not in (select other_user_id from following where user_id = ?)
                                        group by u.username order by common desc "*/

                                JSONArray jsonArrayUsers =  jsonObject.getJSONArray("users");

                                Log.i("arrayUsers",jsonArrayUsers.toString());

                                for(int i = 0 ; i<jsonArrayUsers.length(); i++){
                                    JSONObject jsonObjectSingleUser = jsonArrayUsers.getJSONObject(i);
                                    Log.i("jsonsingleUser",jsonObjectSingleUser.toString());

                                    if(user_id != jsonObjectSingleUser.getInt("id")) {

                                        if (!searchListContainsUser(searchListAdapter, jsonObjectSingleUser.getInt("id")))
                                        {

                                            User user = new User(jsonObjectSingleUser.getInt("id"), ""
                                                    , jsonObjectSingleUser.getString("username"), jsonObjectSingleUser.getString("image")
                                                    , 0, 0
                                                    , 0);


                                            searchListAdapter.add(user);
                                        }
                                    }

                                }


                                // searchListAdapter.notifyDataSetChanged();

                            }else{

                                Toast.makeText(SearchActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }

                            // Log.i("searchListAdapter COUNT","" + searchListAdapter.getCount());
                            // for(int i = 0 ; i<searchListAdapter.getCount(); i++){
                            //    Log.i("searchListAdapter","" + searchListAdapter.getItem(i).getId() +  searchListAdapter.getItem(i).getUsername());
                            //   }


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }


        );

        VolleyHandler.getInstance(getApplicationContext()).addRequetToQueue(stringRequest);



    }





    boolean searchListContainsUser(SearchListAdapter searchListAdapter, int userId){
        for(int i = 0 ; i<searchListAdapter.getCount(); i++){
            if (searchListAdapter.getItem(i).getId() == userId){
                return true;
            }
        }

        return false;
    }
}
