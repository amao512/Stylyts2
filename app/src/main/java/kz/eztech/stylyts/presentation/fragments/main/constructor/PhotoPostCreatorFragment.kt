package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_photo_chooser.*
import kotlinx.android.synthetic.main.fragment_photo_post_creator.*
import kotlinx.android.synthetic.main.item_photo_library.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.PhotoLibraryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.ViewUtils
import java.io.File

class PhotoPostCreatorFragment : BaseFragment<MainActivity>(), EmptyContract.View,
    LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,UniversalViewClickListener {
    
    private lateinit var photoAdapter: PhotoLibraryAdapter
    private var photoUri: Uri? = null
    private var mode = -1
    private val listOfAllImages = ArrayList<String>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_post_creator
    }

    override fun getContractView(): BaseView {
       return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_fragment_photo_post_creator){
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            image_button_right_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Далее"
            text_view_toolbar_right_text.setOnClickListener {
                photoUri?.let {
                    val bundle = Bundle()
                    bundle.putParcelable("uri",photoUri)
                    findNavController().navigate(R.id.action_createCollectionFragment_to_photoChooserFragment,bundle)
                }
            }
            elevation = 0f
            customizeActionToolBar(this,"Создать Публикацию")
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {
        arguments?.let {
            if(it.containsKey("uri")){
                photoUri = it.getParcelable("uri")
            }
            if(it.containsKey("mode")){
                mode = it.getInt("mode")
            }
        }
        
    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        when(mode){
            0 -> {
                switchToCameraPicture()
            }
            else -> {
                checkWritePermission()
                switchFromCameraPicture()
            }
        }
      
        
    }
    
    private fun switchToCameraPicture(){
        recycler_view_fragment_photo_post_creator_list.visibility = View.GONE
        photoUri?.let {
            Glide.with(currentActivity).load(it.path).into(image_view_fragment_photo_post_creator)
        }
        
    }
    
    private fun switchFromCameraPicture(){
        photoAdapter = PhotoLibraryAdapter()
        photoAdapter.setOnClickListener(this)
        val numbers = ViewUtils.calculateNoOfColumns(currentActivity, 100f)
        recycler_view_fragment_photo_post_creator_list.layoutManager = GridLayoutManager(currentActivity, numbers)
        recycler_view_fragment_photo_post_creator_list.adapter = photoAdapter
        photoAdapter.updateList(listOfAllImages)
    }
    
    override fun onViewClicked(view: View, position: Int, item: Any?) {
        item as String
        photoUri = Uri.fromFile(File(item))
        Glide.with(currentActivity).load(photoUri).into(image_view_fragment_photo_post_creator)
    }
    
    private fun checkWritePermission() {
        if (ContextCompat.checkSelfPermission(
                currentActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(
                        currentActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED)  {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                            currentActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )) {

            } else {
                ActivityCompat.requestPermissions(
                    currentActivity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    2
                );
            }
        } else {
            LoaderManager.getInstance(this).initLoader(21, null, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    //resume tasks needing this permission
                    LoaderManager.getInstance(this).initLoader(21, null, this)
                }
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media._ID
        )
        val selection: String? = null     //Selection criteria
        val selectionArgs = arrayOf<String>()  //Selection criteria
        val sortOrder = MediaStore.Images.Media._ID;

        return CursorLoader(
            currentActivity,
            uri,
            projection,
            null,
            null,
            sortOrder
        )

        Log.wtf("onCreateLoader", "Loaded")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val listOfImages = ArrayList<String>()
        data?.let {

            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (it.moveToNext()) {
                listOfImages.add(it.getString(columnIndexData));
            }
        }
        if(listOfImages.isNotEmpty()){
            listOfAllImages.clear()
            listOfAllImages.addAll(listOfImages)
        }
    
        if(listOfAllImages.isNotEmpty()){
            photoUri = Uri.fromFile(File(listOfAllImages[0]))
            Glide.with(currentActivity).load(listOfAllImages[0]).into(image_view_fragment_photo_post_creator)
            photoAdapter.updateList(listOfAllImages)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {    }

    override fun initializeListeners() {
        frame_layout_fragment_photo_post_creator_camera.setOnClickListener(this)
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
            R.id.frame_layout_fragment_photo_post_creator_camera -> {
                findNavController().navigate(R.id.action_createCollectionFragment_to_cameraFragment)
            }
        }
    }
}