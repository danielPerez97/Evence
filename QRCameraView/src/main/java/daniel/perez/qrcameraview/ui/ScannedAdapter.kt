package daniel.perez.qrcameraview.ui

import android.content.Context
import daniel.perez.qrcameraview.data.ScannedData

class ScannedAdapter(private val context: Context,
                     private var dataList: List<ScannedData> = emptyList()
)

//    : BaseAdapter() {

//    fun setData(newData: List<ScannedData>) {
//        dataList = newData
//        notifyDataSetChanged()
//    }
//
//    override fun getCount(): Int {
//        return dataList.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return position
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
//        var overlay = view
//        overlay = LayoutInflater.from(context).inflate(R.layout.scanned_overlay_layout,parent, false)
//        val icon : ImageView = overlay.findViewById(R.id.overlay_icon)
//        icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24))
//
//
//        return overlay
//    }


//}