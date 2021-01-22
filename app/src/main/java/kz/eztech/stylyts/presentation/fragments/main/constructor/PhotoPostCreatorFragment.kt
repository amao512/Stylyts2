package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo_post_creator.*
import kotlinx.android.synthetic.main.item_photo_library.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.PhotoLibraryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.ViewUtils

class PhotoPostCreatorFragment : BaseFragment<MainActivity>(), EmptyContract.View,
    LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var photoAdapter: PhotoLibraryAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_post_creator
    }

    override fun getContractView(): BaseView {
       return this
    }

    override fun customizeActionBar() {

    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        photoAdapter = PhotoLibraryAdapter()
        val numbers = ViewUtils.calculateNoOfColumns(currentActivity, 100f)
        recycler_view_fragment_photo_post_creator_list.layoutManager = GridLayoutManager(currentActivity, numbers)
        recycler_view_fragment_photo_post_creator_list.adapter = photoAdapter
        checkWritePermission()
    }

    private fun checkWritePermission() {
        if (ContextCompat.checkSelfPermission(
                currentActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
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
        Log.wtf("onLoadFinished", "Loaded")
        val listOfAllImages = ArrayList<String>()
        data?.let {

            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (it.moveToNext()) {
                val id = it.getLong(columnIndexData)

                //listOfAllImages.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id))
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
                listOfAllImages.add(it.getString(columnIndexData));
            }
        }
        if(listOfAllImages.isNotEmpty()){
            Log.wtf("List", listOfAllImages.toString())
            photoAdapter.updateList(listOfAllImages)
            Glide.with(currentActivity).load(listOfAllImages[0]).into(image_view_fragment_photo_post_creator)
        }else{
            Log.wtf("List", "Empty list")
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {    }

    override fun initializeListeners() {

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
}