package work.kcs_labo.sample_image_viewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import work.kcs_labo.sample_image_viewer.databinding.SquareImageBinding

/**
 * Created by hide1 on 2021/05/21.
 * ProjectName SampleImageViewer
 * ViewModel has Item List.
 */

class ImageListAdapter(
  private val context: Context,
  private val viewModel: MainActivityViewModel,
  private val parentLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ImageListAdapter.ContentViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
    ContentViewHolder(
      LayoutInflater.from(context).inflate(
        getLayoutRes(viewType),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
    (holder.binding as SquareImageBinding).also { binding ->
      binding.lifecycleOwner = parentLifecycleOwner

      viewModel.getItem(position)?.let { item ->
        binding.squareImageView.setImageDrawable(ContextCompat.getDrawable(context, item))
      }

      binding.root.setOnClickListener {
        viewModel.onShowDetail(position)
      }
    }
    holder.binding.executePendingBindings()
  }

  override fun getItemViewType(position: Int): Int {
    return VIEW_TYPE_ITEM
  }

  override fun getItemCount(): Int {
    return viewModel.getCount()
  }

  private fun getLayoutRes(viewType: Int) =
    when (viewType) {
      VIEW_TYPE_ITEM -> R.layout.square_image /* R.layout.xxx_item */
      else -> throw IllegalArgumentException("Unknown viewType : $viewType")
    }

  companion object {
    const val VIEW_TYPE_ITEM = 1
  }

  class ContentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val binding: ViewDataBinding = DataBindingUtil.bind(v)!!
  }
}