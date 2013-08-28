package so.zudui.util;

import so.zudui.entity.Activity;
import so.zudui.entity.Game;
import so.zudui.entity.HeaderCode;
import so.zudui.entity.Shop;
import so.zudui.entity.User;

public class EntityTableUtil {
	
	private static Activity activity;
	private static Game game;
	private static Shop shop;
	private static User user;
	private static HeaderCode headerCode;
	
	public static Activity getActivity() {
		return activity;
	}
	public static void setActivity(Activity activity) {
		EntityTableUtil.activity = activity;
	}
	public static Game getGame() {
		return game;
	}
	public static void setGame(Game game) {
		EntityTableUtil.game = game;
	}
	public static Shop getShop() {
		return shop;
	}
	public static void setShop(Shop shop) {
		EntityTableUtil.shop = shop;
	}
	public static User getUser() {
		return user;
	}
	public static void setUser(User user) {
		EntityTableUtil.user = user;
	}
	public static HeaderCode getHeaderCode() {
		return headerCode;
	}
	public static void setHeaderCode(HeaderCode headerCode) {
		EntityTableUtil.headerCode = headerCode;
	}
	
}
