package net.hribi.fri;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hribi.fri.Adapter.MainListAdapter;

public class Intro extends AppCompatActivity {
    private ViewPager vp;
    private SampleFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("Hribi");
        tb.setTitleTextColor(0xffffffff);
        vp = (ViewPager) findViewById(R.id.menuPager);
        pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vp);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener(vp));
        tabLayout.setTabTextColors(0xffffffff, 0xffffffff);
        tabLayout.setSelectedTabIndicatorColor(0xffffffff);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"Gorovja", "Vreme"};

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
                return PageFragment.newInstance(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    public static class PageFragment extends Fragment {
        private static final String ARG_PAGE = "ARG_PAGE";

        static PageFragment newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.main_page, container, false);

            if(getArguments().getInt(ARG_PAGE) == 1) {
                MainListAdapter la = new MainListAdapter(getActivity());
                RecyclerView rv =  v.findViewById(R.id.all_recycler);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(la);
            }
            return v;

        }
    }
}
