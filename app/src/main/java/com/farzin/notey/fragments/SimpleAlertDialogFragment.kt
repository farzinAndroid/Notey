package com.farzin.notey.fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.farzin.notey.R
import com.farzin.notey.activities.MainActivity
import com.farzin.notey.utils.Constants
import com.farzin.notey.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SimpleAlertDialogFragment : DialogFragment() {

    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialoge_view, null)

        val persianBtn = view.findViewById<RadioButton>(R.id.persian)
        val englishBtn = view.findViewById<RadioButton>(R.id.english)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)

        if (Constants.USER_LANG == Constants.EN) {
            persianBtn.isChecked = false
            englishBtn.isChecked = true
        } else {
            persianBtn.isChecked = true
            englishBtn.isChecked = false
        }

        if (persianBtn.isChecked) {
            persianBtn.isEnabled = false
        }

        if (englishBtn.isChecked) {
            englishBtn.isEnabled = false
        }

        var lang: String

        radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            if (id == R.id.persian) {
                englishBtn.isChecked = false
                lang = Constants.FA
                dataStoreViewModel.saveUserLang(lang)
                activity?.apply {
                    finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            } else {
                persianBtn.isChecked = false
                lang = Constants.EN
                dataStoreViewModel.saveUserLang(lang)
                activity?.apply {
                    finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        }



        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
    }
}
