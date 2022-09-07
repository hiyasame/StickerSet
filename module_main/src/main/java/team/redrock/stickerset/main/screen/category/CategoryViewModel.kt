package team.redrock.stickerset.main.screen.category

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.model.repository.TelegramRepository
import team.redrock.stickerset.main.mvi.MVIViewModel
import team.redrock.stickerset.main.utils.FetchStatus

class CategoryViewModel :
    MVIViewModel<CategoryViewAction, CategoryViewEvent, CategoryViewStates>() {

    override val mutableViewState: MutableStateFlow<CategoryViewStates>
        get() = MutableStateFlow(CategoryViewStates())

    private var observeStickerSetChangeJob: Job? = null

    override suspend fun dispatch(action: CategoryViewAction) {
        when (action) {
            CategoryViewAction.OnSwipeRefresh, CategoryViewAction.FetchData -> fetchStickers()
            CategoryViewAction.FabClicked -> fabClicked()
            is CategoryViewAction.FetchStickerSet -> fetchStickerSets(action.name)
        }
    }

    private fun fetchStickerSets(name: String) {
        viewModelScope.launch {
            TelegramRepository.getStickerSet(name)
                .onFailure {
                    mutableViewEvents.emit(CategoryViewEvent.ShowSnackBar(it.message.toString()))
                }.onSuccess {
                    mutableViewEvents.emit(CategoryViewEvent.ShowSnackBar("成功下载StickerSet: ${it.name}"))
                }
        }
    }

    private fun fabClicked() {
        viewModelScope.launch {
            mutableViewEvents.emit(CategoryViewEvent.ShowAddDialog)
        }
    }

    private fun fetchStickers() {
        setState { copy(fetchStatus = FetchStatus.Fetching) }
        // 取消之前观察数据库的协程
        observeStickerSetChangeJob?.cancel()
        observeStickerSetChangeJob = viewModelScope.launch {
            TelegramRepository.fetchStickerSets().collectLatest {
                setState { copy(fetchStatus = FetchStatus.Fetched, stickerSets = it) }
            }
        }
    }
}