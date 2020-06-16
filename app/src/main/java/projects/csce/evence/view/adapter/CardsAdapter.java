package projects.csce.evence.view.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import projects.csce.evence.R;
import projects.csce.evence.databinding.EventsListEntryLayoutBinding;
import projects.csce.evence.service.model.FileManager;
import projects.csce.evence.service.model.qr.QrBitmapGenerator;
import projects.csce.evence.view.ui.QRDialog;
import projects.csce.evence.view.ui.model.ViewCalendarData;
import projects.csce.evence.view.ui.model.ViewEvent;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements Observer<List<ViewCalendarData>> {
    private static final String TAG = "CardsAdapter";
    private List<ViewCalendarData> dataList = Collections.emptyList();
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

    public void onChanged(List<ViewCalendarData> newData) {
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
        void bindData(ViewCalendarData ical) {
            ical.getEvents().forEach(this::bind);

            binding.listEntryCardview.setOnClickListener(view ->
            {
                new QRDialog(context, ical, ical.getEvents().get(0), generator, fileManager);

                if(listener != null)
                {
                    listener.onClick(ical);
                }
            });

        }

        private void bind(ViewEvent event)
        {
            binding.listEntryTitleTextview.setText(event.getTitle());
            binding.listEntryDateTextview.setText(event.getStartDate());
            binding.listEntryTimeTextview.setText(event.getStartTime());
            binding.qrImageView.setImageBitmap(generator.forceGenerate(event.getICalText()));

            int isDark = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (isDark == Configuration.UI_MODE_NIGHT_YES)
                binding.qrImageView.setColorFilter(ContextCompat.getColor(context, R.color.qr_dark_tint), android.graphics.PorterDuff.Mode.MULTIPLY);
            else if (isDark == Configuration.UI_MODE_NIGHT_NO)
                binding.qrImageView.clearColorFilter();

        }
    }

    public interface SelectionListener
    {
        void onClick(ViewCalendarData ical);
    }
}
