package com.isil.android.pa3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isil.android.pa3.domain.model.Session
import com.isil.android.pa3.domain.usecase.GetSessionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(getSessionUseCase: GetSessionUseCase) : ViewModel() {
    val session: StateFlow<Session?> = getSessionUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
