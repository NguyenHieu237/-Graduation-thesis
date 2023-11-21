package com.apero.realistic.base.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.apero.realistic.base.ext.click
import com.apero.realistic.base.ext.disposedBy
import com.apero.realistic.base.ext.ignoreFastTap
import io.reactivex.disposables.CompositeDisposable

class BaseViewHolder<T, out V: ViewDataBinding>
constructor(val binding: V,
            val list: ArrayList<T>? = null,
            val listener: OnItemClickListener<T>? = null,
            val bag: CompositeDisposable)
    : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.click()
            .ignoreFastTap()
            .subscribe {
                list?.get(adapterPosition)?.let {
                    listener?.onItemClick(adapterPosition, it)
                }
            }.disposedBy(bag)
    }
}