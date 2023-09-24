package vip.mystery0.l.skipper.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trigger_record")
data class TriggerRecord(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "button_text") val buttonText: String,
    @ColumnInfo(name = "time") val time: Long = System.currentTimeMillis(),
)
