package g6y116.volunteer.feature.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import g6y116.volunteer.R
import g6y116.volunteer.base.abstracts.BaseActivity
import g6y116.volunteer.databinding.ActivityDetailBinding
import g6y116.volunteer.feature.data.model.DetailUiState
import g6y116.volunteer.feature.ui.viewmodel.DetailViewModel

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    override val viewModel: DetailViewModel by viewModels()
    private var toolbarMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.tb)
        intent?.getSerializableExtra("item")?.let {
            viewModel.setDetailUiState(it as DetailUiState)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toolbarMenu = menu
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        intent?.getSerializableExtra("item")?.let {
            if((it as DetailUiState).isBookmark) {
                menu?.findItem(R.id.book_mark)?.isVisible = true
                menu?.findItem(R.id.book_mark_border)?.isVisible = false
            } else {
                menu?.findItem(R.id.book_mark)?.isVisible = false
                menu?.findItem(R.id.book_mark_border)?.isVisible = true
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish() }
            R.id.book_mark -> {
                viewModel.clickBookMark()
                toolbarMenu?.findItem(R.id.book_mark)?.isVisible = false
                toolbarMenu?.findItem(R.id.book_mark_border)?.isVisible = true
            }
            R.id.book_mark_border -> {
                viewModel.clickBookMark()
                toolbarMenu?.findItem(R.id.book_mark)?.isVisible = true
                toolbarMenu?.findItem(R.id.book_mark_border)?.isVisible = false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}