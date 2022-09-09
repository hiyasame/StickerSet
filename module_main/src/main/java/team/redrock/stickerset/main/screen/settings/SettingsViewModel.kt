package team.redrock.stickerset.main.screen.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.model.repository.TelegramRepository
import team.redrock.stickerset.main.mvi.MVIViewModel

class SettingsViewModel :
    MVIViewModel<SettingsViewAction, SettingsViewEvent, SettingsViewStates>() {
    override val mutableViewState: MutableStateFlow<SettingsViewStates> = MutableStateFlow(SettingsViewStates())

    override fun dispatch(action: SettingsViewAction) {
        when (action) {
            is SettingsViewAction.SetToken -> {
                TelegramRepository.token = action.token
                viewModelScope.launch {
                    mutableViewEvents.emit(SettingsViewEvent.ShowSnackBar("设置成功"))
                }
            }
            SettingsViewAction.EditTokenItemClicked -> {
                viewModelScope.launch {
                    mutableViewEvents.emit(SettingsViewEvent.ShowTokenDialog)
                }
            }
        }
    }
}