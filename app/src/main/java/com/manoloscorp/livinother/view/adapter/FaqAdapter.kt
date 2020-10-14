package com.manoloscorp.livinother.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.listener.FaqListener
import com.manoloscorp.livinother.service.model.Faq
import com.manoloscorp.livinother.view.viewholder.FaqItemViewHolder


class FaqAdapter: RecyclerView.Adapter<FaqItemViewHolder>()  {

    private var mList: List<Faq> = listOf()
    private lateinit var mListener: FaqListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqItemViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_faq_adapter, parent, false)

        return FaqItemViewHolder(item)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: FaqItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun updateListener(list: List<Faq>) {
        mList = list
        notifyDataSetChanged()
    }

    fun attachListener(listener: FaqListener) {
        mListener = listener
    }


}