package org.tensorflow.lite.examples.poseestimation.database.User;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface UserDao {

    @Insert
    void setInsert(User DB);

    @Update
    void setUpdate(User DB);

    @Delete
    void setDelete(User DB);

    @Query("SELECT * FROM User") // 조회 쿼리
    List<User> getAll();

}
