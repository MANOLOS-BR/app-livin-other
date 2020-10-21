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
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.view.adapter.OrganAdapter
import com.manoloscorp.livinother.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {

    private lateinit var mViewModel: DashboardViewModel

    private val mAdapter = OrganAdapter()

    private lateinit var mShimmerLayout: ShimmerFrameLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        mShimmerLayout = root.findViewById(R.id.shimmer_layout)

        setRecyclerAndAdapter(root)

        observe()

        mShimmerLayout.visibility = View.VISIBLE
        root.container_organ_date_profiles.visibility = View.GONE
        root.container_organ_ischemia.visibility = View.GONE

        mShimmerLayout.startShimmerAnimation()
        mViewModel.getDashboard()

        return root
    }

    private fun setRecyclerAndAdapter(root: View) {
        val recycler: RecyclerView = root.findViewById(R.id.recycler_view_ischemia)
        recycler.layoutManager = GridLayoutManager(context, 3)
        recycler.adapter = mAdapter
    }

    private fun observe() {
        mViewModel.dashboard.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                text_value_donor.text = it.donations.toString()
                text_value_receptor.text = it.transplants.toString()

                mShimmerLayout.stopShimmerAnimation()

                mShimmerLayout.visibility = View.GONE
                container_organ_date_profiles.visibility = View.VISIBLE
                container_organ_ischemia.visibility = View.VISIBLE

                mAdapter.updateListener(it.organList)
            }
        })

        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.errorMessage(), Toast.LENGTH_SHORT).show()
        })
    }
}