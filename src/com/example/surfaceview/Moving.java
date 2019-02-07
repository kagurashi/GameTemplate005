package com.example.surfaceview;

import android.app.*;
import android.os.*;
import android.support.v4.view.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.widget.*;
import android.support.design.widget.TabLayout;

public class Moving extends Activity 
{
    private TabLayout tabLayout;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moving);

		tabLayout = (TabLayout)findViewById(R.id.tab_layout);
		
		ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MyPagerAdapter());
	}


	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
		@Override
		public Object instantiateItem(View container, int position) {
            int[] pages = { R.layout.map, R.layout.status};
            int[] lists = {R.id.somethingList, R.id.eventList};

            View layout ;
			List<String> items = new ArrayList<String>();
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(position == 0) {
				items.add("日本");
				items.add("アメリカ");
				items.add("中国");
				items.add("イギリス");
			} else if(position == 1) {
				items.add("東京");
				items.add("ワシントン D.C");
				items.add("北京");
				items.add("ロンドン");
			} 
            layout = inflater.inflate(pages[position], null);
            ((ViewPager) container).addView(layout);

			ListView list = (ListView) findViewById(lists[position]);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>( Moving.this, R.layout.data_list,R.id.row_textview1, items);

			list.setAdapter(adapter);

			return layout;

		}
		@Override
		public int getCount() {
			return 2;
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
	}

}
