package com.example.newtrainerapp.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newtrainerapp.dto.TrainerDto
import com.example.newtrainerapp.repository.ActivityRepository
import com.example.newtrainerapp.retrofit.models.BaseNetworkResult
import com.example.newtrainerapp.retrofit.models.request.LogInRequest
import com.example.newtrainerapp.retrofit.models.request.SignUpRequest
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest
import com.example.newtrainerapp.retrofit.models.response.LogInResponse
import com.example.newtrainerapp.retrofit.models.response.SignUpResponse
import com.example.newtrainerapp.room.AppDatabase

class ActivityViewModel : ViewModel() {
    private val repository = ActivityRepository()
    private val _trainerListViewModel = MutableLiveData<List<TrainerDto>?>()
    val trainerListViewModel: MutableLiveData<List<TrainerDto>?> get() = _trainerListViewModel
    var trainerDao = AppDatabase.database?.inputData()

    private val _loadingViewModel = MutableLiveData<Boolean?>()
    val loadingViewModel: MutableLiveData<Boolean?> get() = _loadingViewModel

    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel: MutableLiveData<String?> get() = _errorViewModel

    fun getAllTrainer() {
        repository.getAllTrainer().observeForever {
            var list = ArrayList<TrainerDto>()
            when (it) {
                is BaseNetworkResult.Success -> {
                    it.data?.forEach { trainer ->

                        when (trainer.type) {
                            1 -> {
//                                Toast.makeText(context, trainer.type.toString(), Toast.LENGTH_SHORT).show()
                                insertTrainer(
                                    TrainerRequest(
                                        trainer.trainerName,
                                        trainer.trainerSalary,
                                        trainer.trainerSurname
                                    ), trainer.id
                                )
//                                trainerDao?.updateTrainer(type = 0, name = trainer.name, surname = trainer.surname, salary = trainer.salary, id = trainer.id)
                                trainerDao?.deleteTrainer(trainer.id)
                            }
//                            2 -> {
////                                Toast.makeText(context, trainer.type.toString(), Toast.LENGTH_SHORT).show()
//                                updateTrainer(
//                                    TrainerRequest(
//                                        trainer.trainerName,
//                                        trainer.trainerSalary,
//                                        trainer.trainerSurname
//                                    ), trainer.id, trainer.
//                                )
//                                trainerDao?.updateTrainer(
//                                    type = 2,
//                                    name = trainer.name,
//                                    surname = trainer.surname,
//                                    salary = trainer.salary,
//                                    id = trainer.id
//                                )
//                            }
//                            3 -> {
////                                Toast.makeText(context, trainer.type.toString(), Toast.LENGTH_SHORT).show()
//                                deleteTrainer(trainer)
//                                trainerDao?.deleteTrainer(trainer.id)
//                            }
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

    private val _insertTrainer = MutableLiveData<TrainerDto>()
    val insertTrainer: LiveData<TrainerDto> get() = _insertTrainer

    fun insertTrainer(trainerRequest: TrainerRequest, id: Int) {
        repository.insert(trainerRequest, id).observeForever {
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

    private val _updateTrainer = MutableLiveData<TrainerDto>()
    val update: LiveData<TrainerDto> get() = _updateTrainer

    fun updateTrainer(trainerRequest: TrainerRequest, id: Int, roomId: Int) {
        repository.update(trainerRequest, id, roomId).observeForever {
            when (it) {
                is BaseNetworkResult.Success -> {
                    _updateTrainer.value = TrainerDto(
                        it.data!!.id,
                        it.data.name, it.data.salary, it.data.surname, it.data.type
                    )
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

    private val _deleteTrainer = MutableLiveData<TrainerDto>()
    val delete: LiveData<TrainerDto> get() = _deleteTrainer

    fun deleteTrainer(trainer: TrainerDto) {
        repository.delete(trainer).observeForever {
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

    private val _logIn = MutableLiveData<LogInResponse>()
    val logIn: LiveData<LogInResponse> get() = _logIn

    fun logIn(logInRequest: LogInRequest, context: Context) {
        repository.logIn(logInRequest, context).observeForever {
            when (it) {
                is BaseNetworkResult.Success -> {
                    _logIn.value = it.data!!
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

    private val _signUp = MutableLiveData<SignUpResponse>()
    val signUp: LiveData<SignUpResponse> get() = _signUp


    fun signUp(signUpRequest: SignUpRequest, logInRequest: LogInRequest, context: Context) {
        repository.singUp(signUpRequest, context, logInRequest).observeForever {
            when (it) {
                is BaseNetworkResult.Success -> {
                    _signUp.value = it.data!!
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