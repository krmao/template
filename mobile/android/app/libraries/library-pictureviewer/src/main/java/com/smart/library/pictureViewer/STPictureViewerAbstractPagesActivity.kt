package com.smart.library.pictureviewer

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.smart.library.base.STBaseActivity
import com.smart.library.pictureviewer.R

abstract class STPictureViewerAbstractPagesActivity protected constructor(private val title: Int, private val layout: Int, private val notes: List<STPictureViewerPageModel>) : STBaseActivity() {
    protected var page = 0
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        val actionBar = actionBar
        if (actionBar != null) {
            actionBar.title = getString(title)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        findViewById<View>(R.id.next).setOnClickListener { next() }
        findViewById<View>(R.id.previous).setOnClickListener { previous() }
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_PAGE)) {
            page = savedInstanceState.getInt(BUNDLE_PAGE)
        }
    }

    override fun onResume() {
        super.onResume()
        updateNotes()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_PAGE, page)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private operator fun next() {
        page++
        updateNotes()
    }

    private fun previous() {
        page--
        updateNotes()
    }

    private fun updateNotes() {
        if (page > notes.size - 1) {
            return
        }
        val actionBar = actionBar
        actionBar?.setSubtitle(notes[page].subtitle)
        (findViewById<View>(R.id.note) as TextView).setText(notes[page].text)
        findViewById<View>(R.id.next).visibility = if (page >= notes.size - 1) View.INVISIBLE else View.VISIBLE
        findViewById<View>(R.id.previous).visibility = if (page <= 0) View.INVISIBLE else View.VISIBLE
        onPageChanged(page)
    }

    protected open fun onPageChanged(page: Int) {}

    companion object {
        private const val BUNDLE_PAGE = "page"
    }

}