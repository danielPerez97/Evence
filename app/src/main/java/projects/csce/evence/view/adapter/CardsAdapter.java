package projects.csce.evence.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import projects.csce.evence.R;
import projects.csce.evence.ical.EventSpec;
import projects.csce.evence.ical.ICalSpec;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;
import projects.csce.evence.view.ui.QRDialog;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements Observer<List<ICalSpec>> {
    private static final String TAG = "CardsAdapter";
    private List<ICalSpec> dataList = Collections.emptyList();
    private Context context;
    private QrBitmapGenerator generator;
    private FileManager fileManager;
    private SelectionListener listener;

    public CardsAdapter(Context context, QrBitmapGenerator generator, FileManager fileManager)
    {
        this.context = context;
        this.generator = generator;
        this.fileManager = fileManager;
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

    public void onChanged(List<ICalSpec> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    public void setSelectionListener(SelectionListener listener)
    {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private CardView entryCardView;
        private TextView eventTitle;
        private TextView eventDate;
        private TextView eventTime;
        private ImageView qrImage;
        private CardView editButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.list_entry_title_textview);
            entryCardView = itemView.findViewById(R.id.list_entry_cardview);
            eventDate = itemView.findViewById(R.id.list_entry_date_textview);
            eventTime = itemView.findViewById(R.id.list_entry_time_textview);
            qrImage = itemView.findViewById(R.id.qrImageView);
        }

        //todo: tbc
        void bindData(ICalSpec ical) {
            EventSpec event = ical.getEvents().get(0);
            eventTitle.setText(event.getTitle());
            eventDate.setText(event.getStart().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            eventTime.setText(event.getStart().format(DateTimeFormatter.ofPattern("hh:mm a")));
            qrImage.setImageBitmap(generator.forceGenerate(event));

            entryCardView.setOnClickListener(view ->
            {
                new QRDialog(context, ical, generator, fileManager);

                if(listener != null)
                {
                    listener.onClick(ical);
                }
            });

            int isDark = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (isDark == Configuration.UI_MODE_NIGHT_YES)
                qrImage.setColorFilter(ContextCompat.getColor(context, R.color.qr_dark_tint), android.graphics.PorterDuff.Mode.MULTIPLY);
            else if (isDark == Configuration.UI_MODE_NIGHT_NO)
                qrImage.clearColorFilter();


        }
    }

    public interface SelectionListener
    {
        void onClick(ICalSpec ical);
    }
}
