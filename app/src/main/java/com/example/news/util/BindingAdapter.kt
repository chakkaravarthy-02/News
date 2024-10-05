package com.example.news.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, urlToImage: String?) {
    urlToImage.let {
        Glide.with(imageView.context)
            .load(urlToImage)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}

@BindingAdapter("title")
fun setTitle(textView: TextView, title: String?) {
    textView.text = title
}

@BindingAdapter("description")
fun setDescription(textView: TextView, description: String?) {
    textView.text = description
}


@BindingAdapter("city_name")
fun setCity(textView: TextView, name: String?){
    textView.text = name
}

@BindingAdapter("degree")
fun setDegree(textView: TextView, temp: Double?){
    textView.text = "$temp°C"
}

@BindingAdapter("weather_description")
fun setWeatherDescription(textView: TextView, description: String?){
    textView.text = description
}

@BindingAdapter("icon")
fun setWeatherImage(imageView: ImageView, iconId: String?){
    iconId.let {
        val url = "https://openweathermap.org/img/wn/$iconId.png"
        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
            )
            .into(imageView)
    }
}



