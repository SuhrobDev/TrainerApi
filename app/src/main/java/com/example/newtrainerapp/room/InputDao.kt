package com.example.newtrainerapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newtrainerapp.entity.Trainer

@Dao
interface InputDao {
    @Query("select * from trainer")
    fun getTrainer(): List<Trainer>

    @Insert
    fun addTrainer(trainer: Trainer)

    @Query("update trainer set trainer_name = :name,trainer_surname = :surname,trainer_salary = :salary,type = :type where id = :id")
    fun updateTrainer(name: String, surname: String, salary: Double, type: Int, id: Int)

    @Query("delete from trainer where id = :id")
    fun deleteTrainer(id: Int)

    @Query("select * from trainer where id = :id")
    fun selectFrom(id: Int): Trainer

    @Query("delete from trainer")
    fun deleteAllTrainer()


}