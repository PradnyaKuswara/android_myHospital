package com.example.ugd3_kelompok15.ui.faq

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.entity.Question
import kotlinx.android.synthetic.main.rv_item_question.view.*

class RVQuestionAdapter(private val data: Array<Question>, private val listener: OnAdapterListener) : RecyclerView.Adapter<RVQuestionAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVQuestionAdapter.viewHolder {
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_question, parent, false))
    }

    override fun onBindViewHolder(holder: RVQuestionAdapter.viewHolder, position: Int) {
        val item = data[position]
        holder.view.tv_title_question.text = item.title
        holder.view.card_view.setOnClickListener {
            listener.onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class viewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnAdapterListener {
        fun onClick(question: Question)
    }
}