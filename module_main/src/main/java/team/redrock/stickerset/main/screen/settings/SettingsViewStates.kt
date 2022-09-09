package team.redrock.stickerset.main.screen.settings

import team.redrock.stickerset.main.mvi.ViewAction
import team.redrock.stickerset.main.mvi.ViewEvent
import team.redrock.stickerset.main.mvi.ViewState
import team.redrock.stickerset.main.screen.category.CategoryViewEvent

class SettingsViewStates() : ViewState

sealed class SettingsViewAction : ViewAction {
    object EditTokenItemClicked : SettingsViewAction()
    class SetToken(val token: String) : SettingsViewAction()
}

sealed class SettingsViewEvent : ViewEvent {
    object ShowTokenDialog : SettingsViewEvent()
    object JumpToAboutPage : SettingsViewEvent()
    class ShowSnackBar(val content: String) : SettingsViewEvent()
}