package vip.mystery0.l.skipper.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vip.mystery0.l.skipper.model.db.CustomRule

@Dao
interface RuleDao {
    @Query("SELECT * FROM custom_rule")
    suspend fun getAll(): List<CustomRule>

    @Insert
    suspend fun insertAll(vararg rules: CustomRule)

    @Query("DELETE FROM custom_rule")
    suspend fun deleteAll()
}