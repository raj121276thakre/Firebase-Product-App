package com.example.cosmeticsahyadriapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cosmeticsahyadriapp.MainActivity
import com.example.cosmeticsahyadriapp.R
import com.example.cosmeticsahyadriapp.adapter.FoodAdapter
import com.example.cosmeticsahyadriapp.databinding.ActivityAllFoodsBinding
import com.example.cosmeticsahyadriapp.model.Food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database

import com.google.firebase.ktx.Firebase


class AllFoodsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllFoodsBinding

    private lateinit var database: DatabaseReference
    private lateinit var foodAdapter: FoodAdapter
    private val foodList = mutableListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllFoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setStatusBarColor(this, R.color.yellow, lightStatusBar = true)

        database = Firebase.database.reference.child("foods")

        foodAdapter = FoodAdapter(foodList) { food ->
            val intent = Intent(this, FoodPageActivity::class.java)
            intent.putExtra("food", food)
            startActivity(intent)
        }

        // Set GridLayoutManager with 3 columns
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = foodAdapter

        loadFoods()

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadFoods() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()
                snapshot.children.forEach {
                    val food = it.getValue(Food::class.java)
                    food?.let { foodList.add(it) }
                }
                foodAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AllFoodsActivity, "Failed to load foods", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    // Function to handle delete button click
    fun onDeleteClick(view: View) {
        val recyclerView = binding.recyclerView
        val holder = recyclerView.findContainingViewHolder(view)

        if (holder != null && holder is FoodAdapter.FoodViewHolder) {
            val position = holder.adapterPosition
            val food = foodList[position]

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Delete Food")
                .setMessage("Are you sure you want to delete this food item?")
                .setPositiveButton("Yes") { _, _ ->
                    // Delete food item from Firebase
                    food.id?.let {
                        database.child(it).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Food deleted successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to delete food", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
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



