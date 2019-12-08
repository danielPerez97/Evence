package projects.csce.evence.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import projects.csce.evence.R;
import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;
import projects.csce.evence.view.ui.QRDialog;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements Observer<List<EventSpec>> {
    private static final String TAG = "CardsAdapter";
    private List<EventSpec> dataList = Collections.emptyList();
    private Context context;
    private QrBitmapGenerator generator;

    public CardsAdapter(Context context, QrBitmapGenerator generator)
    {
        this.context = context;
        this.generator = generator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_entry_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsAdapter.ViewHolder holder, int position) {
        holder.bindData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void onChanged(List<EventSpec> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private CardView entryCardView;
        private TextView eventTitle;
        private TextView eventDate;
        private TextView eventTime;
        private ImageView qrImage;



        ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventTitle = itemView.findViewById(R.id.list_entry_title_textview);
            entryCardView = itemView.findViewById(R.id.list_entry_cardview);
            eventDate = itemView.findViewById(R.id.list_entry_date_textview);
            eventTime = itemView.findViewById(R.id.list_entry_time_textview);
            qrImage = itemView.findViewById(R.id.qrImageView);

        }

        //todo: tbc
        void bindData(EventSpec event) {
            eventTitle.setText(event.getTitle());
            eventDate.setText(event.getStart().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            eventTime.setText(event.getStart().format(DateTimeFormatter.ofPattern("HH:mm a")));
            qrImage.setImageBitmap(generator.forceGenerate(event));


            //using setOnClickListener loses statelistanimation
            entryCardView.setOnClickListener(view -> new QRDialog(context, event, generator));
        }
    }
}
