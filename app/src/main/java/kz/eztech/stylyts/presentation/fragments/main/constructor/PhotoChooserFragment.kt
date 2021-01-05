package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_photo_chooser.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.include_toolbar_income_detail
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.collections.PhotoChooserContract
import kz.eztech.stylyts.presentation.utils.FileUtils
import java.io.IOException
import java.lang.Exception

class PhotoChooserFragment : BaseFragment<MainActivity>(),PhotoChooserContract.View {
    private val PHOTO_FROM_GALLERY = 3
    private val REQUEST_CODE_PERMISSIONS = 10
    private var currentPhotoPath:String? = null
    private var currentType = ""
    private var currentInputImage : InputImage? = null

    private lateinit var barcodeOptions: BarcodeScannerOptions
    companion object{
        const val PHOTO_TYPE = "photo_type"
        const val BAR_CODE = "bar_code"
        const val PHOTO_LIBRARY = "photo_library"
        const val PHOTO_CAMERA = "photo_camera"
    }
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            currentActivity, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_chooser
    }

    override fun getContractView(): BaseView {
       return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_photo_chooser){
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            image_button_right_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Далее"
            elevation = 0f
            customizeActionToolBar(this,"Создать образ")
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {
        arguments?.let {
            if(it.containsKey(PHOTO_TYPE)){
                when(it.getString(PHOTO_TYPE)){
                    BAR_CODE -> {
                        currentType = BAR_CODE
                    }
                    PHOTO_CAMERA -> {
                        currentType = PHOTO_CAMERA
                    }
                    PHOTO_LIBRARY -> {
                        currentType = PHOTO_LIBRARY
                    }

                }
            }
        }
    }

    private fun processCurrentType(){
        when(currentType){
            BAR_CODE -> {
                startChooser()
            }
            PHOTO_CAMERA -> {

            }
            PHOTO_LIBRARY -> {
                startChooser()
            }
        }
    }

    override fun initializeViewsData() {
        barcodeOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()
    }

    override fun initializeViews() {
        // Request camera permissions
        if (allPermissionsGranted()) {
            startChooser()
        } else {
            ActivityCompat.requestPermissions(
                currentActivity,permissions, REQUEST_CODE_PERMISSIONS)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startChooser()
            } else {
                Toast.makeText(context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun startChooser(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Фотография из галереии"),
            PHOTO_FROM_GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            when(requestCode){
                PHOTO_FROM_GALLERY -> {
                    when(resultCode){
                        Activity.RESULT_OK -> {
                            val uri = data?.data
                            val projection = arrayOf(MediaStore.Images.Media.DATA)

                            val cursor =
                                currentActivity.contentResolver.query(uri!!, projection, null, null, null)
                            cursor?.moveToFirst()

                            val columnIndex = cursor?.getColumnIndex(projection[0])
                            val picturePath = cursor?.getString(columnIndex!!) // returns null
                            cursor?.close()
                            when(currentType){
                                BAR_CODE -> {
                                    val currentUri = FileUtils.getUriFromString(picturePath)
                                    currentUri?.let {
                                        updatePhoto(it)
                                        var image: InputImage? = null
                                        try {
                                            image = InputImage.fromFilePath(currentActivity, it)
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                        }
                                        image?.let { im ->
                                                scanBarcodes(im)
                                        }
                                    }
                                }
                                PHOTO_CAMERA -> {

                                }
                                PHOTO_LIBRARY -> {
                                    updatePhoto(FileUtils.getUriFromString(picturePath))
                                }
                            }


                        }
                    }
                }
            }
        }catch (e: Exception){
            displayMessage("Упс, что то пошло не так :(")
        }
    }

    private fun scanBarcodes(image: InputImage) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()

        val scanner = BarcodeScanning.getClient()
        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                // [START_EXCLUDE]
                // [START get_barcodes]
                for (barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints

                    val rawValue = barcode.rawValue
                    Log.wtf("BARCODE","bounds = $bounds ,corners = $corners")
                    Log.wtf("BARCODE","rawValue = $rawValue")
                    val valueType = barcode.valueType

                    if(rawValue.isNotEmpty()){
                        findNavController().navigate(R.id.action_photoChooserFragment_to_itemDetailFragment)
                    }
                    // See API reference for complete list of supported types
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

                }
            }
            .addOnFailureListener {
                displayMessage("Упс, что то пошло не так :(,Попробуйте еще раз")
            }
    }

    override fun updatePhoto(path: Uri?) {
        path?.let {
            Glide.with(this).load(path).into(this.image_view_fragment_photo_chooser)
        }?:run{
            displayMessage("Упс, что то пошло не так :(")
        }
    }

    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

    }

    override fun disposeRequests() {

    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {

    }

    override fun hideProgress() {

    }

}