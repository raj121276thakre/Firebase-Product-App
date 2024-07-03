package com.example.cosmeticsahyadriapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cosmeticsahyadriapp.activity.AddFoodActivity
import com.example.cosmeticsahyadriapp.activity.AllFoodsActivity
import com.example.cosmeticsahyadriapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setStatusBarColor(this, R.color.yellow, lightStatusBar = true)

        binding.addProduct.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivity(intent)
        }

        binding.allProducts.setOnClickListener {
            val intent = Intent(this, AllFoodsActivity::class.java)
            startActivity(intent)
        }


    }



    //status bar color
    private fun setStatusBarColor(
        activity: Activity,
        colorResId: Int,
        lightStatusBar: Boolean = false
    ) {
        activity.window?.apply {
            // Set the status bar color
            statusBarColor = ContextCompat.getColor(activity, colorResId)

            // Set the status bar text color mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = if (lightStatusBar) {
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }
}




















