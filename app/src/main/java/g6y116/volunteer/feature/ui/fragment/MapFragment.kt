package g6y116.volunteer.feature.ui.fragment

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import g6y116.volunteer.R
import g6y116.volunteer.base.abstracts.BaseFragment
import g6y116.volunteer.base.utils.onClick
import g6y116.volunteer.databinding.FragmentMapBinding
import g6y116.volunteer.feature.ui.activity.DetailActivity
import g6y116.volunteer.feature.ui.viewmodel.DetailViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {

    override val viewModel: DetailViewModel by activityViewModels()
    private val mapView by lazy { MapView(requireActivity()) }

    override fun onResume() {
        super.onResume()
        activity?.let { detailActivity ->
            (detailActivity as DetailActivity).setToolbarTitle(getText(R.string.toolbar_map).toString())
            (detailActivity as DetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun initView() {
        binding.viewmodel = viewModel
    }

    override fun setOnclick() {
        binding.btn.onClick {
            viewModel.coordinate.value?.let {
                if (it.x.isNotEmpty() && it.y.isNotEmpty()) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                        "kakaomap://look?p=${it.y},${it.x}"
                    )))
                }
            }
        }

        binding.fab.onClick {
            findNavController().popBackStack()
            findNavController().navigate(R.id.infoFragment)
        }
    }

    override fun setObserver() {
        viewModel.coordinate.observe(viewLifecycleOwner) {
            if (it != null) {
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble()), true)
                mapView.setZoomLevel(2, true)

                val marker = MapPOIItem().apply {
                    itemName = getString(R.string.place)
                    tag = 0
                    mapPoint = MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble())
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
                mapView.addPOIItem(marker)

                if (mapView.parent != null) {
                    (mapView.parent as ViewGroup).removeView(mapView)
                }
                binding.mapL.addView(mapView) // map
            }
        }
    }
}