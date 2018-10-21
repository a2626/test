package com.company.insta.instagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.insta.instagram.CheckLikedImageActivity;
import com.company.insta.instagram.R;
import com.company.insta.instagram.helper.SharedPrefrenceManger;
import com.company.insta.instagram.helper.URLS;
import com.company.insta.instagram.helper.VolleyHandler;
import com.company.insta.instagram.adapter.LikeArrayAdapter;
import com.company.insta.instagram.models.Like;
import com.company.insta.instagram.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.company.insta.instagram.R;

public class FriendsActFragment extends Fragment {
    ListView likes_lv;
    ArrayList<Like> arrayLikesList;
    LikeArrayAdapter likeArrayAdapter;

    private FriendsActFragment.OnFragmentInteractionListener mListener;


    public FriendsActFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_likes, container, false);

        likes_lv = view.findViewById(R.id.likes_lv);
        arrayLikesList = new ArrayList<Like>();
        likeArrayAdapter = new LikeArrayAdapter(getContext(),R.layout.like_single_item,arrayLikesList);

        likes_lv.setAdapter(likeArrayAdapter);

        //like Home fragment

        //get all story id associated with current user_id from likes db
       // getAllStoryIds();

        getAllStoriesThatWeLiked();


        //then

        //get story data from stories database

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    private void getAllStoriesThatWeLiked(){


        User user = SharedPrefrenceManger.getInstance(getContext()).getUserData();
        int user_id = user.getId();

        Log.i("user_id", "getAllStoriesThatWeLiked: user id" + user_id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLS.all_following_activities+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error")){

                                JSONArray jsonObjectStories =  jsonObject.getJSONArray("likes");

                                Log.i("arrayStoriesforLikes",jsonObjectStories.toString());

                                for(int i = 0 ; i<jsonObjectStories.length(); i++){
                                    JSONObject jsonObjectSingleStory = jsonObjectStories.getJSONObject(i);
                                    Log.i("jsonsinglestoryLikes",jsonObjectSingleStory.toString());

                                    //     public Like( String story_image, String story_username, String user_profile, String action) {

                                    Like like = new Like(jsonObjectSingleStory.getString("image_url"),
                                            jsonObjectSingleStory.getString("username"),
                                            jsonObjectSingleStory.getString("profile"),
                                            jsonObjectSingleStory.getString("action"));

                                    arrayLikesList.add(like);

                                }


                                likeArrayAdapter.notifyDataSetChanged();

                            }else{

                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }


        );

        VolleyHandler.getInstance(getContext().getApplicationContext()).addRequetToQueue(stringRequest);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FriendsActFragment.OnFragmentInteractionListener) {
            mListener = (FriendsActFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
