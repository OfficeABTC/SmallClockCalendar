package jp.abtc.simpleclockcalendar;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;


public class ClockCalendarProvider extends AppWidgetProvider {
	final static String yearFilter = "jp.abtc.smallclockcalendar.YEAR_CLICKED";
	final static String yearChange = "changeYear";
	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int[] ids){
		super.onUpdate(context, manager, ids);
		Intent intent = new Intent(context, ClockCalendarService.class);
		intent.putExtra(yearChange, false);
		context.startService(intent);
	}
	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);

		String action = intent.getAction();
		if( action.equals(Intent.ACTION_DATE_CHANGED) ||
			action.equals(Intent.ACTION_TIME_CHANGED) ||
			action.equals(Intent.ACTION_TIMEZONE_CHANGED)){
			Intent i= new Intent(context, ClockCalendarService.class);
			i.putExtra(yearChange, false);
			context.startService(i);
		}
	}
}
