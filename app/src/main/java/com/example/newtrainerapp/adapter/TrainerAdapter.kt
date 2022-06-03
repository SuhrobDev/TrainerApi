package com.example.newtrainerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newtrainerapp.databinding.ItemTrainerBinding
import com.example.newtrainerapp.dto.TrainerDto

class TrainerAdapter : RecyclerView.Adapter<TrainerAdapter.TrainerHolder>() {
    var data = ArrayList<TrainerDto>()

    inner class TrainerHolder(var binding: ItemTrainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TrainerDto) {
            binding.tvName.text = data.trainerName
            binding.tvSurname.text = data.trainerSurname
            binding.tvSalary.text = data.trainerSalary.toString()

            binding.rootLayout.setOnClickListener {
                moreClickListener?.invoke(data, adapterPosition, it!!, data.id)
            }
        }
    }

    private var moreClickListener: ((trainer: TrainerDto, pos: Int, view: View, id: Int) -> Unit)? =
        null

    fun setMoreClickListener(f: (trainer: TrainerDto, pos: Int, view: View, id: Int) -> Unit) {
        moreClickListener = f
    }

    private var deleteClickListener: ((id: Int, position: Int) -> Unit)? = null

    fun setDeleteClickListener(f: (id: Int, position: Int) -> Unit) {
        deleteClickListener = f
    }

    private var editClickListener: ((id: Int, position: Int, data: TrainerDto) -> Unit)? = null

    fun setEditClickListener(f: (id: Int, position: Int, data: TrainerDto) -> Unit) {
        editClickListener = f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrainerHolder(
        ItemTrainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TrainerHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount() = data.size

    fun insertData(trainer: TrainerDto) {
        data.add(trainer)
        notifyItemInserted(data.size - 1)
    }

    fun updateData(trainer: TrainerDto, i: Int) {
        data[i] = trainer
        notifyItemChanged(i)
    }

    fun deleteData(i: Int) {
        data.removeAt(i)
        notifyItemRemoved(i)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData(data: List<TrainerDto>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}