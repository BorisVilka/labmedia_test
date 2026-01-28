package com.test.labmedia.ui.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.test.labmedia.MyApp
import com.test.labmedia.R
import com.test.labmedia.databinding.FragmentResultBinding
import com.test.labmedia.ui.viewmodels.InfoUIState
import com.test.labmedia.ui.viewmodels.InformationViewModel
import com.test.labmedia.ui.viewmodels.ResultUIState
import com.test.labmedia.ui.viewmodels.ResultViewModel
import com.test.labmedia.ui.viewmodels.TestResultUI
import kotlin.getValue


class ResultFragment : Fragment() {
    private val viewModel: ResultViewModel by viewModels {
        (requireContext().applicationContext as MyApp).getViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.putArgs(requireArguments())
    }

    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater,container,false)
        binding.menuButton.setOnClickListener { Toast.makeText(requireContext(),"NOT IMPLEMENT",Toast.LENGTH_SHORT).show() }
        binding.infoButton.setOnClickListener { Toast.makeText(requireContext(),"NOT IMPLEMENT",Toast.LENGTH_SHORT).show() }
        binding.searchButton.setOnClickListener { Toast.makeText(requireContext(),"NOT IMPLEMENT",Toast.LENGTH_SHORT).show() }
        binding.backButton.setOnClickListener { requireActivity().findNavController(R.id.fragmentContainerView).popBackStack() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: ResultUIState) {
        when (state) {
            ResultUIState.Loading -> {
                //binding.progressBar.visibility = View.VISIBLE
                //binding.mainBlock.visibility = View.GONE
            }
            is ResultUIState.Content -> {
                //binding.progressBar.visibility = View.GONE
                //binding.mainBlock.visibility = View.VISIBLE
                invalidateUI(state.ui)
            }
            is ResultUIState.Error -> {
                //binding.progressBar.visibility = View.GONE
                //binding.mainBlock.visibility = View.GONE
                Toast.makeText(requireContext(), getString(state.messageRes), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun invalidateUI(data: TestResultUI) {
        binding.timeStart.text = data.timeStartText
        binding.dateStart.text = data.dateStartText
        binding.timeEnd.text = data.timeEndText
        binding.dateEnd.text = data.dateEndText
       binding.titleQuiz.text = data.title
        binding.maxScoreText.text = requireContext().getString(
            R.string.max_score_format,
            data.maxScore
        )
        binding.minScoreText.text = requireContext().getString(
            R.string.min_score_format,
            data.minScore
        )
        binding.score.text = data.score.toString()
        binding.result.background = AppCompatResources.getDrawable(requireContext(),R.drawable.result_bg)
            .apply {
                this!!.setColorFilter(
                    ColorUtils.setAlphaComponent(requireContext().getColor(if(data.success) R.color.success else R.color.fail),22),
                    PorterDuff.Mode.SRC_IN
                )
            }
        binding.statusText.setText(if(data.success) R.string.success else R.string.fail)
        binding.statusLogo.setColorFilter(ColorUtils.setAlphaComponent(requireContext().getColor(if(data.success) R.color.success else R.color.fail),220),
            PorterDuff.Mode.SRC_IN)
        binding.statusLogo.foreground = AppCompatResources.getDrawable(requireContext(),if(data.success) R.drawable.outline_check_24 else R.drawable.outline_close_24)
    }

}