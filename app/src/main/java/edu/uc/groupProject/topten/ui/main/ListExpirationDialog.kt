package edu.uc.groupProject.topten.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import edu.uc.groupProject.topten.R

class ListExpirationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

           var  dialogView: View = inflater.inflate(R.layout.pop_up_list_expired,null)
            dialogView.findViewById<LottieAnimationView>(R.id.podiumAnimationView).playAnimation()

            builder.setView(dialogView)
                .setNegativeButton("Okay",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}