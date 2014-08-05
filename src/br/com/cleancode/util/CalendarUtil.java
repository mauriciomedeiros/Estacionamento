package br.com.cleancode.util;
import java.util.Calendar;
import java.util.Date;


public class CalendarUtil {
	
	private static CalendarUtil calendar = null;
	
	private Calendar cal;
	
	private CalendarUtil() {
		cal = Calendar.getInstance();
	}
	
	public int getHora(Date date){
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public boolean isHoraAntes(Date hora1, Date hora2) {
		cal.setTime(hora1);
		int h1 = cal.get(Calendar.HOUR_OF_DAY);
		cal.setTime(hora2);
		int h2 = cal.get(Calendar.HOUR_OF_DAY);
		return h1 < h2;
	}
	
	public static CalendarUtil getInstance() {
		if(calendar == null)
			calendar = new CalendarUtil();
		return calendar;
	}
}
