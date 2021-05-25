package work.kcs_labo.sample_image_viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailImageAdapter(
  private val fragmentActivity: FragmentActivity,
  private val viewModel: MainActivityViewModel
) : FragmentStateAdapter(fragmentActivity) {
  override fun getItemCount(): Int {
    return viewModel.getCount()
  }

  override fun createFragment(position: Int): Fragment {
    return DetailImageFragment.getInstance(Bundle().also { it.putInt("position", position) })
  }
}