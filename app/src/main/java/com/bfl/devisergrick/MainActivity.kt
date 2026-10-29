package com.bfl.devisergrick

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val OVERLAY_PERMISSION_REQ_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
            gravity = android.view.Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#1a1a2e"))
        }

        val title = android.widget.TextView(this).apply {
            text = "Raising Deviser Grick"
            textSize = 22f
            setTextColor(android.graphics.Color.WHITE)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 0, 0, 40)
        }
        layout.addView(title)

        val btnSummon = Button(this).apply {
            text = "Summon Grick"
            setBackgroundColor(android.graphics.Color.parseColor("#e94560"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                if (checkOverlayPermission()) {
                    startFloatingService()
                }
            }
        }
        layout.addView(btnSummon)

        val btnDismiss = Button(this).apply {
            text = "Dismiss Grick"
            setBackgroundColor(android.graphics.Color.parseColor("#1f4068"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                stopService(Intent(this@MainActivity, GrickFloatingService::class.java))
                Toast.makeText(this@MainActivity, "Grick dismissed", Toast.LENGTH_SHORT).show()
            }
        }
        layout.addView(btnDismiss)

        setContentView(layout)
    }

    private fun checkOverlayPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
                Toast.makeText(this, "Please allow 'Display over other apps'", Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun startFloatingService() {
        val intent = Intent(this, GrickFloatingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Grick Summoned on your screen!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
