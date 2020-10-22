package com.manoloscorp.livinother.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Donation
import com.manoloscorp.livinother.service.model.Transplant


class DonationItemViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var mTextState: TextView = itemView.findViewById(R.id.text_donation_state)

    private var mPotentialDonor: TextView = itemView.findViewById(R.id.text_potential_donor)
    private var mEffectiveDonor: TextView = itemView.findViewById(R.id.text_effective_donor)

    private var mFamilyInterview: TextView = itemView.findViewById(R.id.text_family_interview)
    private var mFamilyNegative: TextView = itemView.findViewById(R.id.text_family_negative)

    fun bindData(donation: Donation) {
        mTextState.text = donation.state.name

        mPotentialDonor.text = donation.potentialDonor.toString()
        mEffectiveDonor.text = donation.effectiveDonor.toString()

        mFamilyInterview.text = donation.familyInterview.toString()
        mFamilyNegative.text = donation.familyNegative.toString()
    }


}