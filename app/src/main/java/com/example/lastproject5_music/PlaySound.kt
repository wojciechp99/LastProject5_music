package com.example.lastproject5_music

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class PlaySound : AppWidgetProvider() {
    private val startSound = "PLAY_SOUND"
    private val stopSound = "STOP_SOUND"
    private val nextSound = "NEXT_SOUND"
    private val pauseSound = "PAUSE_SOUND"
    private val resumeSound = "RESUME_SOUND"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent?.action) {
            startSound -> {
                Log.d("PlaySound", "Przycisk Start został kliknięty!")
                MyMedia.createSound(context!!)
                MyMedia.startSound()
            }
            stopSound -> {
                Log.d("StopSound", "Przycisk Stop został kliknięty!")
                MyMedia.stopSound()
            }
            nextSound -> {
                Log.d("NextSound", "Przycisk Next został kliknięty!")
                MyMedia.nextSound(context!!)
                MyMedia.startSound()
            }
            pauseSound -> {
                Log.d("PauseSound", "Przycisk Pause został kliknięty!")
                MyMedia.pauseSound()
            }
            resumeSound -> {
                Log.d("ResumeSound", "Przycisk Resume został kliknięty!")
                MyMedia.resumeSound()
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.play_sound)

    // "Start"
    val startIntent = Intent(context, PlaySound::class.java)
    startIntent.action = "PLAY_SOUND"
    val startPendingIntent = PendingIntent.getBroadcast(context, 0, startIntent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.startButton, startPendingIntent)

    // "Stop"
    val stopIntent = Intent(context, PlaySound::class.java)
    stopIntent.action = "STOP_SOUND"
    val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.stopButton, stopPendingIntent)

    // "Next"
    val nextIntent = Intent(context, PlaySound::class.java)
    nextIntent.action = "NEXT_SOUND"
    val nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.nextButton, nextPendingIntent)

    // "Pause"
    val pauseIntent = Intent(context, PlaySound::class.java)
    pauseIntent.action = "PAUSE_SOUND"
    val pausePendingIntent = PendingIntent.getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.pauseButton, pausePendingIntent)

    // "Resume"
    val resumeIntent = Intent(context, PlaySound::class.java)
    resumeIntent.action = "RESUME_SOUND"
    val resumePendingIntent = PendingIntent.getBroadcast(context, 0, resumeIntent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.resumeButton, resumePendingIntent)


    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class MyMedia {
    companion object {
        private var mp = MediaPlayer()
        private var index = 0
        private val soundResources = intArrayOf(
            R.raw.sound1,
            R.raw.sound2,
            R.raw.sound3,
            R.raw.sound4
        )
        fun createSound(context: Context): MediaPlayer {
            mp = MediaPlayer.create(context, soundResources[index])
            return mp
        }

        fun startSound() {
            if (mp.isPlaying) {
                Log.e("CANNOT START", "IS PLAYING")
            } else {
                mp.start()
            }

        }

        fun pauseSound() {
            mp.pause()
        }

        fun resumeSound(){
            mp.start()
        }

        fun stopSound() {
            if (mp.isPlaying) {
                mp.stop()
                mp.release()
            }
        }

        fun nextSound(context: Context): MediaPlayer {
            if (mp.isPlaying) {
                mp.stop()
                mp.release()
            }
            index+=1
            if (index>3) {
                index = 0
            }
            mp = MediaPlayer.create(context, soundResources[index])
            return mp
        }

    }
}