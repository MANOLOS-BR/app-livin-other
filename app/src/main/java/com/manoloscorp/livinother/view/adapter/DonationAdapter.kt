package com.manoloscorp.livinother.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Donation
import com.manoloscorp.livinother.view.viewholder.DonationItemViewHolder


class DonationAdapter : RecyclerView.Adapter<DonationItemViewHolder>() {

    private var mList: List<Donation> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationItemViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_donation_adapter, parent, false)

        return DonationItemViewHolder(item)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: DonationItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun updateListener(list: List<Donation>) {
        mList = list
        notifyDataSetChanged()
    }

}