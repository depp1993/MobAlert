package com.deepankarsingh.mobalert;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.deepankarsingh.mobalert.helper.DbConnect;

public class RemoteViewFactory implements RemoteViewsFactory {

	private Context context = null;
	@SuppressWarnings("unused")
	private int appWidgetId;

	DbConnect connect;
	Cursor name;
	Cursor phone;
	int n;
	ArrayList<ListItemWidget> listItemList;

	public RemoteViewFactory(Context context, Intent intent) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		populateListView();

	}

	public void populateListView() {

		listItemList = new ArrayList<ListItemWidget>();
		connect = new DbConnect(context);
		name = connect.getname();
		phone = connect.getphone();
		n = name.getCount();

		name.moveToFirst();
		phone.moveToFirst();
		for (int i = 0; i < n; i++) {
			ListItemWidget listItem = new ListItemWidget();
			listItem.name = name.getString(0);
			name.moveToNext();
			listItem.phone = phone.getString(0);
			phone.moveToNext();
			listItemList.add(listItem);
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataSetChanged() {
		populateListView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItemList.size();
	}

	@Override
	public RemoteViews getViewAt(int position) {

		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.custom_widget_contact_list);
		ListItemWidget listItem = listItemList.get(position);
		remoteView.setTextViewText(R.id.widtcontactName, listItem.name);
		remoteView.setTextViewText(R.id.widtcontactPhone, listItem.phone);
		return remoteView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
