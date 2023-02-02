package g6y116.volunteer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import g6y116.volunteer.repository.Volunteer
import g6y116.volunteer.repository.VolunteerRepository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: VolunteerRepository) : ViewModel() {

    val volunteer = MutableLiveData<Volunteer>()
    val url = MutableLiveData<String>()
    val from = MutableLiveData<String>()
}