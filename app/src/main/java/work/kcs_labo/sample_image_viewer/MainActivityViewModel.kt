package work.kcs_labo.sample_image_viewer

import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

  private val _itemIdListLiveData = MutableLiveData(
    listOf(
      R.drawable.sample_1,
      R.drawable.sample_2,
      R.drawable.sample_3,
      R.drawable.sample_4,
      R.drawable.sample_5,
      R.drawable.sample_6,
      R.drawable.sample_7,
      R.drawable.sample_8,
      R.drawable.sample_9
    )
  )
  val itemIdListLiveData: LiveData<List<Int>> = _itemIdListLiveData

  private val _onBackPressedLiveEvent = singleLiveEvent<Unit>()
  val onBackPressedLiveEvent: LiveData<Unit> = _onBackPressedLiveEvent

  private val _onMotionEventLiveEvent = singleLiveEvent<MotionEvent>()
  val onMotionEventLiveEvent: LiveData<MotionEvent> = _onMotionEventLiveEvent

  private val _onShowDetailLiveEvent = singleLiveEvent<Int>()
  val onShowDetailLiveEvent: LiveData<Int> = _onShowDetailLiveEvent

  private val _onFlingLiveEvent = singleLiveEvent<TurnPage>()
  val onFlingLiveEvent: LiveData<TurnPage> = _onFlingLiveEvent

  var detailImageVisible = false

  fun getCount() = itemIdListLiveData.value?.size ?: 0

  fun getItem(position: Int) = itemIdListLiveData.value?.get(position)

  fun getCurrentItem() = _onShowDetailLiveEvent.value ?: 0

  fun onBackPressed() {
    _onBackPressedLiveEvent.value = Unit
  }

  fun onShowDetail(position: Int) {
    _onShowDetailLiveEvent.value = position
  }

  fun updateMotion(motionEvent: MotionEvent?) {
    motionEvent?.let {
      _onMotionEventLiveEvent.value = it
    }
  }

  fun onFling(turnPage: TurnPage) {
    _onFlingLiveEvent.value = turnPage
  }
}