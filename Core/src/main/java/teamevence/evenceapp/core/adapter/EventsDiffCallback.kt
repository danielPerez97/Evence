package teamevence.evenceapp.core.adapter

import androidx.recyclerview.widget.DiffUtil
import teamevence.evenceapp.core.model.ViewEvent

class EventsDiffCallback(
        var oldEvents: List<ViewEvent>,
        var newEvents: List<ViewEvent>
): DiffUtil.Callback()
{
    override fun getOldListSize(): Int = oldEvents.size

    override fun getNewListSize(): Int = newEvents.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        return oldEvents[oldItemPosition].id == newEvents[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        return oldEvents[oldItemPosition] == newEvents[newItemPosition]
    }

}