package com.test.labmedia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import com.test.labmedia.MyApp
import com.test.labmedia.R
import com.test.labmedia.databinding.FragmentInformationBinding
import com.test.labmedia.ui.viewmodels.InfoUIState
import com.test.labmedia.ui.viewmodels.InformationViewModel
import com.test.labmedia.ui.viewmodels.TestUI


class InformationFragment : Fragment() {

    private val viewModel: InformationViewModel by viewModels {
        (requireContext().applicationContext as MyApp).getViewModelFactory()
    }

    private lateinit var binding: FragmentInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater,container,false)
        binding.scrollButton.setOnClickListener { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
        binding.scrollView.setOnScrollChangeListener { p0, p1, p2, p3, p4 -> checkScroll() }
        binding.button.setOnClickListener {
            requireActivity().findNavController(R.id.fragmentContainerView)
                .navigate(R.id.action_informationFragment_to_quizFragment, Bundle().apply {
                    putString("dateTimeStart",viewModel.getDateTimeStart())
                })
        }
        binding.menuButton.setOnClickListener { Toast.makeText(requireContext(),"NOT IMPLEMENT",Toast.LENGTH_SHORT).show() }
        binding.infoButton.setOnClickListener { Toast.makeText(requireContext(),"NOT IMPLEMENT",Toast.LENGTH_SHORT).show() }
        binding.searchButton.setOnClickListener { Toast.makeText(requireContext(),"NOT IMPLEMENT",Toast.LENGTH_SHORT).show() }
        TooltipCompat.setTooltipText(binding.certificateLogo,getString(R.string.about_cetificate))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun checkScroll() {
        if (binding.scrollView.canScrollVertically(1)) binding.scrollButton.visibility = View.VISIBLE
        else binding.scrollButton.visibility = View.GONE
    }

    private fun loadImage(url: String) {
        Picasso.get()
            .load(url)
            .error(R.drawable.image)
            .into(binding.imageTest)
    }

    private fun renderState(state: InfoUIState) {
        when (state) {
            InfoUIState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.mainBlock.visibility = View.GONE
            }
            is InfoUIState.Content -> {
                binding.progressBar.visibility = View.GONE
                binding.mainBlock.visibility = View.VISIBLE
                invalidateUI(state.ui)
            }
            is InfoUIState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.mainBlock.visibility = View.GONE
                Toast.makeText(requireContext(), getString(state.messageRes), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun invalidateUI(data: TestUI) {
        loadImage(data.imageUrl)
        binding.time.text = data.timeText
        binding.date.text = data.dateText
        binding.description.text = data.description
        binding.testTitle.text = data.title
        if (data.dopInfo.isNotEmpty()) {
            binding.dopInfoBlock.visibility = View.VISIBLE
            binding.dopInfo.text = data.dopInfo
        } else {
            binding.dopInfoBlock.visibility = View.GONE
        }
        binding.statusLogo.setImageResource(data.statusIconRes)
        binding.statusText.setText(data.statusTextRes)
        binding.button.visibility = if (data.showButton) View.VISIBLE else View.GONE
        var showReward = false
        if(data.certificate) {
            showReward = true
            binding.certificateCard.visibility = View.VISIBLE
        } else binding.certificateCard.visibility = View.GONE
        if(data.starsCount>0) {
            showReward = true
            binding.starsCount.text = data.starsCount.toString()
            binding.starsCard.visibility = View.VISIBLE
        } else binding.starsCard.visibility = View.GONE
        if(data.coinsCount>0) {
            showReward = true
            binding.coinsCount.text = data.coinsCount.toString()
            binding.coinsCard.visibility = View.VISIBLE
        } else binding.coinsCard.visibility = View.GONE
        if(showReward) binding.rewardBlock.visibility = View.VISIBLE
        else binding.rewardBlock.visibility = View.GONE
        checkScroll()
    }

}