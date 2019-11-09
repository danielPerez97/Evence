package projects.csce.evence.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import projects.csce.evence.R;
import projects.csce.evence.service.model.Event;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private static final String TAG = "CardsAdapter";
    private List<Event> dataList;
    private Context context;
    private final LayoutInflater layoutInflater;


    public CardsAdapter(Context context, List<Event> newData) {
        this.context = context;
        this.dataList = newData;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.events_list_entry_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsAdapter.ViewHolder holder, int position) {
        if (dataList != null) {
            Log.d(TAG, "onBindViewHolder: 000000000000000000000");
            Event event = dataList.get(position);
            holder.setData(position, event);
        } else {
            Log.d(TAG, "onBindViewHolder: empty list");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<Event> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private Event event;

        private TextView eventTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //todo: change to use databinding
            eventTitle = itemView.findViewById(R.id.entry_title_textview);
        }

        //todo: tbc
        public void setData(int position, Event event) {
            Log.d(TAG, "setData: 0000000000000000000000000s");
            this.position = position;
            this.event = event;

            eventTitle.setText("Event title test");
        }
    }
}
