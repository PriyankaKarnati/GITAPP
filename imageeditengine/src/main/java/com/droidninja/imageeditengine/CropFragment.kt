package com.droidninja.imageeditengine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.droidninja.imageeditengine.filter.ProcessingImage
import com.droidninja.imageeditengine.utils.Matrix3
import com.droidninja.imageeditengine.utils.TaskCallback
import com.droidninja.imageeditengine.utils.Utility
import com.droidninja.imageeditengine.views.cropimage.CropImageView
import com.droidninja.imageeditengine.views.cropimage.CropImageView.Guidelines


class CropFragment : BaseFragment(), View.OnClickListener {
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var cropImageView: CropImageView
    private val currentAngle = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun setImageBitmap(bitmap: Bitmap) {
        cropImageView.setImageBitmap(bitmap)
    }

    interface OnFragmentInteractionListener {
        fun onImageCropped(cropRect: Rect?)
        fun onCancelCrop()
    }

    override fun initView(view: View?) {
        cropImageView = view!!.findViewById(R.id.image_iv)
        view.findViewById<View>(R.id.cancel_tv).setOnClickListener(this)
        view.findViewById<View>(R.id.back_iv).setOnClickListener(this)
        view.findViewById<View>(R.id.rotate_iv).setOnClickListener(this)
        view.findViewById<View>(R.id.done_tv).setOnClickListener(this)
        if (arguments != null) {
            val bitmapimage: Bitmap = arguments!!.getParcelable(ImageEditor.EXTRA_ORIGINAL)!!
            Log.i("CropFragment", "${bitmapimage.width} ${bitmapimage.height}")
            cropImageView.setImageBitmap(bitmapimage)
            cropImageView.setAspectRatio(1, 1)
            cropImageView.setGuidelines(Guidelines.ON_TOUCH)
            val cropRect: Rect = arguments!!.getParcelable(ImageEditor.EXTRA_CROP_RECT)!!
            cropImageView.setCropRect(cropRect)
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.rotate_iv) {
            cropImageView.rotateImage(90)
        } else if (view.id == R.id.cancel_tv) {
            mListener?.onCancelCrop()
        } else if (view.id == R.id.done_tv) {
            val original: Bitmap = arguments!!.getParcelable(ImageEditor.EXTRA_ORIGINAL)!!
//            ProcessingImage(cropImageView.getCroppedImage(cropImageView.width, cropImageView.height), Utility.getCacheFilePath(view!!.context),
//                    object : TaskCallback<String?> {
//                        override fun onTaskDone(data: String?) {
//                            mListener?.onImageCropped(data!!)
//                        }
//                    }).execute()
            mListener?.onImageCropped(cropImageView.getCropRect())

        } else if (view.id == R.id.back_iv) {
            activity?.onBackPressed()
        }
    }


    companion object {
        fun newInstance(bitmap: Bitmap, cropRect: Rect?): CropFragment? {
            val cropFragment = CropFragment()
            val bundle = Bundle()
            bundle.putParcelable(ImageEditor.EXTRA_ORIGINAL, bitmap)
            bundle.putParcelable(ImageEditor.EXTRA_CROP_RECT, cropRect)
            cropFragment.arguments = bundle
            return cropFragment
        }
    }
}