package g6y116.volunteer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import g6y116.volunteer.R
import g6y116.volunteer.databinding.FragmentMapBinding
import g6y116.volunteer.onClick
import g6y116.volunteer.viewmodel.DetailViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment() {

    private val binding by lazy { FragmentMapBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by activityViewModels()
    private val mapView by lazy { MapView(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        setOnclick()
        setObserver()
    }

    private fun setOnclick() {
        binding.fab.onClick {
            findNavController().navigate(R.id.infoFragment)
        }
    }

    private fun setObserver() {
        viewModel.coordinateLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble()), true)
                mapView.setZoomLevel(2, true)

                val marker = MapPOIItem().apply {
                    itemName = "여기"
                    tag = 0
                    mapPoint = MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble())
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin

                }
                mapView.addPOIItem(marker)

                binding.mapL.addView(mapView) // map
            }
        }
    }
}