package com.example.cosmeticsahyadriapp.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cosmeticsahyadriapp.R
import com.example.cosmeticsahyadriapp.databinding.ActivityFoodPageBinding
import com.example.cosmeticsahyadriapp.model.Food

class FoodPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setStatusBarColor(this, R.color.yellow, lightStatusBar = true)

        // Get the food item passed through the intent
        val food = intent.getParcelableExtra<Food>("food")
        food?.let {
            binding.foodName.text = it.name
            binding.foodPrice.text = "${it.price}"
            binding.foodDescription.text = it.description
            Glide.with(this).load(it.imageUrl).into(binding.foodImage)
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