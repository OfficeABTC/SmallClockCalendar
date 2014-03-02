package jp.abtc.simpleclockcalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

public class ClockCalendarService extends Service {
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat sdf1 = new SimpleDateFormat("kk:mm");
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd(EE)");

	private boolean isJapanese = false;
	private String time;
	private String date;
	private String year;
	private String yearG;
	private String[] days = new String[42];
	
	private Calendar now;
//	private Calendar today;

	private int[] idDays = {
			R.id.idDay00, R.id.idDay01, R.id.idDay02, R.id.idDay03, R.id.idDay04, R.id.idDay05, R.id.idDay06,
			R.id.idDay07, R.id.idDay08, R.id.idDay09, R.id.idDay10, R.id.idDay11, R.id.idDay12, R.id.idDay13,
			R.id.idDay14, R.id.idDay15, R.id.idDay16, R.id.idDay17, R.id.idDay18, R.id.idDay19, R.id.idDay20,
			R.id.idDay21, R.id.idDay22, R.id.idDay23, R.id.idDay24, R.id.idDay25, R.id.idDay26, R.id.idDay27,
			R.id.idDay28, R.id.idDay29, R.id.idDay30, R.id.idDay31, R.id.idDay32, R.id.idDay33, R.id.idDay34,
			R.id.idDay35, R.id.idDay36, R.id.idDay37, R.id.idDay38, R.id.idDay39, R.id.idDay40, R.id.idDay41
	};
	
	private void setClock(){
		Date now = new Date();

		time = sdf1.format(now);
		date = sdf2.format(now);

		if(this.now == null)	this.now = Calendar.getInstance();

		this.now.setTime(now);
		this.now.set(Calendar.HOUR, 0);
		this.now.set(Calendar.MINUTE, 0);
		this.now.set(Calendar.MINUTE, 0);
		this.now.set(Calendar.SECOND, 0);
		this.now.set(Calendar.MILLISECOND, 0);
	}

	private void setYear(){
		int value = now.get(Calendar.YEAR);
		year = String.valueOf(value);
		value = value % 100 + 12;
		yearG = "H." + String.valueOf(value);
	}


	private void setDay(){
		Calendar cal = (Calendar) now.clone();
		int m = cal.get(Calendar.MONTH);
		cal.set(Calendar.DATE, 1);
		switch(cal.get(Calendar.DAY_OF_WEEK)){
		case Calendar.SATURDAY:
			cal.add(Calendar.DATE, -1);
		case Calendar.FRIDAY:
			cal.add(Calendar.DATE, -1);
		case Calendar.THURSDAY:
			cal.add(Calendar.DATE, -1);
		case Calendar.WEDNESDAY:
			cal.add(Calendar.DATE, -1);
		case Calendar.TUESDAY:
			cal.add(Calendar.DATE, -1);
		case Calendar.MONDAY:
			cal.add(Calendar.DATE, -1);
		}

		int i=0;

		while(i<days.length){
			if(cal.get(Calendar.MONTH) == m){
				days[i] = String.valueOf(cal.get(Calendar.DATE));
			} else {
				days[i] = "";
			}
			cal.add(Calendar.DATE, 1);
			i++;
		}
//		today = (Calendar) now.clone();
	}
	private void setAlarm(){
		Context context = this;
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, ClockCalendarService.class);
		PendingIntent pi = PendingIntent.getService(this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar cal = Calendar.getInstance();
		int sec = 60 - cal.get(Calendar.SECOND);
		sec = sec > 0 ? sec * 1000 : 60000;

		manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + sec, pi);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int id){
		super.onStartCommand(intent, flags, id);
		
		if(intent.getBooleanExtra(ClockCalendarProvider.yearChange, false)){
			isJapanese = !isJapanese;
		}

		setClock();
		setYear();
		setDay();
		
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.main);

		views.setTextViewText(R.id.idClockTime, time);
		views.setTextViewText(R.id.idClockDate, date);
		if(isJapanese){
			views.setTextViewText(R.id.idCalendarYear, yearG);
		} else {
			views.setTextViewText(R.id.idCalendarYear, year);
		}
		
		int i=0;
		for(int j: idDays){
			views.setTextViewText(j, days[i]);
			if(days[i].equals(String.valueOf(now.get(Calendar.DATE)))){
				views.setInt(j, "setBackgroundColor", Color.argb(0x44, 0x0, 0xff, 0x00));
			} else {
				views.setInt(j, "setBackgroundColor", Color.argb(0x00, 0x0, 0x00, 0x00));
			}
			i++;
		}
		
		ComponentName wedgit = new ComponentName(this, ClockCalendarProvider.class);
		manager.updateAppWidget(wedgit, views);
		
		setAlarm();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}	

