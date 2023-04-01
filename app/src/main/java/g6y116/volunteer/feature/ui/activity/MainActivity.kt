package g6y116.volunteer.feature.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import g6y116.volunteer.R
import g6y116.volunteer.base.abstracts.BaseActivity
import g6y116.volunteer.base.utils.toast
import g6y116.volunteer.databinding.ActivityMainBinding
import g6y116.volunteer.feature.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.tb)
        setNavigation(R.id.nh, binding.bn)

        viewModel.appOption.observe(this) {
            viewModel.applyTheme(it.theme)
            viewModel.applyLanguage(it.language)
        }
    }

    private var waitTime = 0L
    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >= 1000 ) {
            waitTime = System.currentTimeMillis()
            toast(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.")
        } else {
            finish()
        }
    }
}