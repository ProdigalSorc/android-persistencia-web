package br.com.alura.estoque.ui.activity

import android.R
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView


class CenterGridView(context: Context) : RecyclerView.Adapter<CenterGridView.CenterGridViewViewHolder>() {
    private val context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterGridViewViewHolder {
        return CenterGridViewViewHolder(LayoutInflater.from(context).inflate(R.layout.new_tiem, parent, false))
    }

    override fun onBindViewHolder(holder: CenterGridViewViewHolder, position: Int) {
        if (position % 2 == 0) {
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.RIGHT
            holder.top_header_rl.layoutParams = params
        } else {
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.LEFT
            holder.top_header_rl.layoutParams = params
        }
    }

    override fun getItemCount(): Int {
        return 20
    }

    inner class CenterGridViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val top_header_rl: LinearLayout

        init {
            top_header_rl = itemView.findViewById(R.id.top_header_rl)
        }
    }

    init {
        this.context = context
    }
}