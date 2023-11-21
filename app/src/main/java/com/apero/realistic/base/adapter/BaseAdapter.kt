package com.apero.realistic.base.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

abstract class BaseAdapter<T, V: ViewDataBinding>(var listener: OnItemClickListener<T>? = null) :
    RecyclerView.Adapter<BaseViewHolder<T, V>>() {

    companion object {
        const val TAG = "BaseAdapter + Size"
    }

    private var mListItems: ArrayList<T> = ArrayList()
    private var mOnItemClickListener: OnItemClickListener<T>? = listener

    protected abstract fun getLayoutId(viewType: Int): Int
    protected abstract fun setupBinding(parent: ViewGroup, binding: V)
    protected abstract fun bindData(item: T, position: Int, viewType: Int, binding: V)
    private val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    val listItems: ArrayList<T>
        get() = mListItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, V> {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false
        ) as V
        return BaseViewHolder(binding, list = mListItems, listener = mOnItemClickListener, bag = bag).also {
            setupBinding(parent, binding)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T, V>, position: Int) {
        bindData(mListItems[position], position, getItemViewType(position), holder.binding)
//        holder.binding.executePendingBindings()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        bag.clear()
        bag.dispose()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun setListItems(list: ArrayList<T>) {
        mListItems.clear()
        mListItems.addAll(list)
        Log.d(TAG, list.size.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mListItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        mListItems.clear()
        notifyDataSetChanged()
    }

    fun addAndNotify(element: T) {
        mListItems.add(element)
        notifyItemInserted(itemCount - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(element: T) {
        mListItems.add(element)
        notifyDataSetChanged()
    }

    fun removeAndNotify(index: Int): T {
        val t = mListItems.removeAt(index)
        notifyItemRemoved(index)
        return t
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }
}
