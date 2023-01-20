package com.receipts.ui.activities


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.receipts.R
import com.receipts.models.Repositories
import com.receipts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Repositories.init(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}