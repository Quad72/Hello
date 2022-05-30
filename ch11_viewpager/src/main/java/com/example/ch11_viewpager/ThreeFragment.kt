package com.example.ch11_jetpack


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ch11_viewpager.databinding.FragmentThreeBinding

class ThreeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return return FragmentThreeBinding.inflate(inflater, container, false).root
    }
}