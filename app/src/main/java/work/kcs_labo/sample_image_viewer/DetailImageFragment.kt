package work.kcs_labo.sample_image_viewer

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import work.kcs_labo.sample_image_viewer.databinding.DetailImageFragmentBinding
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Created by hide1 on 2021/05/18.
 * ProjectName SampleImageViewer
 */

class DetailImageFragment : Fragment() {

  private val viewModel: MainActivityViewModel by activityViewModels()

  private lateinit var _scaleGestureDetector: ScaleGestureDetector

  private lateinit var _gestureDetector: GestureDetector

  private lateinit var binding: DetailImageFragmentBinding

  // onScaleにより変化
  private var _drawableWidth = 0F
  private var _drawableHeight = 0F

  // 固定値
  private var _viewPortWidth = 0F
  private var _viewPortHeight = 0F

  // 固定値
  private var _defaultDrawableWidth = 0F
  private var _defaultDrawableHeight = 0F

  private var _scaleFactor = 1F

  private var _translationX = 0F

  private var _translationY = 0F

  private var _enableTurnPage: TurnPage = TurnPage.NONE

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.detail_image_fragment,
      container,
      false
    )

    val position = arguments?.getInt("position") ?: 0
    viewModel.getItem(position)?.let {
      binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, it, null))
    }

    initValues()

    viewModel.motionEventLiveData.observe(viewLifecycleOwner) {
      onTouch(it)
    }

    return binding.root
  }

  fun resetScale() {
    _scaleFactor = 1F

    // onScaleが発生したときと同じ流れ
    binding.imageView.scaleX = _scaleFactor
    binding.imageView.scaleY = _scaleFactor

    _drawableWidth = _defaultDrawableWidth * _scaleFactor
    _drawableHeight = _defaultDrawableHeight * _scaleFactor

    _viewPortWidth = min(_drawableWidth, binding.root.width.toFloat())
    _viewPortHeight = min(_drawableHeight, binding.root.height.toFloat())

    adjustTranslation(_translationX, _translationY, 0F)
  }

  private fun onTouch(event: MotionEvent?) {
    _scaleGestureDetector.onTouchEvent(event)
    _gestureDetector.onTouchEvent(event)
  }

  private fun initValues() {
    _scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
    _gestureDetector = GestureDetector(requireContext(), PanListener())

    binding.root.post {

      // Drawable自体のアスペクト比（高さ/幅）
      val drawableAspectRatio =
        binding.imageView.drawable.intrinsicHeight.toFloat() / binding.imageView.drawable.intrinsicWidth.toFloat()

      // ImageViewのアスペクト比（高さ/幅）
      val imageViewAspectRatio =
        binding.imageView.height.toFloat() / binding.imageView.width.toFloat()

      // DrawableがImageViewと比較して縦長（高アスペクト比）か横長（低アスペクト比）かを判断
      // defaultDrawableX: 画面ピッタリに収めたときのサイズ
      _defaultDrawableWidth = if (drawableAspectRatio < imageViewAspectRatio) {
        // 横長なDrawable
        // 単純にImageViewWidthに一致させる
        binding.imageView.width.toFloat()
      } else {
        // 縦長なDrawable
        // ImageViewの高さ×（Drawableの幅 / Drawableの高さ）= ImageViewHeight / DrawableHeight * DrawableWidth
        // すなわち、Drawableの幅をImageViewの幅に一致させるように計算している
        binding.imageView.height.toFloat() / drawableAspectRatio
      }

      _defaultDrawableHeight = if (drawableAspectRatio < imageViewAspectRatio) {
        // 横長なDrawable
        // ImageViewWidth / DrawableWidth * DrawableHeight
        // すなわち、Drawableの高さをImageViewの高さに一致させるように計算している
        binding.imageView.width.toFloat() * drawableAspectRatio
      } else {
        // 縦長なDrawable
        // 単純にImageViewHeightに一致させる
        binding.imageView.height.toFloat()
      }

      // デフォルト値を代入しておく
      _drawableHeight = _defaultDrawableHeight
      _drawableWidth = _defaultDrawableWidth

      // ビューポートのサイズはレイアウト後のImageViewのサイズ
      _viewPortHeight = binding.imageView.height.toFloat()
      _viewPortWidth = binding.imageView.width.toFloat()

      // レイアウト時に、
      // ・Drawableのデフォルトサイズ（縦横比を保ったままピッタリ収めたときのサイズ）
      // ・ViewPortのサイズ（＝ImageView サイズ）
      // ３点をメモしておく。
    }
  }

  private fun adjustTranslation(translationX: Float, translationY: Float, distanceX: Float) {
    // 可動範囲を算出
    // Drawableサイズ - ViewPortサイズ / 2 の絶対値
    // 内側でも外側にはみ出ていても、余白がどれだけあるか算出できる
    // Scaleが変わらなければ実質固定値
    val translationXMargin = abs((_drawableWidth - _viewPortWidth) / 2)
    val translationYMargin = abs((_drawableHeight - _viewPortHeight) / 2)

    // 引数で渡されるtranslationX = 現在移動量 - distanceX
    _translationX = if (translationX < 0) {
      // translationXがマイナス → eg) 0F - 指を左方向にスライド（正の値、ユーザはViewを左に移動させたい）
      // 「移動量（マイナス値）」か、「XMarginを正負反転させたもの」のどちらか大きい方を新しいViewの移動量として取得
      // 横長の画像についてscaleFactorが1Fのとき、ViewPortとDrawableは幅が一致しているのでXMarginはゼロ
      // translationXは常にマイナスなのでゼロを取得
      // scaleFactorが2Fのとき、DrawableはViewPortからはみ出しているため、translationXMarginはゼロより大きい
      // 新しい移動量translationXがXMarginよりもゼロに近い場合、左方向に移動できる
      // translationXがXMarginよりもゼロから遠い場合、常にXMarginが返る（XMarginは実質固定値のため、それ以上Viewは移動できない）
      max(translationX, -translationXMargin)
    } else {
      // translationXがプラス → eg) 0F - 指を右方向にスライド（負の値、ユーザはViewを右に移動させたい）
      // 「移動量（プラス値）」か、「XMargin」のどちらか小さい方を新しいViewの移動量として取得
      // （中略）
      // 新しい移動量translationXがXMarginよりもゼロに近い場合、右方向に移動できる
      // translationXがXMarginよりもゼロから遠い場合、常にXMarginが返る（XMarginは実質固定値のため、それ以上Viewは移動できない）
      min(translationX, translationXMargin)
    }

    _translationY = if (translationY < 0) {
      max(translationY, -translationYMargin)
    } else {
      min(translationY, translationYMargin)
    }

    //TODO ここでViewPagerのページ送りの可否を判定したい
    // 逆方向のときの一発目だけimageView.translationXが前回の値になっていてfalseが出る？
    // 指を離したときに可否を変えるべきでは？（パン操作中にページめくり可能としない）
//    viewModel.setEnableTurnPage(binding.imageView.translationX == _translationX)

//    println("imageView.translationX=${binding.imageView.translationX}, _translationX=$_translationX, true?=${binding.imageView.translationX == _translationX}")

    _enableTurnPage = when {
      distanceX < 0 && binding.imageView.translationX == _translationX -> TurnPage.TO_PREVIOUS
      distanceX > 0 && binding.imageView.translationX == _translationX -> TurnPage.TO_NEXT
      else -> TurnPage.NONE
    }

    // ImageViewを移動させる
    binding.imageView.translationX = _translationX
    binding.imageView.translationY = _translationY
  }

  private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    override fun onScale(detector: ScaleGestureDetector?): Boolean {
      detector?.scaleFactor?.let {
        _scaleFactor *= it
        // 最小倍率か、指定倍率か、最大倍率かをmax, minの組み合わせで表現
        // もはやテンプレ式？
        _scaleFactor = max(1F, min(_scaleFactor, 5F))
        binding.imageView.scaleX = _scaleFactor
        binding.imageView.scaleY = _scaleFactor
        // Drawableのサイズをn倍して疑似的に変更（計算用）
        _drawableWidth = _defaultDrawableWidth * _scaleFactor
        _drawableHeight = _defaultDrawableHeight * _scaleFactor
        // ViewPortサイズもここで可変にする？表示領域とdrawableサイズのいずれか小さい方で？
        _viewPortWidth = min(_drawableWidth, binding.root.width.toFloat())
        _viewPortHeight = min(_drawableHeight, binding.root.height.toFloat())
      } ?: println("scaleFactor is null")

      adjustTranslation(_translationX, _translationY, 0F)

      return true
    }
  }

  private inner class PanListener : GestureDetector.SimpleOnGestureListener() {
    override fun onScroll(
      e1: MotionEvent?,
      e2: MotionEvent?,
      distanceX: Float,
      distanceY: Float
    ): Boolean {
      // 現在のXY座標（adjustTranslation内で更新）からdistance分だけ移動した値をメモ
//      println("distanceX: $distanceX, distanceY: $distanceY")
      // 右にスワイプ：distanceX < 0
      // 左にスワイプ：distanceX > 0
      val translationX = _translationX - distanceX
      val translationY = _translationY - distanceY

      // 移動自体はこちらのメソッド内で実行
      adjustTranslation(translationX, translationY, distanceX)

      return true
    }

    override fun onFling(
      e1: MotionEvent?,
      e2: MotionEvent?,
      velocityX: Float,
      velocityY: Float
    ): Boolean {

      println(velocityX)
      when {
        _enableTurnPage == TurnPage.TO_PREVIOUS && velocityX > 2000 -> viewModel.onFling(TurnPage.TO_PREVIOUS)
        _enableTurnPage == TurnPage.TO_NEXT && velocityX < -2000 -> viewModel.onFling(TurnPage.TO_NEXT)
        else -> viewModel.onFling(TurnPage.NONE)
      }

      _enableTurnPage = TurnPage.NONE

      return true
    }
  }


  companion object {
    fun getInstance(bundle: Bundle? = null): DetailImageFragment {
      val fragment = DetailImageFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}