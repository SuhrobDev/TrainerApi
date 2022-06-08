package com.example.newtrainerapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newtrainerapp.controller.Extensions
import com.example.newtrainerapp.dto.TrainerDto
import com.example.newtrainerapp.entity.Trainer
import com.example.newtrainerapp.retrofit.ApiClient
import com.example.newtrainerapp.retrofit.ApiInterface
import com.example.newtrainerapp.retrofit.models.BaseNetworkResult
import com.example.newtrainerapp.retrofit.models.request.LogInRequest
import com.example.newtrainerapp.retrofit.models.request.SignUpRequest
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest
import com.example.newtrainerapp.retrofit.models.response.LogInResponse
import com.example.newtrainerapp.retrofit.models.response.SignUpResponse
import com.example.newtrainerapp.retrofit.models.response.TrainerResponse
import com.example.newtrainerapp.room.AppDatabase
import com.example.newtrainerapp.ui.LoginFragment
import com.example.newtrainerapp.ui.TrainerFragment
import com.example.newtrainerapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRepository {
    private var apiInterface: ApiInterface? = ApiClient.retrofit!!.create(ApiInterface::class.java)
    private val trainerDao = AppDatabase.database?.inputData()!!

    fun getAllTrainer(): LiveData<BaseNetworkResult<List<TrainerDto>>> {
        val listViewModel = MutableLiveData<BaseNetworkResult<List<TrainerDto>>>()

        apiInterface?.getTrainersList()?.enqueue(object : Callback<List<TrainerResponse>> {
            override fun onResponse(
                call: Call<List<TrainerResponse>>,
                response: Response<List<TrainerResponse>>
            ) {
                listViewModel.value = BaseNetworkResult.Loading(false)
                if (response.code() == 200) {
                    trainerDao.deleteAllTrainer()
                    response.body()?.let {
                        it.forEach { trainer ->
                            trainerDao.addTrainer(
                                Trainer(
                                    type = 0,
                                    trainerId = trainer.id,
                                    name = trainer.trainerName,
                                    surname = trainer.trainerSurname,
                                    salary = trainer.trainerSalary
                                )
                            )
                        }
                        listViewModel.value =
                            BaseNetworkResult.Success(trainerDao.getTrainer().map { trainer ->
                                TrainerDto(
                                    trainer.id,
                                    trainer.name,
                                    trainer.salary,
                                    trainer.surname ?: "",
                                    0
                                )
                            })
                    }
                }
            }

            override fun onFailure(call: Call<List<TrainerResponse>>, t: Throwable) {
                listViewModel.value = BaseNetworkResult.Loading(false)
                listViewModel.value = BaseNetworkResult.Error("No internet connection")
                listViewModel.value =
                    BaseNetworkResult.Success(trainerDao.getTrainer().map { trainer ->
                        TrainerDto(
                            trainer.id,
                            trainer.name,
                            trainer.salary,
                            trainer.surname ?: "",
                            0
                        )
                    })
            }
        })
        return listViewModel
    }

    fun insert(
        trainerRequest: TrainerRequest,
        id: Int
    ): MutableLiveData<BaseNetworkResult<TrainerDto>> {
        val liveData = MutableLiveData<BaseNetworkResult<TrainerDto>>()
        liveData.value = BaseNetworkResult.Loading(true)

        apiInterface?.addTrainer(trainerRequest)?.enqueue(object : Callback<TrainerResponse> {
            override fun onResponse(
                call: Call<TrainerResponse>,
                response: Response<TrainerResponse>
            ) {
                liveData.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful) {
                    response.body()?.let { response ->
                        trainerDao.addTrainer(
                            Trainer(
                                name = response.trainerName,
                                salary = response.trainerSalary,
                                surname = response.trainerSurname,
                                trainerId = response.id,
                                type = 0
                            )
                        )
                        liveData.value = BaseNetworkResult.Success(
                            TrainerDto(
                                trainerName = response.trainerName,
                                trainerSalary = response.trainerSalary,
                                trainerSurname = response.trainerSurname,
                                id = response.id,
                                type = 0
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<TrainerResponse>, t: Throwable) {
                liveData.value = BaseNetworkResult.Loading(false)
                liveData.value = BaseNetworkResult.Error("${t.message}")
                trainerDao.addTrainer(
                    Trainer(
                        name = trainerRequest.trainerName,
                        salary = trainerRequest.trainerSalary,
                        surname = trainerRequest.trainerSurname,
                        trainerId = id,
                        type = 1
                    )
                )
            }
        })
        return liveData
    }

    fun update(
        trainerRequest: TrainerRequest,
        id: Int,
        roomId: Int
    ): MutableLiveData<BaseNetworkResult<Trainer>> {
        val liveData = MutableLiveData<BaseNetworkResult<Trainer>>()
        liveData.value = BaseNetworkResult.Loading(true)

        apiInterface?.editTrainer(trainerRequest, id)?.enqueue(object : Callback<TrainerResponse> {
            override fun onResponse(
                call: Call<TrainerResponse>,
                response: Response<TrainerResponse>
            ) {
                liveData.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        trainerDao.updateTrainer(
                            trainerRequest.trainerName,
                            trainerRequest.trainerSurname,
                            trainerRequest.trainerSalary,
                            0,
                            roomId
                        )
                        liveData.value = BaseNetworkResult.Success(
                            Trainer(
                                name = it.trainerName,
                                salary = it.trainerSalary,
                                surname = it.trainerSurname,
                                trainerId = it.id,
                                type = 0
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<TrainerResponse>, t: Throwable) {
                liveData.value = BaseNetworkResult.Loading(false)
                liveData.value = BaseNetworkResult.Error("${t.message}")
                trainerDao.updateTrainer(
                    trainerRequest.trainerName,
                    trainerRequest.trainerSurname,
                    trainerRequest.trainerSalary,
                    2,
                    roomId
                )
            }
        })
        return liveData
    }

    fun delete(trainer: TrainerDto): MutableLiveData<BaseNetworkResult<TrainerDto>> {
        val liveData = MutableLiveData<BaseNetworkResult<TrainerDto>>()
        liveData.value = BaseNetworkResult.Loading(true)

        apiInterface?.deleteTrainer(trainer.id)?.enqueue(object : Callback<TrainerResponse> {
            override fun onResponse(
                call: Call<TrainerResponse>,
                response: Response<TrainerResponse>
            ) {
                liveData.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        trainerDao.updateTrainer(
                            trainer.trainerName,
                            trainer.trainerSurname,
                            trainer.trainerSalary,
                            3,
                            trainer.id
                        )
                        trainerDao.deleteTrainer(trainer.id)
                        liveData.value = BaseNetworkResult.Error(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<TrainerResponse>, t: Throwable) {
                liveData.value = BaseNetworkResult.Loading(false)
                liveData.value = BaseNetworkResult.Error("${t.message}")
                trainerDao.updateTrainer(
                    trainer.trainerName,
                    trainer.trainerSurname,
                    trainer.trainerSalary,
                    3,
                    trainer.id
                )
                trainerDao.deleteTrainer(trainer.id)
            }
        })
        return liveData
    }

    fun singUp(
        signUpRequest: SignUpRequest,
        context: Context,
        logInRequest: LogInRequest
    ): MutableLiveData<BaseNetworkResult<SignUpResponse>> {
        val liveData = MutableLiveData<BaseNetworkResult<SignUpResponse>>()
        val sharedPref = SharedPref(context)

        apiInterface?.signUp(signUpRequest)?.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if (response.code()==200) {
                    response.body()?.let {
                        liveData.value = BaseNetworkResult.Success(it)
                        logIn(logInRequest, context)
                        Extensions.controller?.startMainFragment(LoginFragment())
                    }
                }else{
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {

            }
        })
        return liveData
    }

    fun logIn(
        logInRequest: LogInRequest,
        context: Context
    ): MutableLiveData<BaseNetworkResult<LogInResponse>> {
        val liveData = MutableLiveData<BaseNetworkResult<LogInResponse>>()
        val sharedPref = SharedPref(context)
        apiInterface?.logIn(logInRequest)
            ?.enqueue(object : Callback<LogInResponse> {
                override fun onResponse(
                    call: Call<LogInResponse>,
                    response: Response<LogInResponse>
                ) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            sharedPref.setToken(it.accessToken)
                            Log.d("TTTT", "language: ${sharedPref.getToken()}")
                            liveData.value = BaseNetworkResult.Success(it)
                        }
                        Extensions.controller?.startMainFragment(TrainerFragment())
                    }else if (response.code()==401){
                        Toast.makeText(context, "user is not recognized", Toast.LENGTH_SHORT).show()
                    }else if (response.code()==400){
                        Toast.makeText(context, "bad request", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                    Toast.makeText(context, "error occurred!", Toast.LENGTH_SHORT).show()
                }
            })
        return liveData
    }
}