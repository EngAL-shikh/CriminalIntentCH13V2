package com.example.criminalintent

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import java.io.File
private val FILE_ARG = "photo File"

class Zooming:DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val view = activity?.layoutInflater?.inflate(R.layout.zoom, null)
        var imgeView = view?.findViewById(R.id.zoomingiv) as ImageView
        val imgeFile = arguments?.getSerializable(FILE_ARG) as File



        if (imgeFile.exists()) {
            var pictureUtils = PictureUtils()
            val bitmap = pictureUtils.getScaledBitmap(
                imgeFile.path, requireActivity()
            )
            imgeView.setImageBitmap(bitmap)
        } else{

            imgeView.setImageResource(R.drawable.nodata)
              }


        return AlertDialog.Builder(requireContext() , R.style.ThemeOverlay_AppCompat_Dialog_Alert)
            .setView(view)
            .setTitle("zooming")
            .setNegativeButton("ok") { dialog , _ ->
                dialog.cancel()

            }.create()
    }




    companion object {
        fun newInstance(photoFileName: File): Zooming {
            val args = Bundle().apply {
                putSerializable(FILE_ARG , photoFileName)
            }
            return Zooming().apply {
                arguments = args
            }

        }
    }

}