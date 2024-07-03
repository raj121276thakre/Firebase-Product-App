package com.example.cosmeticsahyadriapp.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cosmeticsahyadriapp.MainActivity
import com.example.cosmeticsahyadriapp.R
import com.example.cosmeticsahyadriapp.Utils
import com.example.cosmeticsahyadriapp.databinding.ActivityAddFoodBinding
import com.example.cosmeticsahyadriapp.model.Food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFoodBinding

    private lateinit var storage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainAddFood)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setStatusBarColor(this, R.color.yellow, lightStatusBar = true)
        storage = Firebase.storage
        database = Firebase.database.reference.child("foods")

        binding.selectFoodImg.setOnClickListener { selectImage() }
        binding.uploadButton.setOnClickListener { uploadFood() }

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.foodImage.setImageURI(selectedImageUri)
        }
    }


    private fun uploadFood() {
        val foodName = binding.foodName.text.toString().trim()
        val foodDescription = binding.foodDescription.text.toString().trim()
        val foodPrice = binding.foodPrice.text.toString().trim().toDoubleOrNull()

        if (foodName.isEmpty() || foodDescription.isEmpty() || foodPrice == null || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress dialog
        Utils.showDialog(this, "Uploading food...")

        val imageRef = storage.reference.child("food_images/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(selectedImageUri!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            Utils.hideDialog() // Dismiss progress dialog
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Generate sequential ID based on current count of items
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val nextId = snapshot.childrenCount + 1
                        val foodId = nextId.toString()
                        val food =
                            Food(foodId, foodName, foodDescription, foodPrice, downloadUri.toString())
                        database.child(foodId).setValue(food)
                        Toast.makeText(this@AddFoodActivity, "Food uploaded successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@AddFoodActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
            }
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








