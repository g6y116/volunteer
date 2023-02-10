package g6y116.volunteer.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import g6y116.volunteer.R
import g6y116.volunteer.databinding.ActivityDetailBinding
import g6y116.volunteer.viewmodel.DetailViewModel

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.pID = intent?.getStringExtra("pID").toString()
        viewModel.url = intent?.getStringExtra("url").toString()
        viewModel.from = intent?.getStringExtra("from").toString()

        viewModel.getVolunteer(viewModel.pID)

        viewModel.volunteer.observe(this) {
            setView()
        }

        viewModel.isBookMark.observe(this) {
            if (it) binding.ivBookMark.setBackgroundResource(R.drawable.ic_bookmark_24)
            else binding.ivBookMark.setBackgroundResource(R.drawable.ic_bookmark_border_24)
        }
    }

    private fun setView() {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.ivBookMark.setOnClickListener {
            viewModel.clickBookMark()
        }

        binding.btn.setOnClickListener {
            if (viewModel.url.isNotEmpty()) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url)))
            }
        }
    }
}