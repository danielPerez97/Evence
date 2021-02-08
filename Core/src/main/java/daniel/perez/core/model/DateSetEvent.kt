package daniel.perez.core.model

import java.time.LocalDate

data class DateSetEvent(
        val month: Int,
        val dayOfMonth: Int,
        val year: Int)
{
    fun string(): String
    {
        return LocalDate.of(year, month, dayOfMonth).toString()
    }
}
