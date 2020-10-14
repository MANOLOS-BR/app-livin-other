package com.manoloscorp.livinother.service.listener

import com.manoloscorp.livinother.service.model.Faq

interface FaqListener {
    fun onItemClick(param: Faq)
}