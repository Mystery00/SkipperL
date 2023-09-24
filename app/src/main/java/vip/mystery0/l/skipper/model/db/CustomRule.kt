package vip.mystery0.l.skipper.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_rule")
data class CustomRule(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "node_id") val nodeId: String,
    @ColumnInfo(name = "action_id") val actionId: String,
    @ColumnInfo(name = "action_text") val actionText: String,
)
