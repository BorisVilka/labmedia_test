package com.test.labmedia.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.labmedia.MyApp
import com.test.labmedia.R
import com.test.labmedia.databinding.FragmentQuizBinding
import com.test.labmedia.domain.model.QuestionModel
import com.test.labmedia.ui.adapters.QuestionsAdapter
import com.test.labmedia.ui.viewmodels.QuizUIState
import com.test.labmedia.ui.viewmodels.QuizViewModel


class QuizFragment : Fragment() {

    private val viewModel: QuizViewModel by viewModels {
        (requireContext().applicationContext as MyApp).getViewModelFactory()
    }

    private lateinit var binding: FragmentQuizBinding
    private lateinit var adapter: QuestionsAdapter
    private var listState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater,container,false)
        binding.quizBackButton.setOnClickListener { requireActivity().findNavController(R.id.fragmentContainerView).popBackStack() }
        binding.questionsList.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            ).apply {
            isLastItemDecorated = false
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listState = savedInstanceState?.getParcelable(LIST_STATE_KEY)
        setupRecycler()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
        viewModel.selection.observe(viewLifecycleOwner) { selection ->
            adapter.submitSelection(selection)
        }
        viewModel.timerText.observe(viewLifecycleOwner) { text ->
            binding.timerLabel.text = text
        }
    }

    private fun invalidateUI(data: List<QuestionModel>) {
        adapter.submitList(data)
        restoreListStateIfNeeded()
    }

    private fun renderState(state: QuizUIState) {
        when (state) {
            QuizUIState.Loading -> {
                binding.progressBarQuiz.visibility = View.VISIBLE
                binding.mainBlock.visibility = View.GONE
            }
            is QuizUIState.Content -> {
                binding.progressBarQuiz.visibility = View.GONE
                binding.mainBlock.visibility = View.VISIBLE
                invalidateUI(state.data)
            }
            is QuizUIState.Error -> {
                binding.progressBarQuiz.visibility = View.GONE
                binding.mainBlock.visibility = View.GONE
                Toast.makeText(requireContext(), getString(state.messageRes), Toast.LENGTH_SHORT).show()
            }
            is QuizUIState.Solved -> {
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.action_quizFragment_to_resultFragment,viewModel.generateBundle(requireArguments()))
            }
        }
    }

    private fun setupRecycler() {
        adapter = QuestionsAdapter { questionId, answerIndex ->
            viewModel.selectAnswer(questionId, answerIndex)
        }
        binding.questionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.questionsList.adapter = adapter
    }

    private fun restoreListStateIfNeeded() {
        val state = listState ?: return
        binding.questionsList.layoutManager?.onRestoreInstanceState(state)
        listState = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val state = binding.questionsList.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, state)
    }

    companion object {
        private const val LIST_STATE_KEY = "quiz_list_state"
    }
}