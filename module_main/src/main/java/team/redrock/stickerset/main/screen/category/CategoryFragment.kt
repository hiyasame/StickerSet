package team.redrock.stickerset.main.screen.category

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.databinding.FragmentCategoryBinding
import team.redrock.stickerset.main.databinding.ItemCategoryBinding
import team.redrock.stickerset.main.databinding.ItemCategoryDialogBinding
import team.redrock.stickerset.main.mvi.MVIFragment
import team.redrock.stickerset.main.mvi.useEffect
import team.redrock.stickerset.main.utils.FetchStatus

class CategoryFragment :
    MVIFragment<CategoryViewModel, FragmentCategoryBinding, CategoryViewEvent, CategoryViewStates>() {

    private val adapter: CategoryRvAdapter = CategoryRvAdapter()

    override fun initView() {
        binding.apply {
            rvCategory.layoutManager = StaggeredGridLayoutManager(2, 1)
            rvCategory.adapter = adapter
            srlCategory.setOnRefreshListener {
                lifecycleScope.launch {
                    viewModel.dispatch(CategoryViewAction.OnSwipeRefresh)
                }
            }
        }
    }

    override suspend fun StateFlow<CategoryViewStates>.launchEffects() {
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

    override suspend fun renderViewEvent(viewEvent: CategoryViewEvent) {
        when (viewEvent) {
            CategoryViewEvent.ShowAddDialog -> {
                val binding = ItemCategoryDialogBinding.inflate(layoutInflater)
                MaterialAlertDialogBuilder(requireContext())
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
        }
    }
}