package com.example.newtrainerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newtrainerapp.databinding.ItemTrainerBinding
import com.example.newtrainerapp.entity.Trainer
import com.example.newtrainerapp.retrofit.models.response.TrainerResponse

class TrainerAdapter : RecyclerView.Adapter<TrainerAdapter.TrainerHolder>() {
    var data = ArrayList<Trainer>()

    inner class TrainerHolder(var binding: ItemTrainerBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: Trainer){
            binding.tvName.text = data.name
            binding.tvSurname.text = data.surname
            binding.tvSalary.text = data.salary.toString()

            binding.rootLayout.setOnClickListener {
                moreClickListener?.invoke(data,adapterPosition,it!!,data.trainerId)
            }
        }
    }

    private var moreClickListener: ((trainer: Trainer, pos:Int, view:View, id:Int) -> Unit)? = null

    fun setMoreClickListener(f: (trainer: Trainer, pos:Int, view:View, id:Int) -> Unit) {
        moreClickListener = f
    }

    private var deleteClickListener: ((id:Int, position: Int) -> Unit)? = null

    fun setDeleteClickListener(f: (id:Int, position: Int) -> Unit) {
        deleteClickListener = f
    }

    private var editClickListener: ((id:Int, position: Int, data: TrainerResponse) -> Unit)? = null

    fun setEditClickListener(f: (id:Int, position: Int, data: TrainerResponse) -> Unit) {
        editClickListener = f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrainerHolder(
        ItemTrainerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: TrainerHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size

    fun insertData(trainer: Trainer){
        data.add(trainer)
        notifyItemInserted(data.size-1)
    }

    fun updateData(trainer: Trainer, i: Int){
        data[i] =  trainer
        notifyItemChanged(i)
    }

    fun deleteData(i:Int){
        data.removeAt(i)
        notifyItemRemoved(i)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData(data:List<Trainer>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}