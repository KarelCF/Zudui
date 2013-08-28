package so.zudui.entity;

public class Activity {
	
	int id;
	String name;
	String shopname;
	String address;
	int starttime;
	String shoptel;
	int pre_number;
	int activity_type;
	String introduce;
	float longitude;
	float latitude;
	String create_user_uid;
	String join_user_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public String getShoptel() {
		return shoptel;
	}
	public void setShoptel(String shoptel) {
		this.shoptel = shoptel;
	}
	public int getPre_number() {
		return pre_number;
	}
	public void setPre_number(int pre_number) {
		this.pre_number = pre_number;
	}
	public int getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(int activity_type) {
		this.activity_type = activity_type;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public String getCreate_user_uid() {
		return create_user_uid;
	}
	public void setCreate_user_uid(String create_user_uid) {
		this.create_user_uid = create_user_uid;
	}
	public String getJoin_user_id() {
		return join_user_id;
	}
	public void setJoin_user_id(String join_user_id) {
		this.join_user_id = join_user_id;
	}
	
}
