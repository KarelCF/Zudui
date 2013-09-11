package so.zudui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	
	public static String getFormatStartTime(int startTimeInt) {
		String formatStartTime = "";
		long currentTime = System.currentTimeMillis();
		long startTime = (long) startTimeInt * 1000;
		Calendar currentTimeCalendar = Calendar.getInstance();
		Calendar startTimeCalendar = Calendar.getInstance();
		currentTimeCalendar.setTimeInMillis(currentTime);
		startTimeCalendar.setTimeInMillis(startTime);
		int currentDate = currentTimeCalendar.get(Calendar.DATE);
		int startDate = startTimeCalendar.get(Calendar.DATE);
		if (startDate != currentDate) {
			formatStartTime = new SimpleDateFormat( "'明天' HH:mm").format(new Date(startTime));
		} else {
			formatStartTime = new SimpleDateFormat( "'今天' HH:mm").format(new Date(startTime));
		}
		return formatStartTime;
	}
	
}
