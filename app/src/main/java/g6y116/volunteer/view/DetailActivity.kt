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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import g6y116.volunteer.R
import g6y116.volunteer.databinding.ActivityDetailBinding
import g6y116.volunteer.log
import g6y116.volunteer.onClick
import g6y116.volunteer.toast
import g6y116.volunteer.viewmodel.DetailViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModels()
    private val mapView by lazy { MapView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.tb.root)
        supportActionBar?.title = getString(R.string.toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.pID = intent?.getStringExtra("pID").toString()
        viewModel.url = intent?.getStringExtra("url").toString()
        viewModel.from = intent?.getStringExtra("from").toString()

//        binding.lifecycleOwner = this
//        binding.viewmodel = viewModel
//
        viewModel.getVolunteer(viewModel.pID)
        viewModel.addRead(viewModel.pID)
//
//        setOnclick()
//        setObserver()
    }

    private fun setOnclick() {
//        binding.btn.onClick {
//            if (viewModel.url.isNotEmpty())
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url)))
//        }
    }

    private fun setObserver() {
        viewModel.isBookmarkLiveData.observe(this) {
            val bookMarkIcon = binding.tb.root.findViewById<ActionMenuItemView>(R.id.book_mark)
            val bookMarkBorderIcon = binding.tb.root.findViewById<ActionMenuItemView>(R.id.book_mark_border)
            bookMarkIcon.visibility = if (it) View.VISIBLE else View.GONE
            bookMarkBorderIcon.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewModel.errorLiveData.observe(this) {
            toast(this, getString(R.string.msg_volunteer_deleted))
            finish()
        }

//        viewModel.coordinateLiveData.observe(this) {
//            if (it != null) {
//                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble()), true)
//                mapView.setZoomLevel(2, true)
//
//                val marker = MapPOIItem().apply {
//                    itemName = "여기"
//                    tag = 0
//                    mapPoint = MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble())
//                    markerType = MapPOIItem.MarkerType.BluePin
//                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
//
//                }
//                mapView.addPOIItem(marker)
//
//                binding.mapL.addView(mapView) // map
//            }
//        }
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