package com.receipts.ui.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.receipts.R
import com.receipts.models.Repositories
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Repositories.init(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}