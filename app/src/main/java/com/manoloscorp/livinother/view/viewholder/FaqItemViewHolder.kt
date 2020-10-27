package com.manoloscorp.livinother.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.listener.FaqListener
import com.manoloscorp.livinother.service.model.Faq


class FaqItemViewHolder(itemView: View, val listener: FaqListener) :
    RecyclerView.ViewHolder(itemView) {

    private var mContainerItem: ConstraintLayout = itemView.findViewById(R.id.container_item_faq)
    private var mQuestion: TextView = itemView.findViewById(R.id.text_question)
//    private var mAnswer: TextView = itemView.findViewById(R.id.text_answer)

    fun bindData(faq: Faq) {
        this.mQuestion.text = faq.question
//        this.mAnswer.text = faq.answer

        mContainerItem.setOnClickListener { listener.onItemClick(faq) }
    }

}