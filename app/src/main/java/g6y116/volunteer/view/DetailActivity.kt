package g6y116.volunteer.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.view.menu.ActionMenuItemView
import dagger.hilt.android.AndroidEntryPoint
import g6y116.volunteer.R
import g6y116.volunteer.databinding.ActivityDetailBinding
import g6y116.volunteer.onClick
import g6y116.volunteer.toast
import g6y116.volunteer.viewmodel.DetailViewModel

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.title = getString(R.string.toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.pID = intent?.getStringExtra("pID").toString()
        viewModel.url = intent?.getStringExtra("url").toString()
        viewModel.from = intent?.getStringExtra("from").toString()

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.getVolunteer(viewModel.pID)
        viewModel.addRead(viewModel.pID)

        setOnclick()
        setObserver()
    }

    private fun setOnclick() {
        binding.btn.onClick {
            if (viewModel.url.isNotEmpty())
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url)))
        }
    }

    private fun setObserver() {
        viewModel.isBookmarkLiveData.observe(this) {
            val bookMarkIcon = binding.toolbar.root.findViewById<ActionMenuItemView>(R.id.book_mark)
            val bookMarkBorderIcon = binding.toolbar.root.findViewById<ActionMenuItemView>(R.id.book_mark_border)
            bookMarkIcon.visibility = if (it) View.VISIBLE else View.GONE
            bookMarkBorderIcon.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewModel.errorLiveData.observe(this) {
            toast(this, getString(R.string.msg_volunteer_deleted))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish() }
            R.id.book_mark -> { viewModel.clickBookMark() }
            R.id.book_mark_border -> { viewModel.clickBookMark() }
        }
        return super.onOptionsItemSelected(item)
    }
}