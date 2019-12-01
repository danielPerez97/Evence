package projects.csce.evence.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import projects.csce.evence.R;
import projects.csce.evence.ical.EventSpec;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements Observer<List<EventSpec>>
{
    private static final String TAG = "CardsAdapter";
    private List<EventSpec> dataList = Collections.emptyList();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_entry_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsAdapter.ViewHolder holder, int position)
    {
        holder.bindData(dataList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    public void onChanged(List<EventSpec> newData)
    {
        dataList = newData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView eventTitle;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            //todo: change to use databinding
            eventTitle = itemView.findViewById(R.id.entry_title_textview);
        }

        //todo: tbc
        void bindData(EventSpec event)
        {
            eventTitle.setText(event.getTitle());
        }
    }
}
