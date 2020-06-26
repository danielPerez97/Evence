package daniel.perez.core.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import daniel.perez.core.R;
import daniel.perez.core.Utils;
import daniel.perez.core.databinding.EventsListEntryLayoutBinding;
import daniel.perez.core.model.UiPreference;
import daniel.perez.core.model.ViewCalendarData;
import daniel.perez.core.model.ViewEvent;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements Observer<List<ViewCalendarData>>, Consumer<List<ViewCalendarData>>
{
    private static final String TAG = "CardsAdapter";
    private List<ViewCalendarData> dataList = Collections.emptyList();
    private Context context;
    private PublishSubject<ViewCalendarData> clicks = PublishSubject.create();
    private UiPreference uiPreference;

    public CardsAdapter(Context context)
    {
        this.context = context;
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

    @Override
    public void onChanged(List<ViewCalendarData> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    public Observable<ViewCalendarData> clicks()
    {
        return clicks.debounce(300, TimeUnit.MILLISECONDS);
    }

    public void updateUiFormat(UiPreference uiPref) {
        uiPreference = uiPref;
    }

    public void setData(List<ViewCalendarData> newData)
    {
        dataList = newData;
        notifyDataSetChanged();
    }

    @Override
    public void accept(List<ViewCalendarData> viewCalendarData)
    {
        setData(viewCalendarData);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EventsListEntryLayoutBinding binding;

        ViewHolder(@NonNull EventsListEntryLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(ViewCalendarData ical) {
            ical.getEvents().forEach(this::bind);

            binding.listEntryCardview.setOnClickListener(view -> clicks.onNext(ical) );
        }

        private void bind(ViewEvent event)
        {
            binding.listEntryTitleTextview.setText(event.getTitle());
            binding.listEntryDateTextview.setText(Utils.setLocaleDateFormat(event.getStartDate()));
            binding.listEntryPreviewDay.setText(Utils.getDay(event.getStartDate()));
            binding.listEntryPreviewMonth.setText(Utils.getLocaleMonth(event.getStartDate()));
            binding.listEntryPreviewYear.setText(Utils.getYear(event.getStartDate()));
            binding.listEntryTimeTextview.setText(event.getStartTime());

            int isDark = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (isDark == Configuration.UI_MODE_NIGHT_YES)
                binding.qrImageView.setColorFilter(ContextCompat.getColor(context, R.color.qr_dark_tint), android.graphics.PorterDuff.Mode.MULTIPLY);
            else if (isDark == Configuration.UI_MODE_NIGHT_NO)
                binding.qrImageView.clearColorFilter();

            binding.qrImageView.setImageBitmap(event.getImage());
            if (uiPreference.isQrPreviewed()) {
                binding.qrImageView.setVisibility(View.VISIBLE);
                binding.datePreview.setVisibility(View.GONE);
                binding.listEntryDateTextview.setVisibility(View.VISIBLE);
            } else  {
                binding.qrImageView.setVisibility(View.GONE);
                binding.datePreview.setVisibility(View.VISIBLE);
                binding.listEntryDateTextview.setVisibility(View.GONE);
            }
        }
    }
}
