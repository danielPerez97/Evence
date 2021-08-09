package daniel.perez.ical

object TestStrings
{
    val christmas1997 = """
        BEGIN:VEVENT
        UID:My_iCal_doperez_2019-11-29104627.2@uark.edu
        DTSTART;TZID=America/Chicago:19971225T060000
        DTEND;TZID=America/Chicago:19971225T080000
        DTSTAMP;TZID=America/Chicago:19971225T055900
        CLASS:PRIVATE
        CREATED:20191129T104627Z
        LAST-MODIFIED:20191129T104627Z
        SEQUENCE:0
        STATUS:CONFIRMED
        SUMMARY:Christmas 1997
        DESCRIPTION:
        LOCATION:
        TRANSP:OPAQUE
        END:VEVENT
    """.trimIndent()

    val christ1997FullICal = """
        BEGIN:VCALENDAR
        VERSION:2.0
        PRODID:-//University of Arkansas
        BEGIN:VTIMEZONE
        TZID:America/Chicago
        X-LIC-LOCATION:America/Chicago
        BEGIN:DAYLIGHT
        TZOFFSETFROM:-0600
        TZOFFSETTO:-0500
        TZNAME:CDT
        DTSTART:19700308T020000
        RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU
        END:DAYLIGHT
        BEGIN:STANDARD
        TZOFFSETFROM:-0500
        TZOFFSETTO:-0600
        TZNAME:CST
        DTSTART:19701101T020000
        RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU
        END:STANDARD
        END:VTIMEZONE
        BEGIN:VEVENT
        UID:My_iCal_doperez_2019-11-29104627.2@uark.edu
        DTSTART;TZID=America/Chicago:19971225T060000
        DTEND;TZID=America/Chicago:19971225T080000
        DTSTAMP;TZID=America/Chicago:19971225T055900
        CLASS:PRIVATE
        CREATED:20191129T104627Z
        LAST-MODIFIED:20191129T104627Z
        SEQUENCE:0
        STATUS:CONFIRMED
        SUMMARY:Christmas 1997
        DESCRIPTION:
        LOCATION:
        TRANSP:OPAQUE
        END:VEVENT
        END:VCALENDAR
        
        
    """.trimIndent()
}