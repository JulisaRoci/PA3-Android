package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.usecase.GetSessionUseCase
import com.isil.android.pa3.presentation.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(getSessionUseCase: GetSessionUseCase) : ViewModel() {
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val session = getSessionUseCase().first()
            _startDestination.value = if (session != null) Routes.MAIN else Routes.LOGIN
        }
    }
}
