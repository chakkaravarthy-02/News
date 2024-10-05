package com.example.news.screens.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.GridItemBinding
import com.example.news.domain.News

class NewsGridAdapter(val clickListener: NewsListener) : RecyclerView.Adapter<NewsGridAdapter.NewsViewHolder>() {
    var data: List<News> = emptyList()
        set(value){
            field=value
            notifyDataSetChanged()
        }
    class NewsViewHolder(private var binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(newsProperty: News, position: Int, clickListener: NewsListener) {
            binding.property = newsProperty
            binding.newsListener = clickListener
            if(position%4==0 || position%4==2) {
                this.itemView.layoutParams.height = 600
            }else{
                this.itemView.layoutParams.height = 400
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GridItemBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int{
        return data.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsProperty = data[position]
        holder.bind(newsProperty, position, clickListener)
    }
}
class NewsListener(val clickListener: (url: String) -> Unit){
    fun onClick(news: News) = clickListener(news.url)
}

//class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
//    override fun getItemOffsets(
//        outRect: Rect,
//        view: View,
//        parent: RecyclerView,
//        state: RecyclerView.State
//    ) {
//        outRect.right = space
//        outRect.left = space
//        outRect.bottom = space
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = space
//        }
//    }
//}