package com.devs.imgur.ui

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devs.imgur.ImgurApplication
import com.devs.imgur.R
import com.devs.imgur.data.repository.resource.NetworkError
import com.devs.imgur.data.repository.resource.Resource
import com.devs.imgur.data.repository.resource.Status
import com.devs.imgur.util.DialogLoader
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {

    private var dialogLoader: DialogLoader? = null

    fun showLoader() {
        dialogLoader?.let {
            if (it.isShowing) it.dismiss()
        }
        dialogLoader = DialogLoader(requireActivity())
        dialogLoader?.show()
    }

    fun hideLoader() {
        dialogLoader?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    fun toast(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun log(msg: String) {
        if (ImgurApplication.LOGGER_ENABLE)
            Log.i("BaseFragment", msg)
    }

    fun View.showSnackBar(message: String) = Snackbar
        .make(this, message, 5000)
        .show()

    /**
     * It will return the correct error message, depending on the [NetworkError] & [Resource] message.
     */
    fun errorMessage(resource: Resource<Any>): String {
        val message = resource.message ?: if (resource.status == Status.ERROR) {
            resource.networkError?.let { networkError ->
                when (networkError) {
                    NetworkError.NO_CONNECTIVITY -> getString(R.string.no_network_error)
                    NetworkError.TIMEOUT -> getString(R.string.bad_connection)
                    NetworkError.UNKNOWN -> getString(R.string.unknown_error)
                    NetworkError.INVALID_SERVER_URL -> getString(R.string.invalid_url_error)
                }
            }
        } else null
        return message ?: getString(R.string.unknown_error)
    }

    fun hideKeyboard(activity: Activity) {

        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.

        if (activity.currentFocus != null) {
            val view: View = activity.currentFocus as View
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun addFragmentWithoutRemove(containerViewId: Int, fragment: Fragment, fragmentTag: String) {
        try {
            //fragment.enterTransition = Slide(Gravity.END)
            //fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    fun replaceFragmentWithoutBack(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String
    ) {
        try {
            if (activity == null) return
            //fragment.enterTransition = Slide(Gravity.END)
            //fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }
}
