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
import projects.csce.evence.databinding.EventsListEntryLayoutBinding;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EventsListEntryLayoutBinding binding = EventsListEntryLayoutBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
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
        EventsListEntryLayoutBinding binding;

        ViewHolder(@NonNull EventsListEntryLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        //todo: tbc
        void bindData(ICalSpec ical) {
            ical.getEvents().forEach(this::bind);

            binding.listEntryCardview.setOnClickListener(view ->
            {
                new QRDialog(context, ical, generator, fileManager);

                if(listener != null)
                {
                    listener.onClick(ical);
                }
            });

        }

        private void bind(EventSpec event)
        {
            binding.listEntryTitleTextview.setText(event.getTitle());
            binding.listEntryDateTextview.setText(event.getStart().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            binding.listEntryTimeTextview.setText(event.getStart().format(DateTimeFormatter.ofPattern("hh:mm a")));
            binding.qrImageView.setImageBitmap(generator.forceGenerate(event));

            int isDark = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (isDark == Configuration.UI_MODE_NIGHT_YES)
                binding.qrImageView.setColorFilter(ContextCompat.getColor(context, R.color.qr_dark_tint), android.graphics.PorterDuff.Mode.MULTIPLY);
            else if (isDark == Configuration.UI_MODE_NIGHT_NO)
                binding.qrImageView.clearColorFilter();

        }
    }

    public interface SelectionListener
    {
        void onClick(ICalSpec ical);
    }
}
