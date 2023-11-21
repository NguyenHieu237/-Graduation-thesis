package com.apero.realistic.base.adapter

interface OnItemClickListener<T> {
    fun onItemClick(position: Int, model: T)
}