package daniel.perez.ical

import daniel.perez.ical.internal.NoTimeZoneException
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
    lateinit var christmas1997EventSpec: EventSpec

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

        christmas1997EventSpec = EventSpec.Builder(2)
                .start(christmas1997Event)
                .end(christmas1997Event.plusHours(2))
                .dtstamp(dtstamp)
                .title("Christmas 1997")
                .build()
    }

    @Test
    fun `tests a two hour event on christmas 1997`()
    {

        val icalSpec = ICalSpec.Builder()
                .addEvent(christmas1997EventSpec)
                .timeZone(TimeZones.AMERICA_CHICAGO)
                .build()

        assertEquals(TestStrings.christ1997FullICal, icalSpec.text())
    }

    @Test
    fun `tests for failure if no time zone is given`()
    {
        assertThrows(NoTimeZoneException::class.java) {
            ICalSpec.Builder()
                    .addEvent(christmas1997EventSpec)
                    .build()
        }
    }
}