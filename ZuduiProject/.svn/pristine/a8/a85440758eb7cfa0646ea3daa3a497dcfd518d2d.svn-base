package so.zudui.util;

import java.util.List;

import so.zudui.condition.HandlerConditions;
import so.zudui.entity.Activities;
import so.zudui.entity.Activities.Activity;
import so.zudui.entity.Games.Game;

public class ActivityInfoUtil {
	
	
	public static void getActivityIconAndStatus(List<Activity> activities, int status) {
		List<Game> gamesCategory = EntityTableUtil.getGames().getGamesCategory();
		for (Activity activity : activities) {
			if (HandlerConditions.GET_MY_UPCOMING_ACTIVITIES == status) {
				activity.setStatus(Activities.STATUS_UPCOMING);
			} else if (HandlerConditions.GET_MY_ONGOING_ACTIVITIES == status) {
				activity.setStatus(Activities.STATUS_ONGOING);
			} else if (HandlerConditions.GET_MY_FINISHED_ACTIVITIES == status) {
				activity.setStatus(Activities.STATUS_FINISHED);
			}
			for (int i = 0; i < gamesCategory.size(); i++) {
				if (gamesCategory.get(i).getId() == activity.getActivityType())
					activity.setIconUrl(gamesCategory.get(i).getIcon());
			}
		}
	}
	
	public static void getActivityIcon(List<Activity> activities) {
		List<Game> gamesCategory = EntityTableUtil.getGames().getGamesCategory();
		for (Activity activity : activities) {
			for (int i = 0; i < gamesCategory.size(); i++) {
				if (gamesCategory.get(i).getId() == activity.getActivityType())
					activity.setIconUrl(gamesCategory.get(i).getIcon());
			}
		}
	}
	
}
