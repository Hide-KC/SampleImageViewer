package work.kcs_labo.sample_image_viewer

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import work.kcs_labo.sample_image_viewer.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

  private val viewModel: MainActivityViewModel by viewModels {
    // AnyViewModelFactory class
    ViewModelProvider.NewInstanceFactory()
  }

  override fun onStop() {
    super.onStop()
    viewModel.detailImageVisible = detailImageFragmentVisibility()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbar)

    val transaction = supportFragmentManager.beginTransaction()

    if (viewModel.detailImageVisible) {
      val target = supportFragmentManager.findFragmentByTag("TAG_DETAIL")
        ?: DetailImageViewPagerFragment.getInstance(Bundle().also { b ->
          b.putInt(
            "position",
            viewModel.getCurrentItem()
          )
        })

      transaction.replace(R.id.contents, target, "TAG_DETAIL")

    } else {
      val target = supportFragmentManager.findFragmentByTag("TAG_LIST")
        ?: ImageListFragment.getInstance()

      transaction.replace(R.id.contents, target, "TAG_LIST")

    }

    transaction.commit()

    viewModel.onBackPressedLiveEvent.observeSingle(this) {
      println("onBackPressed")

      val imageListFragment =
        supportFragmentManager.findFragmentByTag("TAG_LIST")
          ?: ImageListFragment.getInstance()

      if (!imageListFragment.isVisible) {
        val transaction1 = supportFragmentManager.beginTransaction()
        transaction1.replace(R.id.contents, imageListFragment, "TAG_LIST")
        transaction1.commit()
        viewModel.onFling(TurnPage.NONE)

        binding.root.transitionToStart()
      } else {
        finish()
      }
    }

    viewModel.onShowDetailLiveEvent.observeSingle(this) { position ->
      println("onShowDetail")

      val detailImageViewPagerFragment =
        supportFragmentManager.findFragmentByTag("TAG_DETAIL")
          ?: DetailImageViewPagerFragment
            .getInstance(Bundle().also { b -> b.putInt("position", position ?: 0) })

      if (!detailImageViewPagerFragment.isVisible) {
        val transaction2 = supportFragmentManager.beginTransaction()
        transaction2.replace(R.id.contents, detailImageViewPagerFragment, "TAG_DETAIL")
        transaction2.commit()

        binding.root.transitionToEnd()
      }
    }
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    viewModel.updateMotion(event)
    return true
  }

  private fun detailImageFragmentVisibility() =
    supportFragmentManager.findFragmentByTag("TAG_DETAIL")?.isVisible ?: false

}