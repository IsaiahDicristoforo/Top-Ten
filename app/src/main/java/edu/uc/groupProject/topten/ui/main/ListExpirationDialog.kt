package edu.uc.groupProject.topten.ui.main

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import edu.uc.groupProject.topten.R

class ListExpirationDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            var  dialogView: View = inflater.inflate(R.layout.pop_up_list_expired, null)
            dialogView.findViewById<LottieAnimationView>(R.id.podiumAnimationView).playAnimation()

            var progressBar:ProgressBar =  dialogView.findViewById<ProgressBar>(R.id.dialogProgressBar)
            var listTitleTextBox:TextView = dialogView.findViewById(R.id.listTitleDialogView)
            listTitleTextBox.text = arguments!!.getString("ListTitle")

            var pointsEarned = arguments!!.getInt("PointsEarned")
            var totalPoints = arguments!!.getInt("TotalPoints")

           var pointsView = dialogView.findViewById<TextView>(R.id.tv_PointsEarned)


            val anim = ValueAnimator.ofInt(totalPoints, (totalPoints + pointsEarned)).apply {
                addUpdateListener {
                    pointsView.text = animatedValue.toString()
                }
            }
            anim.duration = 1500
            anim.start()


            dialogView.findViewById<TextView>(R.id.tv_winningItem).text = arguments!!.getString("FirstPlace")

            object : CountDownTimer(10000, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    progressBar.progress = progressBar.progress - 1
                }
                override fun onFinish() {
                    this@ListExpirationDialog.dismiss()
                }
            }.start()

            builder.setView(dialogView)
                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                })

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}