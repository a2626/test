package com.company.insta.instagram.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.company.insta.instagram.R;
import com.company.insta.instagram.helper.ActivityFeedAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ActivityFeedFragment extends Fragment implements FriendsActFragment.OnFragmentInteractionListener, LikesFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.feedtabs)
    TabLayout tabLayout;

    @BindView(R.id.feedviewpager)
    ViewPager viewPager;

    View view;

    FriendsActFragment friendsActFragment = new FriendsActFragment();
    LikesFragment likesFragment = new LikesFragment();

    private FragmentActivity myContext;

    public ActivityFeedFragment() {
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
        view = inflater.inflate(R.layout.fragment_activity_feed, container, false);

        viewPager = (ViewPager)view.findViewById(R.id.feedviewpager);
        tabLayout = (TabLayout)view.findViewById(R.id.feedtabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragManager = myContext.getSupportFragmentManager();
        final ActivityFeedAdapter adapter = new ActivityFeedAdapter(fragManager);

        // adding filter list fragment
        friendsActFragment = new FriendsActFragment();

        // adding edit image fragment
        likesFragment = new LikesFragment();

        adapter.addFragment(friendsActFragment, getString(R.string.tab_friendsAct));
        adapter.addFragment(likesFragment, getString(R.string.tab_myAct));

        viewPager.setAdapter(adapter);
    }
}
