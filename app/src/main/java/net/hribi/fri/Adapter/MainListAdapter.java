package net.hribi.fri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hribi.fri.MountainsList;
import net.hribi.fri.R;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ItemViewHolder> {
    String data[] = new String[]{"Goriško, Notranjsko in Snežniško hribovje", "Julijske Alpe",
            "Kamniško Savinjske Alpe", "Karavanke", "Pohorje in ostala severovzhodna Slovenija", "Polhograjsko hribovje in Ljubljana",
            "Škofjeloško, Cerkljansko hribovje in Jelovica", "Zasavsko - Posavsko hribovje in Dolenjska"};
    private Context ctx;

    public MainListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public MainListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainListAdapter.ItemViewHolder holder, int position) {
        holder.tv.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        LinearLayout ll;

        ItemViewHolder(View v) {
            super(v);
            ll = (LinearLayout) v.findViewById(R.id.parent);
            tv = (TextView) v.findViewById(R.id.title);

            ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(ctx, MountainsList.class);
            i.putExtra("pos", getAdapterPosition());
            i.putExtra("title",data[getAdapterPosition()]);
            ctx.startActivity(i);
        }
    }
}
