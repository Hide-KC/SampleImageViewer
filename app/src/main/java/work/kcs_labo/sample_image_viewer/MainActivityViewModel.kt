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
      R.drawable.sample_9,
      R.drawable.sample_10,
      R.drawable.sample_11,
      R.drawable.sample_12,
      R.drawable.sample_13,
      R.drawable.sample_14,
      R.drawable.sample_15,
      R.drawable.sample_16,
      R.drawable.sample_17,
      R.drawable.sample_18,
      R.drawable.sample_19,
      R.drawable.sample_20
    )
  )
  val itemIdListLiveData: LiveData<List<Int>> = _itemIdListLiveData

  private val _onBackPressedLiveData = MutableLiveData(Unit)
  val onBackPressedLiveData: LiveData<Unit> = _onBackPressedLiveData

  private val _motionEventLiveData = MutableLiveData<MotionEvent>()
  val motionEventLiveData: LiveData<MotionEvent> = _motionEventLiveData

  private val _onShowDetailLiveData = MutableLiveData<Int>()
  val onShowDetailLiveData: LiveData<Int> = _onShowDetailLiveData

  private val _onFlingLiveData = MutableLiveData(TurnPage.NONE)
  val onFlingLiveData: LiveData<TurnPage> = _onFlingLiveData

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

  fun onFling(turnPage: TurnPage) {
    _onFlingLiveData.value = turnPage
  }
}