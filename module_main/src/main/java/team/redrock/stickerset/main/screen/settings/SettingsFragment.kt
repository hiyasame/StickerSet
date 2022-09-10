package team.redrock.stickerset.main.screen.settings

import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import team.redrock.stickerset.main.databinding.FragmentSettingsBinding
import team.redrock.stickerset.main.databinding.LayoutCategoryDialogBinding
import team.redrock.stickerset.main.databinding.LayoutSetTokenDialogBinding
import team.redrock.stickerset.main.mvi.MVIFragment
import team.redrock.stickerset.main.screen.about.AboutActivity
import team.redrock.stickerset.main.screen.category.CategoryViewAction

class SettingsFragment:
    MVIFragment<SettingsViewModel, FragmentSettingsBinding, SettingsViewEvent, SettingsViewStates>() {

    override fun initView() {
        binding.ivEdit.setOnClickListener {
            viewModel.dispatch(SettingsViewAction.EditTokenItemClicked)
        }
        binding.llAbout.setOnClickListener {
            AboutActivity.start(requireActivity())
        }
    }

    override fun StateFlow<SettingsViewStates>.launchEffects() {

    }

    override fun renderViewEvent(viewEvent: SettingsViewEvent) {
        when (viewEvent) {
            is SettingsViewEvent.ShowSnackBar -> {
                Snackbar.make(binding.root, viewEvent.content, Snackbar.LENGTH_SHORT)
                    .show()
            }
            SettingsViewEvent.ShowTokenDialog -> {
                val binding = LayoutSetTokenDialogBinding.inflate(layoutInflater)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Input Telegram Bot Token")
                    .setView(binding.root)
                    .setPositiveButton("OK") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.dispatch(SettingsViewAction.SetToken(binding.etToken.text.toString()))
                        }
                    }.setNegativeButton("Cancel") { _, _ -> }
                    .show()
            }
        }
    }
}