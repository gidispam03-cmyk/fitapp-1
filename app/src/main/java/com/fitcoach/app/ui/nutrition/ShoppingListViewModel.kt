package com.fitcoach.app.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.ShoppingListItem
import com.fitcoach.app.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    val items: StateFlow<List<ShoppingListItem>> = repository.observeShoppingList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggle(item: ShoppingListItem) {
        viewModelScope.launch { repository.toggleShoppingItem(item) }
    }

    fun regenerate() {
        viewModelScope.launch { repository.regenerateShoppingList() }
    }
}
