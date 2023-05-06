/*
 * Copyright 2023 Rivan Parmar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rivan.androlabs.feature.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rivan.androlabs.core.data.repository.ProjectResourceQuery
import com.rivan.androlabs.core.data.repository.UserProjectResourceDataRepository
import com.rivan.androlabs.core.data.util.SyncStatusMonitor
import com.rivan.androlabs.core.domain.GetUserProjectResourcesUseCase
import com.rivan.androlabs.core.domain.model.UserProjectResource
import com.rivan.androlabs.core.model.data.UserProjectResourceData
import com.rivan.androlabs.core.ui.ProjectFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    syncStatusMonitor: SyncStatusMonitor,
    private val userProjectResourceDataRepository: UserProjectResourceDataRepository,
    getRecentUserProjectResources: GetUserProjectResourcesUseCase
) : ViewModel() {

    val isSyncing = syncStatusMonitor.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val feedState: StateFlow<ProjectFeedUiState> =
        userProjectResourceDataRepository.getRecentProjectResources(getRecentUserProjectResources)
            .map(ProjectFeedUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ProjectFeedUiState.Loading
            )

    fun  updateRecentProjectResource(projectResourceId: String, isRecent: Boolean) {
        viewModelScope.launch {
            userProjectResourceDataRepository.toggleRecentProjectResourceId(
                projectResourceId, isRecent
            )
        }
    }

    fun updateProjectResourceFavourited(projectResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userProjectResourceDataRepository
                .updateProjectResourceFavourite(projectResourceId, isChecked)
        }
    }

    fun updateProjectResourceCompleted(projectResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userProjectResourceDataRepository
                .updateProjectResourceCompleted(projectResourceId, isChecked)
        }
    }
}

/**
 * Obtain a flow of user project resources whose ids match those that the user has opened recently.
 *
 * getUserNewsResources: The `UseCase` used to obtain the flow of user project resources.
 */
@OptIn(ExperimentalCoroutinesApi::class)
private fun UserProjectResourceDataRepository.getRecentProjectResources(
    getUserProjectResources: GetUserProjectResourcesUseCase
): Flow<List<UserProjectResource>> = userProjectResourceData
    // Map the user data into a set of recent project resource IDs or null if we should return an
    // empty list.
    .map { userProjectResourceData ->
        if (userProjectResourceData.shouldShowEmptyFeed()) {
            null
        } else {
            userProjectResourceData.recentProjectResources
        }
    }
    // Only emit a set of recent project resource IDs if it's changed. This avoids calling
    // potentially expensive operations (like setting up a new flow) when nothing has changed.
    .distinctUntilChanged()
    // getUserProjectResources returns a flow, so we have a flow inside a flow. flatMapLatest moves
    // the inner flow (the one we want to return) to the outer flow and cancels any previous flows
    // created by getUserProjectResources.
    .flatMapLatest { recentProjectResources ->
        if (recentProjectResources == null) {
            flowOf(emptyList())
        } else {
            getUserProjectResources(
                ProjectResourceQuery(
                    filterProjectIds = recentProjectResources
                )
            )
        }
    }

/**
 * If the user hasn't opened any project or lab, then show an empty list.
 */
private fun UserProjectResourceData.shouldShowEmptyFeed() =
    recentProjectResources.isEmpty()