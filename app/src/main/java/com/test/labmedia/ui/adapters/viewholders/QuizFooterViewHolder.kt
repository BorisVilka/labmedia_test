package com.test.labmedia.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.test.labmedia.R
import com.test.labmedia.databinding.ItemQuizFooterBinding

class QuizFooterViewHolder(
    private val binding: ItemQuizFooterBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(answeredCount: Int, totalCount: Int) {
        binding.answeredSummary.text = itemView.context.getString(
            R.string.quiz_footer_answered_format,
            totalCount-answeredCount
        )
    }
}
