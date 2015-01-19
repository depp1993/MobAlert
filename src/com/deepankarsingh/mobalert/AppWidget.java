package com.deepankarsingh.mobalert;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AppWidget extends AppWidgetProvider {

	public static String UPDATE_LIST = "UPDATE_LIST";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		if (intent.getAction().equalsIgnoreCase(UPDATE_LIST)) {
			updateWidget(context);
		}
	}

	private void updateWidget(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int appWidgetIds[] = appWidgetManager
				.getAppWidgetIds(new ComponentName(context, AppWidget.class));
		appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
				R.id.widget_contacts_info);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		int length = appWidgetIds.length;
		for (int i = 0; i < length; i++) {

			@SuppressWarnings("unused")
			int appWidgetId = appWidgetIds[i];
			RemoteViews remoteViews = updateWidgetListView(context,
					appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@SuppressWarnings("deprecation")
	private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		Intent clickIntent = new Intent(context, AppWidget.class);
		clickIntent.setAction(UPDATE_LIST);
		PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(
				context, 0, clickIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.sync_button,
				pendingIntentRefresh);

		Intent callintent = new Intent(context, CallFromWidget.class);
		PendingIntent pendingIntentCall = PendingIntent.getActivity(context, 0,
				callintent, 0);
		remoteViews.setOnClickPendingIntent(R.id.call, pendingIntentCall);

		Intent alertIntent = new Intent(context, AlertFromWidget.class);
		PendingIntent pendingIntentAlert = PendingIntent.getService(context, 0,
				alertIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.alert, pendingIntentAlert);

		Intent listIntent = new Intent(context, RemoteListView.class);
		listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		listIntent
				.setData(Uri.parse(listIntent.toUri(Intent.URI_INTENT_SCHEME)));

		remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_contacts_info,
				listIntent);
		remoteViews.setEmptyView(R.id.widget_contacts_info, R.id.empty_view);
		return remoteViews;
	}

	// public static PendingIntent buildButtonPendingIntent(Context context) {
	//
	// Intent intent = new Intent();
	// intent.setAction(AppWidgetUtility.WIDGET_UPDATE_ACTION);
	// return PendingIntent.getBroadcast(context, 0, intent,
	// PendingIntent.FLAG_UPDATE_CURRENT);
	// }
	//
	// public static void pushWidgetUpdate(Context context, RemoteViews
	// remoteViews) {
	// ComponentName myWidget = new ComponentName(context, AppWidget.class);
	// AppWidgetManager manager = AppWidgetManager.getInstance(context);
	// manager.updateAppWidget(myWidget, remoteViews);
	// }

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "Thank You for using MobAlert Widget",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

}
