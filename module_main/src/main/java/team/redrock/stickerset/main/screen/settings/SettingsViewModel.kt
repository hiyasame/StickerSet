package team.redrock.stickerset.main.screen.settings

import kotlinx.coroutines.flow.MutableStateFlow
import team.redrock.stickerset.main.mvi.MVIViewModel

class SettingsViewModel :
    MVIViewModel<SettingsViewAction, SettingsViewEvent, SettingsViewStates>() {
    override val mutableViewState: MutableStateFlow<SettingsViewStates>
        get() = MutableStateFlow(SettingsViewStates())

    override suspend fun dispatch(action: SettingsViewAction) {

    }
}