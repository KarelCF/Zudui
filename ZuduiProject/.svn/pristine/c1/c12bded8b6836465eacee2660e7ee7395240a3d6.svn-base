package so.zudui.friends;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

// 使用FragmentStatePagerAdapter而不是FragmentPagerAdapter
// 可以避免在从fragment跳转至其他Activity时出现的NullPointerException问题
public class AddFriendFragmentAdapter extends FragmentStatePagerAdapter  {
	
	//指示器的标题
	private static String[] fragmentsIndicator = FriendSource.getFriendFrom();
	
	private int fragmentsTotal = fragmentsIndicator.length;
	
	private ArrayList<Fragment> fragments;
	
	public AddFriendFragmentAdapter(FragmentManager fm, Context context) {
		super(fm);
		fragments = new ArrayList<Fragment>();
		fragments.add(new SurroundingFriendFragment(context));
		fragments.add(new SinaFriendFragment(context));
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
		
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragmentsIndicator[position % fragmentsTotal];
	}
	
	@Override
	public int getCount() {
		return fragmentsTotal;
	}

}
