package dev.skyit.tmdb_findyourmovie.ui.common

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentWebBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment

class WebFragment: BaseFragment(R.layout.fragment_web) {
    private val binding: FragmentWebBinding by viewBinding()
    private val args: WebFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(args.link)

        binding.webFragmentTittle.text = args.title ?: "Trailer"

        binding.dismissButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.linkViewerTolbar.isVisible = false
        parentActivity.setAppBarTitle(args.title ?: "Trailer")
    }
}