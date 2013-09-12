package so.zudui.friends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import so.zudui.condition.DetailConditions;
import so.zudui.entity.User;
import so.zudui.entity.Friends.Friend;
import so.zudui.launch.activity.R;
import so.zudui.space.FriendSpaceActivity;
import so.zudui.util.EntityTableUtil;
import so.zudui.webservice.WebServiceUtil;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class SurroundingFriendFragment extends Fragment {
	
	private View surroundingFriendView = null;
	private Context context = null;
	
	private SurroundingFriendAdapter adapter = null;
	private ListView listView = null;
	private List<Friend> surroundingsList = new ArrayList<Friend>();
	private List<String> friendBufferList;
	
	private ImageButton confirmBtn = null;
	
	private User user = EntityTableUtil.getMainUser();
	private ProgressDialog progressDialog = null;
	
	public SurroundingFriendFragment(Context context) {
		this.context = context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		new GetSurroundingFriendTask().execute();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		surroundingFriendView = inflater.inflate(R.layout.layout_surrounding_friend, container, false);
		listView = (ListView) surroundingFriendView.findViewById(R.id.surrounding_friend_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
						EntityTableUtil.setSurroundingsList(surroundingsList);
						Intent intent = new Intent(context, FriendSpaceActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("position", position);
						bundle.putInt("condition", DetailConditions.ADD_FRIEND_PAGE);
						intent.putExtra("bundle", bundle);
						context.startActivity(intent);
			}
		});
		confirmBtn = (ImageButton) surroundingFriendView.findViewById(R.id.surrounding_friend_imagebtn_confirm);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (adapter != null) {
					friendBufferList = adapter.getFriendBufferList();
					setNewFriendIds();
					new AddFriendTask().execute();
				}
			}
		});
		return surroundingFriendView;
	}
	
	private String newFriendIds;
	
	private void setNewFriendIds() {
		StringBuffer newFriendIdsBuffer = new StringBuffer();
		for (int i = 0; i < friendBufferList.size(); i++)
			newFriendIdsBuffer.append(friendBufferList.get(i));
		newFriendIds = newFriendIdsBuffer.toString();
		user.addFriendIds(newFriendIdsBuffer.toString());
	}
	
	private class AddFriendTask extends AsyncTask<Void, Void, Integer> {
		
		public AddFriendTask() {
			progressDialog = ProgressDialog.show(context, null, "正在添加好友...", true);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			// TODO 更新至服务器
			result = webServiceUtil.addFriend(newFriendIds);
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			if (result == WebServiceUtil.SUCCESS) {
				// TODO 跳转回好友列表
				Toast.makeText(context, "添加好友成功", Toast.LENGTH_SHORT).show();
				SurroundingFriendFragment.this.getActivity().finish();
			} else {
				Toast.makeText(context, "添加好友失败", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	private class GetSurroundingFriendTask extends AsyncTask<Void, Void, Integer> {
		
		public GetSurroundingFriendTask() {
			progressDialog = ProgressDialog.show(context, null, "正在查找附近的人...", true);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			result = webServiceUtil.querySurrounding(user.getLongitude(), user.getLatitude());
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			if (result == WebServiceUtil.SUCCESS) {
				surroundingsList = EntityTableUtil.getSurroundingsList();
				//TODO 去掉已经是好友的人
				removeCheckedFriend();
				
				adapter = new SurroundingFriendAdapter(context, surroundingsList);
				listView.setAdapter(adapter);
			} else if (result == WebServiceUtil.EMPTY) {
				Toast.makeText(context, "附近没有用户哦", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "查找附近用户失败", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	private void removeCheckedFriend() {
		Iterator<Friend> iterator = surroundingsList.iterator();
		String friendIds = user.getFriendIds();
		if(friendIds != null) {
			while (iterator.hasNext()) {
				Friend surrounding = (Friend) iterator.next();
				if(friendIds.contains(surrounding.getUid()))
					iterator.remove();
			}
		}
	}

}
