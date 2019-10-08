package projects.csce.evence;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
    }
    public void generateQR(View view){
        //EditText message = findViewById(R.id.messageEdiText);
        EditText title = findViewById(R.id.title_edit_text);
        EditText startDate = findViewById(R.id.start_date_edit_text);
        EditText endDate = findViewById(R.id.end_date_edit_text);
        EditText location = findViewById(R.id.location_edit_text);
        EditText description = findViewById(R.id.description_edit_text);

        //String text= message.getText().toString(); // Whatever you need to encode in the QR code
        String text = "BEGIN:VEVENT" +
                "\nSUMMARY:" + title.getText() + "" +
                "\nDTSTART:" + startDate.getText().toString() +
                "\nDTEND:" + endDate.getText().toString() +
                "\nLOCATION:" + location.getText().toString() +
                "\nDESCRIPTION:" + description.getText().toString() +
                "\nEND:VEVENT\n";
        ImageView imageView = findViewById(R.id.QRImage);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
