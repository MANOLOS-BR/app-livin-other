package com.manoloscorp.livinother.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Organ
import com.manoloscorp.livinother.view.viewholder.OrganItemViewHolder


class OrganAdapter : RecyclerView.Adapter<OrganItemViewHolder>() {

    private var mList: List<Organ> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganItemViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_ischemia_adapter, parent, false)

        return OrganItemViewHolder(item)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: OrganItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun updateListener(list: List<Organ>) {
        mList = list
        notifyDataSetChanged()
    }

}