package com.example.smoothscrollingdemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.smoothscrollingdemo.ui.theme.SmoothScrollingDemoTheme

private const val PERMISSION_REQ_ID = 22
private val REQUESTED_PERMISSIONS = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.CAMERA,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    // Add any other permissions you need here
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmoothScrollingDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID) {
            if (checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) &&
                checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) &&
                checkSelfPermission(this, REQUESTED_PERMISSIONS[2])
            ) {
                val intent = Intent(this, LiveStreamingActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
            } else {
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(), // Fills the parent size
        contentAlignment = Alignment.Center // Content will be aligned to the center
    ) {
        Button(
            onClick = {
                if (checkSelfPermission(context, REQUESTED_PERMISSIONS[0]) &&
                    checkSelfPermission(context, REQUESTED_PERMISSIONS[1]) &&
                    checkSelfPermission(context, REQUESTED_PERMISSIONS[2])
                ) {
                    val intent = Intent(context, LiveStreamingActivity::class.java)
                    context.startActivity(intent)
                    if(context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        context as MainActivity,
                        REQUESTED_PERMISSIONS,
                        PERMISSION_REQ_ID
                    )
                }
            },
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp) // Set left (start) and right (end) padding values
        ) {
            Text("View Live Streams")
        }
    }
}

fun checkSelfPermission(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmoothScrollingDemoTheme {
        Greeting("Android")
    }
}
