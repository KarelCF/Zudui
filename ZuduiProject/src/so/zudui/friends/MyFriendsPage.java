package so.zudui.friends;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import so.zudui.condition.DetailConditions;
import so.zudui.entity.User;
import so.zudui.entity.Friends.Friend;
import so.zudui.launch.activity.R;
import so.zudui.space.FriendSpaceActivity;
import so.zudui.util.EntityTableUtil;
import so.zudui.webservice.WebServiceUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class MyFriendsPage extends SherlockActivity {
	
	private ListView myFriendsListView = null;
	private ImageButton addFriendBtn = null;
	
	private MyFriendsAdapter myFriendsAdapter = null;
	private List<Friend> checkedFriendsList = new ArrayList<Friend>();
	
	private User user = EntityTableUtil.getMainUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_friends_page);
		setTitle(R.string.title_friends_activity);
		
		initMyFriendsActivityView();
		new QueryMyFriendsTask().execute();
		
	}
	
	private void initMyFriendsActivityView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addFriendBtn = (ImageButton) findViewById(R.id.friends_activity_btn_add_friend);
		addFriendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyFriendsPage.this, AddFriendPage.class);
				MyFriendsPage.this.startActivity(intent);
			}
		});
		myFriendsListView = (ListView) findViewById(R.id.friends_activity_listview_friends);
		myFriendsListView.setEmptyView(findViewById(R.id.friends_page_textview_no_friend));
		myFriendsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				EntityTableUtil.setCheckedFriendsList(checkedFriendsList);
				Intent intent = new Intent(MyFriendsPage.this, FriendSpaceActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putInt("condition", DetailConditions.FRIEND_PAGE);
				intent.putExtra("bundle", bundle);
				MyFriendsPage.this.startActivity(intent);
				
			}
		});
	}
	
	private class QueryMyFriendsTask extends AsyncTask<Void, Void, Integer> {
		
		ProgressDialog progressDialog = null;
		
		public QueryMyFriendsTask() {
			progressDialog = ProgressDialog.show(MyFriendsPage.this, null, "读取好友列表中...", true);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			String friendIdsList = user.getFriendIds();
			if (friendIdsList != null && !friendIdsList.equals(",")) {
				WebServiceUtil webServiceUtil = new WebServiceUtil();
				String formatFriendIds = getFormatFriendIds(friendIdsList);
				result = webServiceUtil.queryMyFriends(formatFriendIds);
				if (result == WebServiceUtil.FAILED)
					return result;
				checkedFriendsList = EntityTableUtil.getCheckedFriendsList();
				myFriendsAdapter = new MyFriendsAdapter(MyFriendsPage.this, checkedFriendsList);
			} else {
				result = WebServiceUtil.EMPTY;
			}
			return result;
		}
		
		private String getFormatFriendIds(String friendIdsList) {
			String[] friendIdsArray = friendIdsList.split(",");
			StringBuffer formatFriendIdsBuffer = new StringBuffer();
			for (int i = 1; i < friendIdsArray.length; i++) {
				if (i != friendIdsArray.length - 1) {
					formatFriendIdsBuffer.append("'" + friendIdsArray[i] + "',");
				} else {
					formatFriendIdsBuffer.append("'" + friendIdsArray[i] + "'");
				}
			}
			return formatFriendIdsBuffer.toString();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (result == WebServiceUtil.SUCCESS) {
				myFriendsListView.setAdapter(myFriendsAdapter);
//				Toast.makeText(MyFriendsPage.this, "成功获取好友列表", Toast.LENGTH_SHORT).show();
			} else if (result == WebServiceUtil.EMPTY) {
				// 下方代码解决剩余一个时残留的问题
				if(myFriendsAdapter != null) {
					checkedFriendsList.clear();
					myFriendsAdapter.notifyDataSetChanged();
				}
				Toast.makeText(MyFriendsPage.this, "您还没有添加任何好友, 点击下方添加好友", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MyFriendsPage.this, "获取好友列表失败", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new QueryMyFriendsTask().execute();
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
