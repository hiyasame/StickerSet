package team.redrock.stickerset.main.screen.category

import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.mvi.ViewAction
import team.redrock.stickerset.main.mvi.ViewEvent
import team.redrock.stickerset.main.mvi.ViewState
import team.redrock.stickerset.main.utils.FetchStatus

data class CategoryViewStates(
    val fetchStatus: FetchStatus = FetchStatus.NoFetched,
    val stickerSets: List<StickerSetEntity> = listOf()
) : ViewState

sealed class CategoryViewAction : ViewAction {
    object FabClicked : CategoryViewAction()
    object OnSwipeRefresh : CategoryViewAction()
    object FetchData : CategoryViewAction()
    class FetchStickerSet(val name: String) : CategoryViewAction()
}

sealed class CategoryViewEvent : ViewEvent {
    object ShowAddDialog : CategoryViewEvent()
    class ShowSnackBar(val content: String) : CategoryViewEvent()
    class ShowDownloadingProcess(val cur: Int, val total: Int) : CategoryViewEvent()
}