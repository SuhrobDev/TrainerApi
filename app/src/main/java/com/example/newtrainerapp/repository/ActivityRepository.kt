package com.example.newtrainerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newtrainerapp.entity.Trainer
import com.example.newtrainerapp.room.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.newtrainerapp.retrofit.ApiClient
import com.example.newtrainerapp.retrofit.ApiInterface
import com.example.newtrainerapp.retrofit.models.BaseNetworkResult
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest
import com.example.newtrainerapp.retrofit.models.response.TrainerResponse

class ActivityRepository {
    private var apiInterface: ApiInterface? = ApiClient.retrofit!!.create(ApiInterface::class.java)
    private val trainerDao = AppDatabase.database?.inputData()!!

    fun getAllTrainer(): LiveData<BaseNetworkResult<List<Trainer>>> {
        val listViewModel = MutableLiveData<BaseNetworkResult<List<Trainer>>>()
        listViewModel.value = BaseNetworkResult.Loading(true)

        apiInterface?.getTrainersList()?.enqueue(object : Callback<List<TrainerResponse>> {
            override fun onResponse(
                call: Call<List<TrainerResponse>>,
                response: Response<List<TrainerResponse>>
            ) {
                listViewModel.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.forEach { trainer->
                            trainerDao.addTrainer(Trainer(type = 0, trainerId = trainer.id, name = trainer.trainerName, surname = trainer.trainerSurname, salary = trainer.trainerSalary))
                        }
                        listViewModel.value = BaseNetworkResult.Success(trainerDao.getTrainer())
                    }
                }
            }

            override fun onFailure(call: Call<List<TrainerResponse>>, t: Throwable) {
                listViewModel.value = BaseNetworkResult.Loading(false)
                listViewModel.value = BaseNetworkResult.Error("No internet connection")
                listViewModel.value = BaseNetworkResult.Success(trainerDao.getTrainer())
            }
        })
        return listViewModel
    }

    fun insert(trainerRequest: TrainerRequest, id: Int): MutableLiveData<BaseNetworkResult<Trainer>> {
        val liveData = MutableLiveData<BaseNetworkResult<Trainer>>()
        liveData.value = BaseNetworkResult.Loading(true)

        apiInterface?.addTrainer(trainerRequest)?.enqueue(object : Callback<TrainerResponse>{
            override fun onResponse(
                call: Call<TrainerResponse>,
                response: Response<TrainerResponse>
            ) {
                liveData.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful){
                    response.body()?.let {
                        trainerDao.addTrainer(Trainer(name = it.trainerName, salary = it.trainerSalary, surname = it.trainerSurname, trainerId = it.id, type = 0))
                        liveData.value = BaseNetworkResult.Success(Trainer(name = it.trainerName, salary = it.trainerSalary, surname = it.trainerSurname, trainerId = it.id, type = 0))
                    }
                }
            }

            override fun onFailure(call: Call<TrainerResponse>, t: Throwable) {
                liveData.value = BaseNetworkResult.Loading(false)
                liveData.value = BaseNetworkResult.Error("${t.message}")
                trainerDao.addTrainer(Trainer(name = trainerRequest.trainerName, salary = trainerRequest.trainerSalary, surname = trainerRequest.trainerSurname, trainerId = id, type = 1))
            }
        })
        return liveData
    }

    fun update(trainerRequest: TrainerRequest, id:Int, roomId:Int): MutableLiveData<BaseNetworkResult<Trainer>> {
        val liveData = MutableLiveData<BaseNetworkResult<Trainer>>()
        liveData.value = BaseNetworkResult.Loading(true)

        apiInterface?.editTrainer(trainerRequest,id)?.enqueue(object : Callback<TrainerResponse>{
            override fun onResponse(
                call: Call<TrainerResponse>,
                response: Response<TrainerResponse>
            ) {
                liveData.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful){
                    response.body()?.let {
                        trainerDao.updateTrainer(trainerRequest.trainerName,trainerRequest.trainerSurname,trainerRequest.trainerSalary,0,roomId)
                        liveData.value = BaseNetworkResult.Success(Trainer(name = it.trainerName, salary = it.trainerSalary, surname = it.trainerSurname, trainerId = it.id, type = 0))
                    }
                }
            }

            override fun onFailure(call: Call<TrainerResponse>, t: Throwable) {
                liveData.value = BaseNetworkResult.Loading(false)
                liveData.value = BaseNetworkResult.Error("${t.message}")
                trainerDao.updateTrainer(trainerRequest.trainerName,trainerRequest.trainerSurname,trainerRequest.trainerSalary,2,roomId)
            }
        })
        return liveData
    }

    fun delete(trainer: Trainer): MutableLiveData<BaseNetworkResult<Trainer>> {
        val liveData = MutableLiveData<BaseNetworkResult<Trainer>>()
        liveData.value = BaseNetworkResult.Loading(true)

        apiInterface?.deleteTrainer(trainer.trainerId)?.enqueue(object : Callback<TrainerResponse>{
            override fun onResponse(
                call: Call<TrainerResponse>,
                response: Response<TrainerResponse>
            ) {
                liveData.value = BaseNetworkResult.Loading(false)
                if (response.isSuccessful){
                    response.body()?.let {
                        trainerDao.deleteTrainer(trainer.id)
                        liveData.value = BaseNetworkResult.Error(response.message())
                    }
                }
            }
            override fun onFailure(call: Call<TrainerResponse>, t: Throwable) {
                liveData.value = BaseNetworkResult.Loading(false)
                liveData.value = BaseNetworkResult.Error("${t.message}")
                trainerDao.updateTrainer(trainer.name,trainer.surname,trainer.salary,3,trainer.id)
            }
        })
        return liveData
    }
}