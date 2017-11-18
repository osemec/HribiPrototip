package net.hribi.fri.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hribi.fri.Helper.DataHribi;
import net.hribi.fri.MountainDesc;
import net.hribi.fri.R;

public class MountainsListAdapter extends RecyclerView.Adapter<MountainsListAdapter.ItemViewHolder> {
    private int pos;
    private DataHribi dh;
    private String name[];
    private Integer height[];
    private Context ctx;
    private int x;

    public MountainsListAdapter(Context ctx, int pos) {
        this.ctx = ctx;
        this.pos = pos;
        dh = new DataHribi(pos);
        name = dh.getName();
        height = dh.getHeight();
    }

    @Override
    public MountainsListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new MountainsListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MountainsListAdapter.ItemViewHolder holder, int position) {
        holder.name.setText(name[position]);
        holder.height.setText(height[position]+" m");
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,height;
        LinearLayout ll;

        public ItemViewHolder(View v) {
            super(v);

            ll = (LinearLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.title);
            height = (TextView) v.findViewById(R.id.height);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,0.8f);
            float d = ctx.getResources().getDisplayMetrics().density;

            llp.setMargins((int)(16 * d), 0, 0, 0);
            name.setLayoutParams(llp);
            height.setVisibility(View.VISIBLE);

            ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(ctx, MountainDesc.class);
            i.putExtra("startpos",pos);
            i.putExtra("curpos",getAdapterPosition());
            //i.putExtra("title",MountainsListAdapter.name[0]);
            ctx.startActivity(i);
        }
    }
}
