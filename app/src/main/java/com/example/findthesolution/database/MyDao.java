package com.example.findthesolution.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.findthesolution.database.Users;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    public void addUser(Users users);

    @Query("select * from Users")
    public List<Users> getUsers();

    @Update
    public void updateUser(Users users);
}
