package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_create_collection_accept.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class CameraFragment : BaseFragment<MainActivity>(),BaseView,View.OnClickListener {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format)
                .format(System.currentTimeMillis()) + extension)
    }
    
    private lateinit var imageCapture:ImageCapture
    private var mode = 0
    inner class ImageAnalyzer : ImageAnalysis.Analyzer {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            Log.wtf("Analyze","analyzing")
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanBarcodes(image,imageProxy)
            }
        }
    }
    private val executor = Executors.newSingleThreadExecutor()

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun getLayoutId(): Int {
        return R.layout.fragment_camera
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_camera_toolbar){
            setBackgroundColor(getColor(requireContext(),R.color.app_black_65))
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.GONE
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Далее"
            text_view_toolbar_right_text.setTextColor(getColor(requireContext(),R.color.app_light_orange))
            elevation = 0f
            customizeActionToolBar(this)
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {
        arguments?.let {
            if(it.containsKey("mode")){
                mode = it.getInt("mode")
            }

        }
    }

    override fun initializeViewsData() {

    }

    override fun onStart() {
        super.onStart()
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                currentActivity,permissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun initializeViews() {

    }

    override fun initializeListeners() {
        frame_layout_fragment_camera_take_picture.setOnClickListener(this)
    }

    override fun processPostInitialization() {

    }

    override fun disposeRequests() {

    }

    override fun displayMessage(msg: String) {

    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {

    }

    override fun hideProgress() {

    }
    
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.frame_layout_fragment_camera_take_picture -> {
                takePicture()
            }
        }
    }
    
    private fun takePicture(){
        val photoFile = createFile(FileUtils.getOutputDirectory(requireContext()), FILENAME, PHOTO_EXTENSION)
        val outputOptions =ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions,  ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(error: ImageCaptureException)
                    {
                        Log.wtf(TAG, "Photo capture failed: ${error.message}", error)
                    }
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = photoFile.toUri()
                        val bundle = Bundle()
                        when(mode){
                            0  -> {
                                bundle.putInt("mode",0)
                                bundle.putParcelable("uri",savedUri)
                                findNavController().navigate(R.id.action_cameraFragment_to_photoChooserFragment,bundle)
                            }
                            1 -> {
                               //
                            }
                            2 -> {
                                bundle.putParcelable("uri",savedUri)
                                findNavController().navigate(R.id.action_cameraFragment_to_cleanBackgroundFragment,bundle)
                            }
                        }

                    }
                })
    }
    
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(currentActivity)
        imageCapture = ImageCapture.Builder()
                .setTargetRotation(requireView().display.rotation)
                .build()
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            when(mode){
                0 -> {
                    linear_layout_fragment_camera_scanner_holder.visibility = View.GONE
                    frameLayout4.visibility = View.VISIBLE
                    try {
                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll()
        
                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(this, cameraSelector,preview, imageCapture )
        
                    } catch (exc: Exception) {
                        Log.e(TAG, "Use case binding failed", exc)
                    }
                    
                }
                1 -> {
                    linear_layout_fragment_camera_scanner_holder.visibility = View.VISIBLE
                    frameLayout4.visibility = View.GONE
                    val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
    
                    imageAnalysis.setAnalyzer(executor, ImageAnalyzer())
                    try {
                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll()
        
                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(
                                this, cameraSelector, imageAnalysis,preview
                        )
        
                    } catch (exc: Exception) {
                        Log.e(TAG, "Use case binding failed", exc)
                    }
                }
                2 -> {
                    try {
                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll()

                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(this, cameraSelector,preview, imageCapture )

                    } catch (exc: Exception) {
                        Log.e(TAG, "Use case binding failed", exc)
                    }
                }
            }
            

        }, ContextCompat.getMainExecutor(currentActivity))
    }

    private fun scanBarcodes(image: InputImage,imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()

        val scanner = BarcodeScanning.getClient()
         scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints

                    val rawValue = barcode.rawValue
                    Log.wtf("BARCODE","bounds = $bounds ,corners = $corners")
                    Log.wtf("BARCODE","rawValue = $rawValue")
                    val valueType = barcode.valueType


                    when (valueType) {
                        Barcode.TYPE_WIFI -> {
                            val ssid = barcode.wifi!!.ssid
                            val password = barcode.wifi!!.password
                            val type = barcode.wifi!!.encryptionType
                            Log.wtf("BARCODE - WIFI","ssid = $ssid ,password = $password,type = $type ")
                        }
                        Barcode.TYPE_URL -> {
                            val title = barcode.url!!.title
                            val url = barcode.url!!.url
                            Log.wtf("BARCODE - TYPE_URL","title = $title ,url = $url")

                        }
                    }

                    if(rawValue.isNotEmpty()){
                        val bundle = Bundle()
                        bundle.putString("barcode_code",rawValue)
                        findNavController().navigate(R.id.itemDetailFragment,bundle)
                    }

                }
            }
            .addOnFailureListener {
                displayMessage("Упс, что то пошло не так :(,Попробуйте еще раз")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            currentActivity, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

}