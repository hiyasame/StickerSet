package team.redrock.stickerset.main.screen.category

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.model.repository.TelegramRepository
import team.redrock.stickerset.main.mvi.MVIViewModel
import team.redrock.stickerset.main.utils.FetchStatus

class CategoryViewModel :
    MVIViewModel<CategoryViewAction, CategoryViewEvent, CategoryViewStates>() {

    override val mutableViewState: MutableStateFlow<CategoryViewStates> = MutableStateFlow(CategoryViewStates())

    private var observeStickerSetChangeJob: Job? = null
    private val progressFlow = MutableSharedFlow<Pair<Int, Int>>()

    init {
        viewModelScope.launch {
            progressFlow.collectLatest { (cur, total) ->
                mutableViewEvents.emit(CategoryViewEvent.ShowDownloadingProcess(cur, total))
            }
        }
    }

    override fun dispatch(action: CategoryViewAction) {
        when (action) {
            CategoryViewAction.OnSwipeRefresh, CategoryViewAction.FetchData -> fetchStickers()
            CategoryViewAction.FabClicked -> fabClicked()
            is CategoryViewAction.FetchStickerSet -> fetchStickerSets(action.name)
        }
    }

    private fun fetchStickerSets(name: String) {
        viewModelScope.launch {
            TelegramRepository.getStickerSet(name, progressFlow)
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