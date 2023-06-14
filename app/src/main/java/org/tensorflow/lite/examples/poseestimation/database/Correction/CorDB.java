package org.tensorflow.lite.examples.poseestimation.database.Correction;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Correction.class}, version = 1)
public abstract class CorDB extends RoomDatabase {
    public abstract CorDao corDao();
}
