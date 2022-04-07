package org.techtown.trip_app.Map

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.techtown.trip_app.R
import org.techtown.trip_app.databinding.ToastSampleBinding

object SampleToast {

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastSampleBinding = DataBindingUtil.inflate(inflater, R.layout.toast_sample, null, false)

        binding.tvSample.text = message

        return Toast(context).apply {
            setGravity(Gravity.CENTER, 0, 16.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

}