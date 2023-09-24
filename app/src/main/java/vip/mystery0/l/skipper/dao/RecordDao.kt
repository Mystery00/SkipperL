package vip.mystery0.l.skipper.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vip.mystery0.l.skipper.model.TriggerRecordTuple
import vip.mystery0.l.skipper.model.db.TriggerRecord

@Dao
interface RecordDao {
    @Insert
    fun insert(triggerRecord: TriggerRecord)

    @Query("SELECT package_name, count(*) AS count FROM trigger_record WHERE time > :time GROUP BY package_name ORDER BY count DESC")
    fun getAll(time: Long): List<TriggerRecordTuple>
}