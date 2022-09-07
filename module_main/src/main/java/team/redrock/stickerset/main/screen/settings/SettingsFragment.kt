package team.redrock.stickerset.main.screen.settings

import kotlinx.coroutines.flow.StateFlow
import team.redrock.stickerset.main.databinding.FragmentSettingsBinding
import team.redrock.stickerset.main.mvi.MVIFragment

class SettingsFragment:
    MVIFragment<SettingsViewModel, FragmentSettingsBinding, SettingsViewEvent, SettingsViewStates>() {

    override fun initView() {

    }

    override suspend fun StateFlow<SettingsViewStates>.launchEffects() {

    }

    override suspend fun renderViewEvent(viewEvent: SettingsViewEvent) {

    }
}