package com.example.newtrainerapp.mvvm

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newtrainerapp.entity.Trainer
import com.example.newtrainerapp.room.AppDatabase
import com.example.newtrainerapp.repository.ActivityRepository
import com.example.newtrainerapp.retrofit.models.BaseNetworkResult
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest

class ActivityViewModel : ViewModel() {
    private val repository = ActivityRepository()
    private val _trainerListViewModel = MutableLiveData<List<Trainer>?>()
    val trainerListViewModel: MutableLiveData<List<Trainer>?> get() = _trainerListViewModel
    var trainerDao = AppDatabase.database?.inputData()

    private val _loadingViewModel = MutableLiveData<Boolean?>()
    val loadingViewModel: MutableLiveData<Boolean?> get() = _loadingViewModel

    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel: MutableLiveData<String?> get() = _errorViewModel

    fun getAllTrainer(context: Context) {
        repository.getAllTrainer().observeForever {
            var list = ArrayList<Trainer>()
            when (it) {
                is BaseNetworkResult.Success -> {
                    it.data?.forEach {trainer ->
                        when(trainer.type){
                            1->{
                                Toast.makeText(context, trainer.type.toString(), Toast.LENGTH_SHORT).show()
                                insertTrainer(TrainerRequest(trainer.name,trainer.salary,trainer.surname),trainer.trainerId)
//                                trainerDao?.updateTrainer(type = 0, name = trainer.name, surname = trainer.surname, salary = trainer.salary, id = trainer.id)
                                trainerDao?.deleteTrainer(trainer.id)
                            }
                            2->{
                                Toast.makeText(context, trainer.type.toString(), Toast.LENGTH_SHORT).show()
                                updateTrainer(TrainerRequest(trainer.name,trainer.salary,trainer.surname),trainer.trainerId,trainer.id)
                                trainerDao?.updateTrainer(type = 0, name = trainer.name, surname = trainer.surname, salary = trainer.salary, id = trainer.id)
                            }
                            3->{
                                Toast.makeText(context, trainer.type.toString(), Toast.LENGTH_SHORT).show()
                                deleteTrainer(trainer)
                                trainerDao?.deleteTrainer(trainer.id)
                            }
                        }
                        _trainerListViewModel.value = it.data
                    }
                }
                is BaseNetworkResult.Error -> {
                    _errorViewModel.value = it.message
                }
                is BaseNetworkResult.Loading -> {
                    _loadingViewModel.value = it.isLoading
                }
            }
        }
    }

    private val _insertTrainer = MutableLiveData<Trainer>()
    val insertTrainer: LiveData<Trainer> get() = _insertTrainer

    fun insertTrainer(trainerRequest: TrainerRequest, id: Int) {
        repository.insert(trainerRequest,id).observeForever {
            when (it) {
                is BaseNetworkResult.Success -> {
                    _insertTrainer.value = it.data!!
                }
                is BaseNetworkResult.Error -> {
                    _errorViewModel.value = it.message
                }
                is BaseNetworkResult.Loading -> {
                    _loadingViewModel.value = it.isLoading
                }
            }
        }
    }

    private val _updateTrainer = MutableLiveData<Trainer>()
    val update: LiveData<Trainer> get() = _updateTrainer

    fun updateTrainer(trainerRequest: TrainerRequest, id:Int, roomId: Int){
        repository.update(trainerRequest,id,roomId).observeForever{
            when (it) {
                is BaseNetworkResult.Success -> {
                    _updateTrainer.value = it.data!!
                }
                is BaseNetworkResult.Error -> {
                    _errorViewModel.value = it.message
                }
                is BaseNetworkResult.Loading -> {
                    _loadingViewModel.value = it.isLoading
                }
            }
        }
    }

    private val _deleteTrainer = MutableLiveData<Trainer>()
    val delete: LiveData<Trainer> get() = _deleteTrainer

    fun deleteTrainer(trainer: Trainer){
        repository.delete(trainer).observeForever{
            when (it) {
                is BaseNetworkResult.Success -> {
                    _deleteTrainer.value = it.data!!
                }
                is BaseNetworkResult.Error -> {
                    _errorViewModel.value = it.message
                }
                is BaseNetworkResult.Loading -> {
                    _loadingViewModel.value = it.isLoading
                }
            }
        }
    }
}