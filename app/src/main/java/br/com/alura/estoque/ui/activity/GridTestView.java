package br.com.alura.estoque.ui.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import br.com.alura.estoque.R;

public class GridTestView extends RecyclerView.Adapter<GridTestView.CenterGridViewViewHolder> {

    private Context context;
    public GridTestView(Context context){
        this.context =context;
    }

    @Override
    public CenterGridViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CenterGridViewViewHolder(LayoutInflater.from(context).inflate(R.layout.new_tiem,parent,false));
    }

    @Override
    public void onBindViewHolder(CenterGridViewViewHolder holder, int position) {

        if(position%2==0){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            holder.top_header_rl.setLayoutParams(params);
        }else{
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            holder.top_header_rl.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class CenterGridViewViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout top_header_rl;
        public CenterGridViewViewHolder(View itemView) {
            super(itemView);
            top_header_rl = (LinearLayout)itemView.findViewById(R.id.top_header_rl);
        }
    }
}