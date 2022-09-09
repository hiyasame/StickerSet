package team.redrock.stickerset.main.screen.stickerset

import team.redrock.stickerset.main.model.database.entity.StickerEntity
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.mvi.ViewAction
import team.redrock.stickerset.main.mvi.ViewEvent
import team.redrock.stickerset.main.mvi.ViewState
import team.redrock.stickerset.main.screen.category.CategoryViewAction
import team.redrock.stickerset.main.utils.FetchStatus

data class StickerViewStates(
    val fetchStatus: FetchStatus = FetchStatus.NoFetched,
    val stickerSets: List<StickerEntity> = listOf()
) : ViewState

sealed class StickerViewAction : ViewAction {
    object OnSwipeRefresh : StickerViewAction()
    object FetchData : StickerViewAction()
    object DeleteStickerSet : StickerViewAction()
}

sealed class StickerViewEvent : ViewEvent {
    class Toast(val content: String) : StickerViewEvent()
    object Exit : StickerViewEvent()
}