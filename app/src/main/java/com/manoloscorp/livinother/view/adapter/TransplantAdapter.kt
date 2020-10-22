package com.manoloscorp.livinother.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Transplant
import com.manoloscorp.livinother.view.viewholder.TransplantItemViewHolder


class TransplantAdapter : RecyclerView.Adapter<TransplantItemViewHolder>() {

    private var mList: List<Transplant> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransplantItemViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_transplant_adapter, parent, false)

        return TransplantItemViewHolder(item)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: TransplantItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun updateListener(list: List<Transplant>) {
        mList = list
        notifyDataSetChanged()
    }

}