package dev.skyit.tmdb_findyourmovie.generic

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import dev.skyit.tmdb_findyourmovie.MainActivity
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.ui.utils.Loadable

open class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId), Loadable {

    protected val parentActivity: MainActivity
        get() = activity as MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.custom_move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.custom_move)

        enterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_bottom)
        exitTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
    }

    override var isLoading: Boolean
        get() = parentActivity.isLoading
        set(value) {
            parentActivity.isLoading = value
        }


}