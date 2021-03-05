package daniel.perez.qrcameraview.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.barcode.Barcode
import daniel.perez.core.R
import daniel.perez.qrcameraview.data.ScannedData
import daniel.perez.qrcameraview.data.ScannedQR
import daniel.perez.qrcameraview.databinding.ScannedQrListEntryLayoutBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ScannedQrAdapter(private val context: Context) : RecyclerView.Adapter<ScannedQrAdapter.ViewHolder>() {
    private var dataList: List<ScannedData> = emptyList()
    private val clicks = PublishSubject.create<Barcode>()
    private val barcodeTypes = BarcodeTypes(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScannedQrListEntryLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData((dataList[position] as ScannedQR).data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun clicks(): Observable<Barcode> {
        return clicks.debounce(300, TimeUnit.MILLISECONDS )
    }

    fun setData(newData: List<ScannedData>) {
        dataList = newData
        notifyDataSetChanged()
    }

    fun clearData(){
        dataList = emptyList()
    }

    inner class ViewHolder(var binding: ScannedQrListEntryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(barcode: Barcode) {
            bind(barcode)
            binding.listEntryCardview.setOnClickListener { view: View? -> clicks.onNext(barcode) }
        }

        private fun bind(barcode: Barcode) {
            binding.qrValueTextview.text = barcode.displayValue
            binding.qrTypeTextview.text = barcodeTypes.getBarcodeTypeString(barcode)
            binding.qrIconImageView.setImageDrawable(barcodeTypes.getBarcodeTypeIcon(barcode))
            val isDark = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (isDark == Configuration.UI_MODE_NIGHT_NO)
                binding.qrIconImageView.setColorFilter(ContextCompat.getColor(context, R.color.qr_dark_tint), PorterDuff.Mode.MULTIPLY)
            else if (isDark == Configuration.UI_MODE_NIGHT_YES)
                binding.qrIconImageView.clearColorFilter()
        }
    }
}