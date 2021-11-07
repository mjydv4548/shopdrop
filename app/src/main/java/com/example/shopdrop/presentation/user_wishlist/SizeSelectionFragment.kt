package com.example.shopdrop.presentation.user_wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.presentation.user_wishlist.adapter.BottomSheetAdapter
import com.example.shopdrop.presentation.user_wishlist.model.Size
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_size_selection.*

class SizeSelectionFragment : BottomSheetDialogFragment(), BottomSheetAdapter.SelectedSize {
    private lateinit var adapter: BottomSheetAdapter
    private val args: SizeSelectionFragmentArgs by navArgs()
    private lateinit var previousSavedStateHandle: SavedStateHandle
    private var selectedSize: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_size_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousSavedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle

        size_price.text = "$${args.productPrice}"
        val sizeList: MutableList<String> = args.productSizeList.toMutableList()

        val size = Size(sizeList)

        adapter = BottomSheetAdapter(requireContext(), size, this)
        size_selector_recycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        size_selector_recycleView.adapter = adapter

        btn_size_done.setOnClickListener {

        }

        btn_size_done.setOnClickListener {
            if (selectedSize.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a size", Toast.LENGTH_SHORT).show()
            } else {
                previousSavedStateHandle.set(Constants.SELECTED_SIZE, selectedSize)
                findNavController().popBackStack()
            }
        }

    }

    override fun selectedSize(size: String) {
        selectedSize = size
    }
}

