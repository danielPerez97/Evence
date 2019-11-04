package projects.csce.evence.service.model.qr;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrBitmapGenerator
{
    private MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    private BarcodeEncoder barcodeEncoder = new BarcodeEncoder();


    public Bitmap generate(Qr qr) throws WriterException
    {
        BitMatrix bitMatrix = multiFormatWriter.encode(qr.text(), BarcodeFormat.QR_CODE,600,600);
        return barcodeEncoder.createBitmap(bitMatrix);
    }

}
