package com.example.news.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false)

        val arguments = WebViewFragmentArgs.fromBundle(requireArguments())

        webView = binding.webView

        webView.settings.javaScriptEnabled = true

        println(arguments.webUrl)

        webView.loadUrl(arguments.webUrl)


        webView.webViewClient = WebViewClientImpl()

        webView.settings.setSupportZoom(true)

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.destroy()
    }

    inner class WebViewClientImpl: WebViewClient(){
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            binding.webStatus.visibility = View.GONE
        }
    }
}