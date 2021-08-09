package daniel.perez.ical

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestICalSpec
{
    lateinit var christmas1997Event: ZonedDateTime

    @BeforeAll
    fun setup()
    {
        christmas1997Event = ZonedDateTime.of(
                1997,
                Month.DECEMBER.value,
                25,
                6,
                0,
                0,
                0,
                ZoneId.of("America/Chicago")
        )
    }

    @Test
    fun `tests a two hour event on christmas 1997`()
    {
        val dtstamp = ZonedDateTime.of(
                1997,
                Month.DECEMBER.value,
                25,
                5,
                59,
                0,
                0,
                ZoneId.of("America/Chicago")
        )

        val eventSpec = EventSpec.Builder(2)
                .start(christmas1997Event)
                .end(christmas1997Event.plusHours(2))
                .dtstamp(dtstamp)
                .title("Christmas 1997")
                .build()

        val icalSpec = ICalSpec.Builder()
                .addEvent(eventSpec)
                .timeZone(TimeZones.AMERICA_CHICAGO)
                .build()

        assertEquals(TestStrings.christ1997FullICal, icalSpec.text())
    }
}