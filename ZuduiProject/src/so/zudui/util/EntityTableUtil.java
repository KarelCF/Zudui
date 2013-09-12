package so.zudui.util;

import java.util.ArrayList;
import java.util.List;

import so.zudui.entity.Activities;
import so.zudui.entity.Friends;
import so.zudui.entity.Friends.Friend;
import so.zudui.entity.Games;
import so.zudui.entity.Shop;
import so.zudui.entity.User;
import so.zudui.entity.Activities.Activity;

public class EntityTableUtil {
	
	private static Activities myActivities;
	private static List<Activity> myActivitiesList;
	private static Activities homePageActivities;
	private static List<Activity> homeActivitiesList;
	
	private static Games games;
	private static Shop shop;
	private static User mainUser;
	private static User participantUser;
	private static User friendUser;
	
	private static Friends friends;
	private static List<Friend> friendsList = new ArrayList<Friend>();
	private static Activities friendActivities;
	private static List<Activity> friendActivitiesList = new ArrayList<Activity>();
	
	private static Friends surroundings;
	private static List<Friend> surroundingsList = new ArrayList<Friend>();
	
	private static Friends checkedFriends;
	private static List<Friend> checkedFriendsList = new ArrayList<Friend>();
	
	public static Activities getMyActivities() {
		return myActivities;
	}
	public static void setMyActivities(Activities activities) {
		EntityTableUtil.myActivities = activities;
	}
	public static Activities getHomePageActivities() {
		return homePageActivities;
	}
	
	public static List<Activity> getMyActivitiesList() {
		return myActivitiesList;
	}
	public static void setMyActivitiesList(List<Activity> myActivitiesList) {
		EntityTableUtil.myActivitiesList = myActivitiesList;
	}
	
	public static void setHomePageActivities(Activities homePageActivities) {
		EntityTableUtil.homePageActivities = homePageActivities;
	}
	public static List<Activity> getHomeActivitiesList() {
		return homeActivitiesList;
	}
	public static void setHomeActivitiesList(List<Activity> homeActivitiesList) {
		EntityTableUtil.homeActivitiesList = homeActivitiesList;
	}
	public static Games getGames() {
		return games;
	}
	public static void setGames(Games games) {
		EntityTableUtil.games = games;
	}
	public static Shop getShop() {
		return shop;
	}
	public static void setShop(Shop shop) {
		EntityTableUtil.shop = shop;
	}
	public static User getMainUser() {
		return mainUser;
	}
	public static void setMainUser(User user) {
		EntityTableUtil.mainUser = user;
	}
	public static User getParticipantUser() {
		return participantUser;
	}
	public static void setParticipantUser(User participantUser) {
		EntityTableUtil.participantUser = participantUser;
	}
	public static User getFriendUser() {
		return friendUser;
	}
	public static void setFriendUser(User friendUser) {
		EntityTableUtil.friendUser = friendUser;
	}
	public static Friends getFriends() {
		return friends;
	}
	public static void setFriends(Friends friends) {
		EntityTableUtil.friends = friends;
	}
	//  TODO 活动内页点击头像进入他人空间
	public static int getFriendsListSize() {
		return friendsList.size();
	}
	public static void clearFriendsList() {
		EntityTableUtil.friendsList.clear();
	}
	public static void addFriendsList(Friend friend) {
		EntityTableUtil.friendsList.add(friend);
	}
	public static List<Friend> getFriendsList() {
		return friendsList;
	}
	public static void setFriendsList(List<Friend> friendsList) {
		EntityTableUtil.friendsList = friendsList;
	}
	public static Activities getFriendActivities() {
		return friendActivities;
	}
	public static void setFriendActivities(Activities friendActivities) {
		EntityTableUtil.friendActivities = friendActivities;
	}
	public static List<Activity> getFriendActivitiesList() {
		return friendActivitiesList;
	}
	public static void setFriendActivitiesList(List<Activity> friendActivitiesList) {
		EntityTableUtil.friendActivitiesList = friendActivitiesList;
	}
	public static Friends getSurroundings() {
		return surroundings;
	}
	public static void setSurroundings(Friends surroundings) {
		EntityTableUtil.surroundings = surroundings;
	}
	public static List<Friend> getSurroundingsList() {
		return surroundingsList;
	}
	public static void setSurroundingsList(List<Friend> surroundingsList) {
		EntityTableUtil.surroundingsList = surroundingsList;
	}
	public static void clearSurroundingsList() {
		EntityTableUtil.surroundingsList.clear();
	}
	public static Friends getCheckedFriends() {
		return checkedFriends;
	}
	public static void setCheckedFriends(Friends checkedFriends) {
		EntityTableUtil.checkedFriends = checkedFriends;
	}
	public static List<Friend> getCheckedFriendsList() {
		return checkedFriendsList;
	}
	public static void setCheckedFriendsList(List<Friend> checkedFriendsList) {
		EntityTableUtil.checkedFriendsList = checkedFriendsList;
	}
}
