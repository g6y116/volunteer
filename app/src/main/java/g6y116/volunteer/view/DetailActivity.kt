package g6y116.volunteer.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import g6y116.volunteer.databinding.ActivityDetailBinding
import g6y116.volunteer.repository.Volunteer
import g6y116.volunteer.viewmodel.DetailViewModel

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setView()
    }

    private fun setView() {
        viewModel.volunteer.value = intent?.getSerializableExtra("volunteer") as Volunteer
        viewModel.url.value = intent?.getStringExtra("url").toString()
        viewModel.from.value = intent?.getStringExtra("from").toString()

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.btn.setOnClickListener {
            viewModel.url.value?.let {
                Log.d("성준", "${viewModel.volunteer.value?.title} / ${viewModel.volunteer.value?.pID}")
                Log.d("성준", "${viewModel.url.value}")
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }
    }
}