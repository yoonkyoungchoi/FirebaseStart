package kr.hs.emirim.choi.firebaseStart.realtimedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.hs.emirim.choi.firebaseStart.R;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    private Context context = null;
    private ArrayList<MemoItem> memoItems = null;
    private MemoViewListener memoViewListener = null;

    public MemoAdapter(Context context, ArrayList<MemoItem> memoItems, MemoViewListener memoViewListener) {
        this.context = context;
        this.memoItems = memoItems;
        this.memoViewListener = memoViewListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item_list, parent, false);

        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleView.setText(memoItems.get(position).getTitle());
        holder.contentsView.setText(memoItems.get(position).getMemocontents());
    }

    @Override
    public int getItemCount() {
        return memoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public TextView titleView = null;
        public TextView contentsView = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.memotitle);
            contentsView = itemView.findViewById(R.id.memocontents);
        }

        @Override
        public void onClick(View v) {

        }
    }
}