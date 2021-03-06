package com.bnp.allmanager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class AllManagerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        /*int[] appWidgetIds holds ids of multiple instance
         * of your widget
         * meaning you are placing more than one widgets on
         * your homescreen*/
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

private RemoteViews updateWidgetListView(Context context, int appWidgetId){
    //which layout to show on widget
    RemoteViews remoteViews = new RemoteViews(
            context.getPackageName(),R.layout.all_manager_widget);
    //RemoteViews Service needed to provide adapter for ListView
    Intent svcIntent = new Intent(context, WidgetService.class);
    //passing app widget id to that RemoteViews Service
    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    //setting a unique Uri to the intent
    //don't know its purpose to me right now
    svcIntent.setData(Uri.parse(
            svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
    //setting adapter to listview of the widget
    remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
            svcIntent);
    //setting an empty view in case of no data
    remoteViews.setEmptyView(R.id.listViewWidget, R.id.appwidget_text);
    return remoteViews;
}

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_empty);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.all_manager_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


