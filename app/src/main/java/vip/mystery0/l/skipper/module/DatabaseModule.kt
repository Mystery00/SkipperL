package vip.mystery0.l.skipper.module

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import vip.mystery0.l.skipper.dao.RecordDao
import vip.mystery0.l.skipper.dao.RuleDao
import vip.mystery0.l.skipper.model.db.CustomRule
import vip.mystery0.l.skipper.model.db.TriggerRecord

@Database(entities = [TriggerRecord::class, CustomRule::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun getRecordDao(): RecordDao

    abstract fun getRuleDao(): RuleDao
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext().applicationContext, DB::class.java, "skipper.db")
            .build()
    }
    single {
        get<DB>().getRecordDao()
    }
    single {
        get<DB>().getRuleDao()
    }
}