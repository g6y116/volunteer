package g6y116.volunteer.base.abstracts

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

// https://velog.io/@changhee09/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-BaseActivity-BaseFragment

abstract class BaseActivity<B: ViewDataBinding>(@LayoutRes val layoutRes: Int) : AppCompatActivity() {

    lateinit var binding: B
    abstract val viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    fun setNavigation(navHostFragmentID: Int, bottomNavigationView: BottomNavigationView) {
        val navHostFragment = supportFragmentManager.findFragmentById(navHostFragmentID) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    fun setToolbarTitle(text: String) {
        supportActionBar?.title = text
    }
}