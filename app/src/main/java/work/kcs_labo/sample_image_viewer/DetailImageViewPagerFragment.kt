package work.kcs_labo.sample_image_viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import work.kcs_labo.sample_image_viewer.databinding.DetailImageViewPagerFragmentBinding
import kotlin.math.max
import kotlin.math.min

/**
 * Created by hide1 on 2021/05/23.
 * ProjectName SampleImageViewer
 */

class DetailImageViewPagerFragment : Fragment() {

  private val viewModel: MainActivityViewModel by activityViewModels()

  private lateinit var binding: DetailImageViewPagerFragmentBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback {
      viewModel.onBackPressed()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("position", binding.viewPager.currentItem)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DetailImageViewPagerFragmentBinding.inflate(inflater, container, false)

    binding.viewPager.adapter = DetailImageAdapter(requireActivity(), viewModel)

    binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        val savedPosition = savedInstanceState?.getInt("position") ?: -1
        binding.viewPager.adapter?.let { adapter ->
          if (savedPosition != -1) {
            requireActivity().supportFragmentManager.findFragmentByTag("f$savedPosition")
          } else {
            requireActivity().supportFragmentManager
              .findFragmentByTag("f${adapter.getItemId(position)}")
          }?.let { fragment ->
            if (fragment is DetailImageFragment) {
              fragment.resetScale()
            }
          }
        }
      }
    })

    val savedPosition = savedInstanceState?.getInt("position") ?: -1

    if (savedPosition == -1) {
      val initPosition = arguments?.getInt("position") ?: 0
      binding.viewPager.setCurrentItem(initPosition, false)
    } else {
      binding.viewPager.post {
        binding.viewPager.currentItem = savedPosition
      }
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

    binding.viewPager.isUserInputEnabled = false

    return binding.root
  }

  companion object {
    fun getInstance(bundle: Bundle? = null): DetailImageViewPagerFragment {
      val fragment = DetailImageViewPagerFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}