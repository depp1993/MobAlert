package com.deepankarsingh.mobalert;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class RemoteListView extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		@SuppressWarnings("unused")
		int appWidgetId = intent.getIntExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		return (new RemoteViewFactory(this.getApplicationContext(), intent));
	}
}
