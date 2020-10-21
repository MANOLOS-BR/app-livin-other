package com.manoloscorp.livinother.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants
import com.manoloscorp.livinother.service.model.Organ
import kotlinx.android.synthetic.main.fragment_health_register.*


class OrganItemViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var mImageOrgan: ImageView = itemView.findViewById(R.id.image_organ)
    private var mTextOrgan: TextView = itemView.findViewById(R.id.text_organ)
    private var mIschemia: TextView = itemView.findViewById(R.id.text_organ_time)

    fun bindData(organ: Organ) {
        changeImageOrgan(organ.name)
        mTextOrgan.text = organ.name
        mIschemia.text = "${organ.timeIschemia} ${organ.unit}"
    }

    private fun changeImageOrgan(value: String?) {
        when (value) {
            LivinOtherConstants.ORGAN.CORACAO -> {
                mImageOrgan.setImageResource(R.drawable.ic_coracao)
            }
            LivinOtherConstants.ORGAN.PULMAO -> {
                mImageOrgan.setImageResource(R.drawable.pulmao)
            }
            LivinOtherConstants.ORGAN.RIM -> {
                mImageOrgan.setImageResource(R.drawable.rim)
            }
            LivinOtherConstants.ORGAN.FIGADO -> {
                mImageOrgan.setImageResource(R.drawable.ic_figado)
            }
            LivinOtherConstants.ORGAN.PANCREAS -> {
                mImageOrgan.setImageResource(R.drawable.pancreas)
            }
            LivinOtherConstants.ORGAN.CORNEA -> {
                mImageOrgan.setImageResource(R.drawable.ic_cornea)
            }
            else -> {
                mImageOrgan.setImageResource(R.drawable.ic_baseline_broken_image_24)
            }
        }
    }

}