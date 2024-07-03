package com.example.cosmeticsahyadriapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cosmeticsahyadriapp.databinding.ItemFoodBinding
import com.example.cosmeticsahyadriapp.model.Food

class FoodAdapter(private val foodList: List<Food>, private val onClick: (Food) -> Unit) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.foodName.text = food.name
            binding.foodPrice.text = "${food.price}"
            Glide.with(binding.root).load(food.imageUrl).into(binding.foodImage)
            binding.root.setOnClickListener { onClick(food) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int = foodList.size
}
