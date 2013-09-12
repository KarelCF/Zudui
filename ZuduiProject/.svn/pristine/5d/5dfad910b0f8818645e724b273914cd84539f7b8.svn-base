package so.zudui.friends;

import so.zudui.launch.activity.R;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TabPageIndicator;

public class AddFriendPage extends SherlockFragmentActivity {
	
	private AddFriendFragmentAdapter addfriendAdapter = null;
	private ViewPager viewPager = null;
	private TabPageIndicator indicator = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_friend_page);
		setTitle("添加好友");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addfriendAdapter = new AddFriendFragmentAdapter(this.getSupportFragmentManager(), this);
		
		// 给ViewPager设置Adapter
		viewPager = (ViewPager) findViewById(R.id.add_friend_viewpager); 
		viewPager.setAdapter(addfriendAdapter);
		
		// indicator的设置
		indicator = (TabPageIndicator) findViewById(R.id.add_friend_indicator);
		indicator.setViewPager(viewPager);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
