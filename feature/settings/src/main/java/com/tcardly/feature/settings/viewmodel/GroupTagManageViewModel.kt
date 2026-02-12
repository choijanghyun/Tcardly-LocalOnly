package com.tcardly.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.Tag
import com.tcardly.domain.repository.Group
import com.tcardly.domain.repository.GroupTagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupTagManageUiState(
    val groups: List<Group> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val isLoading: Boolean = true,
    val showGroupDialog: Boolean = false,
    val showTagDialog: Boolean = false,
    val editingGroupName: String = "",
    val editingGroupColor: String = "#0D9488",
    val editingTagName: String = "",
    val editingTagBgColor: String = "#DBEAFE",
    val editingTagTextColor: String = "#2563EB",
    val error: String? = null
)

@HiltViewModel
class GroupTagManageViewModel @Inject constructor(
    private val repository: GroupTagRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupTagManageUiState())
    val uiState: StateFlow<GroupTagManageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.getAllGroups(), repository.getAllTags()) { groups, tags ->
                _uiState.update { it.copy(groups = groups, tags = tags, isLoading = false) }
            }.collect {}
        }
    }

    fun showGroupDialog() { _uiState.update { it.copy(showGroupDialog = true, editingGroupName = "") } }
    fun hideGroupDialog() { _uiState.update { it.copy(showGroupDialog = false) } }
    fun updateGroupName(name: String) { _uiState.update { it.copy(editingGroupName = name) } }

    fun createGroup() {
        val name = _uiState.value.editingGroupName.trim()
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.createGroup(Group(name = name, color = _uiState.value.editingGroupColor))
            _uiState.update { it.copy(showGroupDialog = false) }
        }
    }

    fun deleteGroup(groupId: Long) {
        viewModelScope.launch { repository.deleteGroup(groupId) }
    }

    fun showTagDialog() { _uiState.update { it.copy(showTagDialog = true, editingTagName = "") } }
    fun hideTagDialog() { _uiState.update { it.copy(showTagDialog = false) } }
    fun updateTagName(name: String) { _uiState.update { it.copy(editingTagName = name) } }

    fun createTag() {
        val name = _uiState.value.editingTagName.trim()
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.createTag(Tag(
                name = name,
                bgColor = _uiState.value.editingTagBgColor,
                textColor = _uiState.value.editingTagTextColor
            ))
            _uiState.update { it.copy(showTagDialog = false) }
        }
    }

    fun deleteTag(tagId: Long) {
        viewModelScope.launch { repository.deleteTag(tagId) }
    }
}
