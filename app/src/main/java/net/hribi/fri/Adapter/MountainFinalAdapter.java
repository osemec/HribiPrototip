package net.hribi.fri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hribi.fri.MountainFinal;
import net.hribi.fri.R;

import java.util.ArrayList;
import java.util.List;

public class MountainFinalAdapter extends RecyclerView.Adapter<MountainFinalAdapter.ItemViewHolder> {
    private List<String> routeName;
    private List<String> routeTime;
    private List<String> routeUrl;
    private Context ctx;

    public MountainFinalAdapter(Context ctx,List<String> routeName, List<String> routeTime, List<String> routeUrl) {
        this.ctx = ctx;
        this.routeName = routeName;
        this.routeTime = routeTime;
        this.routeUrl = routeUrl;
    }

    @Override
    public MountainFinalAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mountain_final_item, parent, false);
        return new MountainFinalAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MountainFinalAdapter.ItemViewHolder holder, int position) {
        holder.title.setText(routeName.get(position));
        holder.time.setText(routeTime.get(position));
    }

    @Override
    public int getItemCount() {
        return routeName.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, time;
        LinearLayout ll;

        ItemViewHolder(View v) {
            super(v);

            ll = (LinearLayout) v.findViewById(R.id.parent);
            title = (TextView) v.findViewById(R.id.title);
            time = (TextView) v.findViewById(R.id.time);

            ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(ctx, MountainFinal.class);
            i.putExtra("url",routeUrl.get(getAdapterPosition()));
            i.putExtra("title",routeName.get(getAdapterPosition()));
            ctx.startActivity(i);
        }
    }
}