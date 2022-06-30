package teamevence.evenceapp.core.model

import java.time.LocalTime

data class TimeSetEvent(val hour: Int,
                        val minute: Int,
                        val half: Half)
{
    fun string(): String
    {
        return LocalTime.of(hour, minute).toString()
    }
}

enum class Half { AM, PM }