package org.tensorflow.lite.examples.poseestimation.database.Correction;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface CorDao {

    @Insert
    void setInsert(Correction DB);

    @Update
    void setUpdate(Correction DB);

    @Delete
    void setDelete(Correction DB);

    @Query("SELECT * FROM Correction") // 조회 쿼리
    List<Correction> getAll();

}
