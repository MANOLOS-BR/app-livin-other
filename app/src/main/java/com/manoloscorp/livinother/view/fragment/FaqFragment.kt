package com.manoloscorp.livinother.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.listener.FaqListener
import com.manoloscorp.livinother.service.model.Faq
import com.manoloscorp.livinother.view.adapter.FaqAdapter
import com.manoloscorp.livinother.viewmodel.FaqViewModel

class FaqFragment : Fragment() {

    private lateinit var mViewModel: FaqViewModel
    private val mAdapter = FaqAdapter()

    private lateinit var mShimmerLayout: ShimmerFrameLayout

    private lateinit var mListener: FaqListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(FaqViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_faq, container, false)

        mShimmerLayout = root.findViewById(R.id.shimmer_layout)

        setRecyclerAndAdapter(root)

        observe()

        mListener = object : FaqListener {
            override fun onItemClick(param: Faq) {
                showCustomAlert(param)
            }

        }

        mShimmerLayout.visibility = View.VISIBLE
        mShimmerLayout.startShimmerAnimation()
        mViewModel.getAllFaqs()

        return root
    }

    private fun showCustomAlert(param: Faq) {
        val dialogView = layoutInflater.inflate(R.layout.content_dialog_view, null)
        val customDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .show()

        val txtTitle = dialogView.findViewById<TextView>(R.id.text_title_dialog)
        val txtMessage = dialogView.findViewById<TextView>(R.id.text_message_dialog)

        txtTitle.text = (param.question)
        txtMessage.text = (param.answer)

        val btDismiss = dialogView.findViewById<Button>(R.id.btDismissCustomDialog)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
        }
    }

    private fun setRecyclerAndAdapter(root: View) {
        val recycler: RecyclerView = root.findViewById(R.id.recycler_view)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = mAdapter
    }

    private fun observe() {
        mViewModel.faq.observe(viewLifecycleOwner, Observer {
            if (it != null && it.count() > 0) {
                mShimmerLayout.stopShimmerAnimation()
                mShimmerLayout.visibility = View.GONE
                mAdapter.updateListener(it)
            }
        })

        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.errorMessage(), Toast.LENGTH_SHORT).show()
        })

    }

    override fun onResume() {
        super.onResume()
        mAdapter.attachListener(mListener)
    }

}