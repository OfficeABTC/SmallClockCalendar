package jp.abtc.simpleclockcalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class ClockCalendarProvider extends AppWidgetProvider {
	final static String yearFilter = "jp.abtc.smallclockcalendar.YEAR_CLICKED";
	final static String yearChange = "changeYear";
	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int[] ids){
		super.onUpdate(context, manager, ids);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
		Intent yearIntent = new Intent(yearFilter);
		PendingIntent pi = PendingIntent.getBroadcast(context,0, yearIntent, 0);
		views.setOnClickPendingIntent(R.id.clock,pi);
		manager.updateAppWidget(ids, views);
		
		
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
			
		} else if(action.equals(yearFilter)){
			Intent i= new Intent(context, ClockCalendarService.class);
			i.putExtra(yearChange, true);
			context.startService(i);
		}
	}
}
