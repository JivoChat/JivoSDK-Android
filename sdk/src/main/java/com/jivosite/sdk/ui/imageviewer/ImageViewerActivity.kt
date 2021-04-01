package com.jivosite.sdk.ui.imageviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import com.github.chrisbanes.photoview.PhotoView
import com.jivosite.sdk.R
import com.jivosite.sdk.support.ext.Intents
import com.jivosite.sdk.support.utils.copyToClipboard


/**
 * Created on 27.01.2021.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ImageViewerActivity : AppCompatActivity(R.layout.activity_image_viewer) {

    companion object {
        private const val EXTRA_URL = "url"
        private const val EXTRA_NAME = "name"

        @JvmStatic
        fun show(context: Context, url: String, name: String) {
            val intent = Intent(context, ImageViewerActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_NAME, name)
            }
            context.startActivity(intent)
        }
    }

    private val viewModel: ImageViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.extras?.getString(EXTRA_URL)
        val name = intent.extras?.getString(EXTRA_NAME)
        val photoView = findViewById<PhotoView>(R.id.imageView)

        viewModel.url = url

        setupToolbar(name)

        photoView.load(url)
    }

    private fun setupToolbar(name: String?) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = name
        }
        toolbar.run {
            setNavigationOnClickListener {
                onBackPressed()
            }
            toolbar.inflateMenu(R.menu.image_viewer_menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.image_viewer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_download -> {
                Intents.downloadFile(this, viewModel.url)
                true
            }
            R.id.action_copy -> {
                copyToClipboard(viewModel.url)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun copyToClipboard(text: String?) {
        if (text.copyToClipboard(this)) {
            Toast.makeText(this, R.string.toast_copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }
}