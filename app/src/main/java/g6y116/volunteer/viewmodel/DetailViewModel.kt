package g6y116.volunteer.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.repository.VolunteerRepository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

}