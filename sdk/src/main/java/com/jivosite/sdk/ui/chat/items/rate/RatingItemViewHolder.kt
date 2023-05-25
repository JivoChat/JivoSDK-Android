package com.jivosite.sdk.ui.chat.items.rate

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemRatingBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.RatingEntry

/**
 * Created on 20.01.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class RatingItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: RateItemViewModel
) : AdapterDelegateViewHolder<ChatEntry>(itemView) {

    init {
        val binding = DgItemRatingBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner
        binding.rating.setOnJivoRatingBarChangeListener {
            viewModel.setRate(it)
        }
        binding.comment.doOnTextChanged { text, _, _, _ ->
            viewModel.setComment(text.toString())
        }
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is RatingEntry) {
            viewModel.entry = data
        }
    }
}