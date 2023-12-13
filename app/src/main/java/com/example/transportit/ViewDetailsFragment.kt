package com.example.transportit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentRequestBinding
import com.example.transportit.databinding.FragmentViewdetailsBinding

class ViewDetailsFragment : Fragment() {
    private lateinit var binding: FragmentViewdetailsBinding
    val args: ViewDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewdetailsBinding.inflate(inflater, container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvFrom.text = args.fromLocation
            tvTo.text = args.toLocation
            tvDateAndTime.text = "${args.date} ${args.time}"
            tvDateReq.text = args.dateRequested
            tvGoodType.text = args.goodsType
            tvGoodweightt.text = args.goodsWeight
            tvMessage.text = args.message
        }
    }
}