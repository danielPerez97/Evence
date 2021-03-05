package daniel.perez.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import daniel.perez.core.databinding.EventsListEntryLayoutBinding
import daniel.perez.core.db.timeString
import daniel.perez.core.model.UiPreference
import daniel.perez.core.model.ViewEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CardsAdapter(private val context: Context, private val imageLoader: ImageLoader) : RecyclerView.Adapter<CardsAdapter.ViewHolder>(), Observer<List<ViewEvent>>, Consumer<List<ViewEvent>> {
    private var dataList: List<ViewEvent> = emptyList()
    private val clicks = PublishSubject.create<ViewEvent>()
    private var uiPreference: UiPreference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventsListEntryLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onChanged(newData: List<ViewEvent>) {
        dataList = newData
        notifyDataSetChanged()
    }

    fun clicks(): Observable<ViewEvent> {
        return clicks.debounce(300, TimeUnit.MILLISECONDS)
    }

    fun updateUiFormat(uiPref: UiPreference?) {
        uiPreference = uiPref
    }

    fun setData(newData: List<ViewEvent>) {
        dataList = newData
        notifyDataSetChanged()
    }

    override fun accept(viewCalendarData: List<ViewEvent>) {
        setData(viewCalendarData)
    }

    inner class ViewHolder(var binding: EventsListEntryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(event: ViewEvent) {
            bind(event)
            binding.listEntryCardview.setOnClickListener { view: View? -> clicks.onNext(event) }
        }

        private fun bind(event: ViewEvent) {
            binding.listEntryTitleTextview.text = event.title
            binding.listEntryPreviewDay.text = event.startDateTime.dayOfMonth.toString()
            binding.listEntryPreviewMonth.text = event.startDatePretty().substringBefore(" ")
            binding.listEntryTimeTextview.text = event.startDateTime.timeString()

        }
    }

    companion object {
        private const val TAG = "CardsAdapter"
    }
}