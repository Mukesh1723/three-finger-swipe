package eu.hxreborn.tfs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eu.hxreborn.tfs.prefs.PrefSpec
import eu.hxreborn.tfs.prefs.PrefsRepository
import eu.hxreborn.tfs.prefs.PrefsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModelImpl(
    private val repository: PrefsRepository,
) : SettingsViewModel() {
    override val uiState: StateFlow<PrefsState> =
        repository.state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PrefsState(),
        )

    override fun <T : Any> savePref(
        pref: PrefSpec<T>,
        value: T,
    ) = repository.save(pref, value)

    override fun resetToDefaults() = repository.resetAll()
}

class SettingsViewModelFactory(
    private val repository: PrefsRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(SettingsViewModelImpl::class.java))
        return SettingsViewModelImpl(repository) as T
    }
}
