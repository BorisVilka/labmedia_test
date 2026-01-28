package com.test.labmedia.ui.adapters.viewholders

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.labmedia.R
import com.test.labmedia.databinding.QuestionItemBinding
import com.test.labmedia.domain.model.QuestionModel

class QuestionViewHolder(
    private val binding: QuestionItemBinding,
    private val onAnswerSelected: (questionId: Int, answerIndex: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var selectedColor: Int = binding.root.context.getColor(R.color.selected)
    fun bind(data: QuestionModel, selectedAnswer: Int?) {
        val number = if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            (bindingAdapterPosition + 1).toString()
        } else {
            ""
        }
        binding.numberQuestion.text = number
        binding.costQuestion.text = itemView.context.getString(
            R.string.question_cost_format,
            data.score
        )
        binding.titleQuestion.text = data.question

        bindAnswer(
            container = binding.ans1,
            textView = binding.text1,
            checkView = binding.check1,
            text = data.answers.answer1,
            answerIndex = 1,
            questionId = data.id,
            selectedAnswer = selectedAnswer
        )
        bindAnswer(
            container = binding.ans2,
            textView = binding.text2,
            checkView = binding.check2,
            text = data.answers.answer2,
            answerIndex = 2,
            questionId = data.id,
            selectedAnswer = selectedAnswer
        )
        bindAnswer(
            container = binding.ans3,
            textView = binding.text3,
            checkView = binding.check3,
            text = data.answers.answer3,
            answerIndex = 3,
            questionId = data.id,
            selectedAnswer = selectedAnswer
        )
        bindAnswer(
            container = binding.ans4,
            textView = binding.text4,
            checkView = binding.check4,
            text = data.answers.answer4,
            answerIndex = 4,
            questionId = data.id,
            selectedAnswer = selectedAnswer
        )
        bindAnswer(
            container = binding.ans5,
            textView = binding.text5,
            checkView = binding.check5,
            text = data.answers.answer5,
            answerIndex = 5,
            questionId = data.id,
            selectedAnswer = selectedAnswer
        )
        bindAnswer(
            container = binding.ans6,
            textView = binding.text6,
            checkView = binding.check6,
            text = data.answers.answer6,
            answerIndex = 6,
            questionId = data.id,
            selectedAnswer = selectedAnswer
        )
    }

    private fun bindAnswer(
        container: View,
        textView: TextView,
        checkView: ImageView,
        text: String?,
        answerIndex: Int,
        questionId: Int,
        selectedAnswer: Int?
    ) {
        binding.checkQuestion.setColorFilter(if(selectedAnswer!=null) selectedColor else Color.GRAY)
        if (text.isNullOrBlank()) {
            container.visibility = View.GONE
            return
        }
        container.visibility = View.VISIBLE
        textView.text = text
        val selected = selectedAnswer == answerIndex
        checkView.setColorFilter(
            if (selected) selectedColor else android.R.color.transparent
        )
        textView.setBackgroundResource(if(selected) R.drawable.corner_selected else R.drawable.corner_unselected)
        container.setOnClickListener {
            onAnswerSelected(questionId, answerIndex)
        }
    }
}