package gmedia.net.id.OnTime.approval;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.approval.cuti.CutiFragment;
import gmedia.net.id.OnTime.approval.ijin.IjinFragment;
import gmedia.net.id.OnTime.approval.reimburse.ReimburseFragment;

public class ApprovalFragment extends Fragment {

    View view;
    private TabLayout mTabLayout;
    TextView title;

    public ApprovalFragment() {
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
        view = inflater.inflate(R.layout.fragment_approval, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        final MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
        pagerAdapter.SetOnSelectView(mTabLayout, 0);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int c = tab.getPosition();
                pagerAdapter.SetOnSelectView(mTabLayout, c);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int c = tab.getPosition();
                pagerAdapter.SetUnSelectView(mTabLayout, c);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 3;
        TextView title;
        private final String[] mTabsTitle = {"Cuti", "Ijin", "Reimburse"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_approval, null);
            title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            return view;
        }

        public void SetOnSelectView(TabLayout tabLayout, int position) {
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            View selected = tab.getCustomView();
            TextView iv_text = (TextView) selected.findViewById(R.id.title);
            iv_text.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        }

        public void SetUnSelectView(TabLayout tabLayout, int position) {
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            View selected = tab.getCustomView();
            TextView iv_text = (TextView) selected.findViewById(R.id.title);
            iv_text.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new CutiFragment();
                case 1:
                    return new IjinFragment();
                case 2:
                    return new ReimburseFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }


}