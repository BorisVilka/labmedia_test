package com.test.labmedia.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.labmedia.databinding.ItemQuizFooterBinding
import com.test.labmedia.databinding.QuestionItemBinding
import com.test.labmedia.domain.model.QuestionModel
import com.test.labmedia.ui.adapters.viewholders.QuestionViewHolder
import com.test.labmedia.ui.adapters.viewholders.QuizFooterViewHolder

class QuestionsAdapter(
    private val onAnswerSelected: (questionId: Int, answerIndex: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: List<QuestionModel> = emptyList()
    private var selection: Map<Int, Int> = emptyMap()

    init {
        setHasStableIds(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<QuestionModel>) {
        data = items
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitSelection(selection: Map<Int, Int>) {
        val previous = this.selection
        this.selection = selection
        val changedIds = (previous.keys + selection.keys).filter { previous[it] != selection[it] }
        for (id in changedIds) {
            val position = data.indexOfFirst { it.id == id }
            if (position != -1) {
                notifyItemChanged(position)
            }
        }
        notifyItemChanged(footerPosition())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            QuizFooterViewHolder(
                ItemQuizFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            QuestionViewHolder(
                QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onAnswerSelected
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is QuizFooterViewHolder) {
            holder.bind(selection.size, data.size)
            return
        }
        val item = data[position]
        val selectedAnswer = selection[item.id]
        (holder as QuestionViewHolder).bind(item, selectedAnswer)
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemId(position: Int): Long {
        return if (position == data.size) {
            FOOTER_ID
        } else {
            data[position].id.toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) VIEW_TYPE_FOOTER else VIEW_TYPE_QUESTION
    }

    private fun footerPosition(): Int = data.size

    private companion object {
        const val VIEW_TYPE_QUESTION = 1
        const val VIEW_TYPE_FOOTER = 2
        const val FOOTER_ID = Long.MIN_VALUE
    }
}

