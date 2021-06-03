package work.kcs_labo.sample_image_viewer

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import work.kcs_labo.sample_image_viewer.databinding.DetailImageViewPagerFragmentBinding
import kotlin.math.max
import kotlin.math.min

/**
 * Created by hide1 on 2021/05/23.
 * ProjectName SampleImageViewer
 */

class DetailImageViewPagerFragment : Fragment(R.layout.detail_image_view_pager_fragment) {

  private val viewModel: MainActivityViewModel by activityViewModels()

  private val binding by viewBinding(DetailImageViewPagerFragmentBinding::bind)

  private val callback = object : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
      super.onPageSelected(position)
      viewModel.onPageSelected(position)

      binding.viewPager.adapter?.let {
        childFragmentManager.findFragmentByTag("f$position")
      }?.let { fragment ->
        if (fragment is OnInitFieldListener) {
          fragment.onInitField()
        }
      }

    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.viewPager.also {
      it.adapter = DetailImageAdapter(this, viewModel)
      it.offscreenPageLimit = 1
      it.isUserInputEnabled = false
      it.setCurrentItem(viewModel.getCurrentItem(), false)
      it.registerOnPageChangeCallback(callback)
    }

    viewModel.onFlingLiveEvent.observeSingle(viewLifecycleOwner)
    {
      val current = binding.viewPager.currentItem
      when (it) {
        TurnPage.TO_PREVIOUS -> {
          binding.viewPager.post {
            binding.viewPager.currentItem = max(0, current - 1)
          }
        }
        TurnPage.TO_NEXT -> {
          binding.viewPager.post {
            binding.viewPager.currentItem = min(viewModel.getCount(), current + 1)
          }
        }
        else -> {
        }
      }
    }

    requireActivity().onBackPressedDispatcher.addCallback {
      viewModel.onBackPressed()
    }
  }

  override fun onResume() {
    super.onResume()
    val currentPosition = viewModel.getCurrentItem()
    binding.root.post {
      binding.viewPager.setCurrentItem(currentPosition, false)
    }
  }

  override fun onStop() {
    super.onStop()
    println("onStop")
    binding.viewPager.unregisterOnPageChangeCallback(callback)
  }

  companion object {
    fun getInstance(bundle: Bundle? = null): DetailImageViewPagerFragment {
      val fragment = DetailImageViewPagerFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}