package com.manoloscorp.livinother.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Transplant


class TransplantItemViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var mTextOrgan: TextView = itemView.findViewById(R.id.text_organ)
    private var mQueueAmount: TextView = itemView.findViewById(R.id.text_queue_amount)
    private var mNumberTransplants: TextView = itemView.findViewById(R.id.text_number_transplants)

    fun bindData(transplant: Transplant) {
        mTextOrgan.text = transplant.organ?.name
        mQueueAmount.text = transplant.queueAmount.toString()
        mNumberTransplants.text = transplant.numberTransplants.toString()
    }


}