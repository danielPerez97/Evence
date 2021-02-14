package daniel.perez.core.adapter

import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import daniel.perez.core.model.UiPreference
import daniel.perez.core.model.ViewEvent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.ImageLoader
import coil.load
import daniel.perez.core.*
import daniel.perez.core.databinding.EventsListEntryLayoutBinding
import daniel.perez.core.db.timeString
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
            binding.listEntryDateTextview.text = event.startDatePretty()
            binding.listEntryPreviewDay.text = event.startDateTime.dayOfMonth.toString()
            binding.listEntryPreviewMonth.text = event.startDateTime.monthValue.toString()
            binding.listEntryPreviewYear.text = event.startDateTime.year.toString()
            binding.listEntryTimeTextview.text = event.startDateTime.timeString()
            val isDark = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (isDark == Configuration.UI_MODE_NIGHT_YES) binding.qrImageView.setColorFilter(ContextCompat.getColor(context, R.color.qr_dark_tint), PorterDuff.Mode.MULTIPLY) else if (isDark == Configuration.UI_MODE_NIGHT_NO) binding.qrImageView.clearColorFilter()
            binding.qrImageView.load(event.imageFileUri, imageLoader)
            if (uiPreference!!.isQrPreviewed) {
                binding.qrImageView.visibility = View.VISIBLE
                binding.datePreview.visibility = View.GONE
                binding.listEntryDateTextview.visibility = View.VISIBLE
            } else {
                binding.qrImageView.visibility = View.GONE
                binding.datePreview.visibility = View.VISIBLE
                binding.listEntryDateTextview.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val TAG = "CardsAdapter"
    }
}