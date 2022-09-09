package team.redrock.stickerset.main.screen.stickerset

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.model.repository.TelegramRepository
import team.redrock.stickerset.main.mvi.MVIViewModel
import team.redrock.stickerset.main.utils.FetchStatus

class StickerSetViewModel : MVIViewModel<StickerViewAction, StickerViewEvent, StickerViewStates>() {
    override val mutableViewState: MutableStateFlow<StickerViewStates> = MutableStateFlow(StickerViewStates())

    lateinit var data: StickerSetEntity

    override fun dispatch(action: StickerViewAction) {
        when (action) {
            is StickerViewAction.DeleteStickerSet -> {
                viewModelScope.launch {
                    TelegramRepository.deleteStickerSet(data)
                    mutableViewEvents.emit(StickerViewEvent.Toast("已删除"))
                    mutableViewEvents.emit(StickerViewEvent.Exit)
                }
            }
            is StickerViewAction.FetchData, StickerViewAction.OnSwipeRefresh -> fetchData(data)
        }
    }

    private fun fetchData(data: StickerSetEntity) {
        setState { copy(fetchStatus = FetchStatus.Fetching) }
        viewModelScope.launch {
            val res = TelegramRepository.fetchStickers(data.id!!)
            setState { copy(fetchStatus = FetchStatus.Fetched, stickerSets = res) }
        }
    }

}