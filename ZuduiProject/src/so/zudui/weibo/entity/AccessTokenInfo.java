package so.zudui.weibo.entity;

public class AccessTokenInfo {
	
	private String access_token;
	private String remind_in;
	private String expires_in;
	private long uid;
	
	public String getAccessToken() {
		return access_token;
	}
	public String getRemindIn() {
		return remind_in;
	}
	public String getExpiresIn() {
		return expires_in;
	}
	public long getUid() {
		return uid;
	}
	
	
}
