package work.kcs_labo.sample_image_viewer

import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

  private val _itemIdListLiveData = MutableLiveData(
    listOf(
      R.drawable.calligraphy,
      R.drawable.sample_icon,
      R.drawable.sample_ookii,
      R.drawable.sample_screenshot,
      R.drawable.sample_tatenaga,
      R.drawable.sample_yokonaga,
      R.drawable.calligraphy,
      R.drawable.sample_icon,
      R.drawable.sample_ookii,
      R.drawable.sample_screenshot,
      R.drawable.sample_tatenaga,
      R.drawable.sample_yokonaga,
      R.drawable.calligraphy,
      R.drawable.sample_icon,
      R.drawable.sample_ookii,
      R.drawable.sample_screenshot,
      R.drawable.sample_tatenaga,
      R.drawable.sample_yokonaga
    )
  )
  val itemIdListLiveData: LiveData<List<Int>> = _itemIdListLiveData

  private val _onBackPressedLiveData = MutableLiveData(Unit)
  val onBackPressedLiveData: LiveData<Unit> = _onBackPressedLiveData

  private val _motionEventLiveData = MutableLiveData<MotionEvent>()
  val motionEventLiveData: LiveData<MotionEvent> = _motionEventLiveData

  private val _onShowDetailLiveData = MutableLiveData<Int>()
  val onShowDetailLiveData: LiveData<Int> = _onShowDetailLiveData

  fun getCount() = itemIdListLiveData.value?.size ?: 0

  fun getItem(position: Int) = itemIdListLiveData.value?.get(position)

  fun onBackPressed() {
    _onBackPressedLiveData.value = Unit
  }

  fun onShowDetail(position: Int) {
    _onShowDetailLiveData.value = position
  }

  fun updateMotion(motionEvent: MotionEvent?) {
    motionEvent?.let {
      _motionEventLiveData.value = it
    }
  }
}