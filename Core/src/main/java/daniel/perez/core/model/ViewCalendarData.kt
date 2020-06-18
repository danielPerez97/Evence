package daniel.perez.core.model

import daniel.perez.core.model.ViewEvent

data class ViewCalendarData(
        val fileName: String,
        val events: List<ViewEvent>
)