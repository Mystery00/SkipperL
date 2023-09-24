package vip.mystery0.l.skipper.store

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import vip.mystery0.l.skipper.dao.RecordDao
import vip.mystery0.l.skipper.model.db.TriggerRecord

object RecordStore : KoinComponent {
    private val recordDao: RecordDao by inject()

    fun saveRecord(packageName: CharSequence, buttonText: CharSequence) =
        recordDao.insert(
            TriggerRecord(
                packageName = packageName.toString(),
                buttonText = buttonText.toString(),
            )
        )
}