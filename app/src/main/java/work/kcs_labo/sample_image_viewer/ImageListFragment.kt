package work.kcs_labo.sample_image_viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import work.kcs_labo.sample_image_viewer.databinding.ImageListFragmentBinding

/**
 * Created by hide1 on 2021/05/21.
 * ProjectName SampleImageViewer
 */

class ImageListFragment : Fragment() {

  private val viewModel: MainActivityViewModel by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val binding = ImageListFragmentBinding.inflate(inflater, container, false)
    binding.imageList.adapter = ImageListAdapter(requireContext(), viewModel, viewLifecycleOwner)
    binding.imageList.layoutManager = GridLayoutManager(requireActivity(), 3)

    return binding.root
  }

  companion object {
    fun getInstance(bundle: Bundle? = null): ImageListFragment {
      val fragment = ImageListFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}