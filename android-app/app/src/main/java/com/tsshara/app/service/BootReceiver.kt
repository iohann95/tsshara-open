package com.tsshara.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tsshara.app.data.PrefsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Receives BOOT_COMPLETED and restarts UPS monitoring if it was enabled.
 *
 * Uses [goAsync] instead of runBlocking: receivers run on the main thread,
 * and blocking it while DataStore loads risks an ANR.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        Log.i("BootReceiver", "Device booted, checking monitoring settings")
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val settings = PrefsManager(context).settingsFlow.first()
                if (settings.monitoringEnabled && settings.hasConfiguredDevices) {
                    UpsMonitorService.start(context)
                    Log.i("BootReceiver", "Started UPS monitoring service after boot")
                }
            } catch (e: Exception) {
                Log.e("BootReceiver", "Error re-starting monitoring after boot", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
