package com.example.newtrainerapp.ui

import android.app.AlertDialog
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtrainerapp.R
import com.example.newtrainerapp.adapter.TrainerAdapter
import com.example.newtrainerapp.controller.Extensions
import com.example.newtrainerapp.databinding.FragmentTrainerBinding
import com.example.newtrainerapp.dialog.AddStudentDialog
import com.example.newtrainerapp.dialog.EditStudentDialog
import com.example.newtrainerapp.dto.TrainerDto
import com.example.newtrainerapp.mvvm.ActivityViewModel
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest
import com.example.newtrainerapp.utils.SharedPref
import okhttp3.internal.notify

class TrainerFragment : BaseFragment<FragmentTrainerBinding>(FragmentTrainerBinding::inflate) {
    private lateinit var viewModel: ActivityViewModel

    private val sharedPref by lazy {
        SharedPref(requireContext())
    }

    private val adapter by lazy {
        TrainerAdapter()
    }

    override fun onViewCreated() {
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter

        binding.apply {
            list.layoutManager = LinearLayoutManager(requireContext())
            list.adapter = adapter
        }
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]

        viewModel.trainerListViewModel.observe(requireActivity()) {
            adapter.getData(it!!)
        }

        viewModel.loadingViewModel.observe(requireActivity()) {
            binding.swipe.isRefreshing = it!!
        }

        viewModel.errorViewModel.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.swipe.setOnRefreshListener {
            viewModel.getAllTrainer()
        }

        viewModel.getAllTrainer()

        binding.add.setOnClickListener {
            val addDialog = AddStudentDialog(requireContext())
            var id = 0
            addDialog.setOnAddListener { name, surname, salary ->
                viewModel.insertTrainer.observe(requireActivity()) {
                    adapter.insertData(it!!)
                    id = it.id
                }
                viewModel.insertTrainer(TrainerRequest(name, salary.toDouble(), surname), id)
            }
            addDialog.show()
        }

        binding.logOut.setOnClickListener {
            Extensions.controller?.startMainFragment(LoginFragment())
            sharedPref.setToken("")
        }
        adapter.setMoreClickListener { trainer, pos, view, id ->
            val menu = PopupMenu(requireContext(), view)
            Toast.makeText(requireContext(), "$id", Toast.LENGTH_SHORT).show()
            menu.inflate(R.menu.pop_up_menu)
            menu.setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.edit -> {
                        val edit = EditStudentDialog(
                            requireContext(),
                            trainer.trainerName,
                            trainer.trainerSurname,
                            trainer.trainerSalary.toString()
                        )
                        edit.setOnAddListener { name, surname, salary ->

                            viewModel.update.observe(requireActivity()) { trainer ->
                                adapter.updateData(trainer, pos)
                            }

                            Toast.makeText(requireContext(), "$id", Toast.LENGTH_SHORT).show()
                            viewModel.updateTrainer(
                                TrainerRequest(
                                    name,
                                    salary.toDouble(),
                                    surname
                                ), id, trainer.id
                            )
                        }
                        edit.show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete -> {
                        deleteDialog(requireContext(), pos, trainer)
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
            menu.show()
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(binding.list)
    }

    private var simpleCallBack =

        object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return true // true if moved, false otherwise
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.layoutPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        //delete
                        deleteDialog(
                            requireContext(),
                            pos,/*viewModel.getAllTrainer(requireContext())*/
                            adapter.data[pos]
                        )
                    }
                    ItemTouchHelper.RIGHT -> {
                        //edit
                        val edit = EditStudentDialog(
                            requireContext(),
                            adapter.data[pos].trainerName, adapter.data[pos].trainerSurname,
                            adapter.data[pos].trainerSalary.toString()
                        )
                        edit.setOnAddListener { name, surname, salary ->

                            viewModel.update.observe(requireActivity()) { trainer ->
                                adapter.updateData(trainer, pos)
                            }

                            viewModel.updateTrainer(
                                TrainerRequest(
                                    name,
                                    salary.toDouble(),
                                    surname
                                ), id, adapter.data[pos].id
                            )
                            Toast.makeText(requireContext(), "edited!", Toast.LENGTH_SHORT).show()

                            adapter.notifyItemInserted(pos)
                        }
                        edit.show()
                    }
                }
            }
        }

    private fun deleteDialog(context: Context, position: Int, trainer: TrainerDto) {
        val delete = AlertDialog.Builder(context)
        delete.setTitle("Confirm delete")
        delete.setMessage("You want delete student")
        delete.setPositiveButton("Yes") { p0, _ ->
            viewModel.deleteTrainer(trainer)
            adapter.deleteData(position)
//            adapter.notifyItemRemoved(position)
            Toast.makeText(requireContext(), "deleted!", Toast.LENGTH_SHORT).show()
            p0?.dismiss()
        }
        delete.setNegativeButton(
            "No"
        ) { p0, _ ->
            adapter.notify()
            p0?.dismiss()
        }
        delete.show()
    }

//    override fun onCreateOptionsMenu(menu: Menu?,inflate: MenuInflater) {
//        inflate.inflate(R.menu.menu, menu)
//        return super.onCreateOptionsMenu(menu!!,inflate)
//    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            //logic for logout
            Extensions.controller?.startMainFragment(LoginFragment())
            return true
        }
        return true
    }
}
