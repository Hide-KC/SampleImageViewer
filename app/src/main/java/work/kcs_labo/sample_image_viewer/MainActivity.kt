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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbar)

    val transaction = supportFragmentManager.beginTransaction()

    transaction.replace(R.id.contents, ImageListFragment.getInstance(), "TAG_LIST")

    transaction.commit()

    viewModel.onBackPressedLiveData.observe(this) {
      println("onBackPressed")

      val imageListFragment =
        supportFragmentManager.findFragmentByTag("TAG_LIST") ?: ImageListFragment.getInstance()

      if (!imageListFragment.isVisible) {
        val transaction1 = supportFragmentManager.beginTransaction()
        transaction1.replace(R.id.contents, imageListFragment)
        transaction1.commit()

        binding.root.transitionToStart()
      } else {
        finish()
      }
    }

    viewModel.onShowDetailLiveData.observe(this) { position ->
      println("onShowDetail")

      val detailFragment =
        DetailImageFragment.getInstance(Bundle().also { b -> b.putInt("position", position) })
      if (!detailFragment.isVisible) {
        val transaction2 = supportFragmentManager.beginTransaction()
        transaction2.replace(R.id.contents, detailFragment)
        transaction2.commit()

        binding.root.transitionToEnd()
      }
    }
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    viewModel.updateMotion(event)
    return super.onTouchEvent(event)
  }
}