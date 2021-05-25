package work.kcs_labo.sample_image_viewer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean

fun <T> singleLiveEvent() : MutableLiveData<T> {
  return MutableLiveData<T>().also { it.value = null }
}

fun <T> LiveData<T>.observeSingle(owner: LifecycleOwner, observer: ((T?) -> Unit)) {
  val firstIgnore = AtomicBoolean(true)
  this.observe(owner) {
    if (firstIgnore.getAndSet(false)) return@observe
    observer(it)
  }
}