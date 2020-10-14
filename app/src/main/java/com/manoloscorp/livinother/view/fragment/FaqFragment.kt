package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.listener.FaqListener
import com.manoloscorp.livinother.view.adapter.FaqAdapter
import com.manoloscorp.livinother.viewmodel.FaqViewModel

class FaqFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: FaqViewModel
    private val mAdapter = FaqAdapter()
    private lateinit var mListener: FaqListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(FaqViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_faq, container, false)

        setRecyclerAndAdapter(root)

        observe()

        mViewModel.getAllFaqs()

        return root
    }

    private fun setRecyclerAndAdapter(root: View) {
        val recycler: RecyclerView = root.findViewById(R.id.recycler_view)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = mAdapter
    }

    private fun observe() {
        mViewModel.faq.observe(viewLifecycleOwner, Observer {
            if (it != null && it.count() > 0) {
                mAdapter.updateListener(it)
            }
        })

        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.errorMessage(), Toast.LENGTH_SHORT).show()
        })

    }


    override fun onClick(view: View) {

    }
}