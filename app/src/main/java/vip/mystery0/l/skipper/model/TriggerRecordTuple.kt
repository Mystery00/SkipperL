package vip.mystery0.l.skipper.model

import androidx.room.ColumnInfo

data class TriggerRecordTuple(
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "count") val count: Int,
)
