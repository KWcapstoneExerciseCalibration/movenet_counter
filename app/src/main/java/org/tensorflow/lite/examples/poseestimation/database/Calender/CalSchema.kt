
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_calender")
data class CalSchema(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val date: Int,
    val note: String
)