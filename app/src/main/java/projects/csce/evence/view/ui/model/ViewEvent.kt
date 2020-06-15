package projects.csce.evence.view.ui.model

data class ViewEvent(
        val title: String,
        val description: String,
        val startDate: String,
        val startTime: String,
        val startInstantEpoch: Long,
        val endEpochMilli: Long,
        val location: String,
        val iCalText: String
)