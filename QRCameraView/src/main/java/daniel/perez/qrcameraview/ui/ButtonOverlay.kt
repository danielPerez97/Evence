package daniel.perez.qrcameraview.ui

import android.content.Context
import daniel.perez.qrcameraview.databinding.ActivityQrReaderBinding

class ButtonOverlay(private val context: Context,
                    private val binding: ActivityQrReaderBinding,
                    ) {
//
//    private lateinit var outlineOverlay: OutlineOverlay
//    private lateinit var barcodes: Barcode
//    private lateinit var scannedText: Text
//
//
//    init{
//        setup()
//    }
//
//    private fun setup(){
//        outlineOverlay = OutlineOverlay(context)
//    }
//
//    private fun updateViews() {
//        if (isScanning()) {
//            binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_search_white_24dp))
//            overlay.clearOverlays()
//            when (currentScanType) {
//                CameraHandler.SCAN_TYPE.BARCODE -> {
//                    binding.result.text = "Scanning for QR codes"
//                }
//                CameraHandler.SCAN_TYPE.TEXT -> {
//                    binding.result.text = "Scanning for texts"
//                }
//            }
//        } else {
//
//            when (currentScanType) {
//                CameraHandler.SCAN_TYPE.BARCODE -> {
//
//                    when (qrData.valueType) {
//                        Barcode.TYPE_CALENDAR_EVENT -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_event_white_36dp))
//                        Barcode.TYPE_URL -> binding.qrTypeCardview.setImageDrawable(context.getDrawable(R.drawable.ic_open_in_new_white_24dp))
//                        Barcode.TYPE_CONTACT_INFO -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_person_add_white_24dp))
//                        Barcode.TYPE_EMAIL -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_email_white_24dp))
//                        Barcode.TYPE_PHONE -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_phone_white_24dp))
//                        Barcode.TYPE_SMS -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_textsms_black_24dp))
//                        Barcode.TYPE_ISBN -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_shopping_cart_white_24dp))
//                        Barcode.TYPE_WIFI -> {
//                            binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_wifi_white_24dp))
//                            binding.result.text = "Network name: ${qrData.wifi.ssid} \n Password: ${qrData.wifi.password}"
//                        }
//                        Barcode.TYPE_GEO -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_place_white_24dp))
//                        Barcode.TYPE_DRIVER_LICENSE -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_account_box_white_24dp))
//                        else -> binding.qrTypeCardview.setImageDrawable(getDrawable(R.drawable.ic_short_text_white_24dp))
//                    }
//                }
//
//                CameraHandler.SCAN_TYPE.TEXT -> {
//                    binding.result.text = scannedText.textBlocks[0].text
//                    overlay.addOverlay(textBoundingBoxes)
//                }
//            }
//        }
//    }
//
//    fun updateOverlays() {
////        val icon = ImageView(context)
////        icon.setImageResource(R.drawable.ic_baseline_qr_code_scanner_24)
////        binding.parentLayout.addView(icon)
//
//        clearOverlays()
//
//        binding.parentLayout.addView(outlineOverlay)
//        setOverlayButtonIcon()
//    }
//
//    fun setOverlayButtonIcon(){
//
//    }
//
//    fun clearOverlays() {
//        outlineOverlay.clearOverlays()
//    }
//





}