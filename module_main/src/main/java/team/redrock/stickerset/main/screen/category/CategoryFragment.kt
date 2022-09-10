package team.redrock.stickerset.main.screen.category

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.databinding.FragmentCategoryBinding
import team.redrock.stickerset.main.databinding.LayoutCategoryDialogBinding
import team.redrock.stickerset.main.databinding.LayoutProgressBarBinding
import team.redrock.stickerset.main.mvi.MVIFragment
import team.redrock.stickerset.main.screen.stickerset.StickerSetActivity
import team.redrock.stickerset.main.utils.FetchStatus

class CategoryFragment :
    MVIFragment<CategoryViewModel, FragmentCategoryBinding, CategoryViewEvent, CategoryViewStates>() {

    private val adapter: CategoryRvAdapter = CategoryRvAdapter {
        StickerSetActivity.start(requireActivity(), it)
    }
    private var progressBinding: LayoutProgressBarBinding? = null
    private var dialog: AlertDialog? = null

    override fun initView() {
        binding.apply {
            rvCategory.layoutManager = StaggeredGridLayoutManager(2, 1)
            rvCategory.adapter = adapter
            srlCategory.setOnRefreshListener {
                viewModel.dispatch(CategoryViewAction.OnSwipeRefresh)
            }
            fabCategory.setOnClickListener {
                viewModel.dispatch(CategoryViewAction.FabClicked)
            }
        }
    }

    override fun StateFlow<CategoryViewStates>.launchEffects() {
        useEffect(CategoryViewStates::fetchStatus) {
            when (it) {
                FetchStatus.NoFetched -> {
                    lifecycleScope.launch {
                        viewModel.dispatch(CategoryViewAction.FetchData)
                    }
                    binding.srlCategory.isRefreshing = false
                }
                FetchStatus.Fetched -> {
                    binding.srlCategory.isRefreshing = false
                }
                FetchStatus.Fetching -> {
                    binding.srlCategory.isRefreshing = true
                }
            }
        }
        useEffect(CategoryViewStates::stickerSets) {
            adapter.submitList(it)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun renderViewEvent(viewEvent: CategoryViewEvent) {
        when (viewEvent) {
            CategoryViewEvent.ShowAddDialog -> {
                val binding = LayoutCategoryDialogBinding.inflate(layoutInflater)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("从Telegram拉取新的StickerSet")
                    .setView(binding.root)
                    .setPositiveButton("拉取!") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.dispatch(CategoryViewAction.FetchStickerSet(binding.etName.text.toString()))
                        }
                    }.setNegativeButton("算了") { _, _ -> }
                    .show()
            }
            is CategoryViewEvent.ShowSnackBar -> {
                Snackbar.make(binding.cl, viewEvent.content, Snackbar.LENGTH_SHORT)
                    .show()
            }
            is CategoryViewEvent.ShowDownloadingProcess -> {
                progressBinding?.processBar?.progress = viewEvent.cur * 100 / viewEvent.total
                if (progressBinding == null) {
                    progressBinding = LayoutProgressBarBinding.inflate(layoutInflater)
                    dialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("正在下载...")
                        .setView(progressBinding?.root)
                        .setCancelable(false)
                        .show()
                }
                progressBinding?.tv?.text = "${viewEvent.cur} / ${viewEvent.total}"
                if (viewEvent.cur == viewEvent.total) {
                    dialog?.cancel()
                }
            }
        }
    }
}