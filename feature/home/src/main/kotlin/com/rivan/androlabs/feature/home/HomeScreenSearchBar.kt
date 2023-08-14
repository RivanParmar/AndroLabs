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

package com.rivan.androlabs.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rivan.androlabs.core.designsystem.component.ALDockedSearchBar
import com.rivan.androlabs.core.designsystem.component.ALSearchBar
import com.rivan.androlabs.core.designsystem.icon.ALIcons
import com.rivan.androlabs.core.model.data.ContentType

@Composable
internal fun HomeScreenSearchBar(
    contentType: ContentType,
    text: String,
    active: Boolean,
    recentSearchQueriesUiState: RecentSearchQueriesUiState,
    onSearch: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
) {
    val leadingIcon = @Composable {
        if (!active) {
            Icon(imageVector = ALIcons.Search, contentDescription = null)
        } else {
            IconButton(onClick = { onActiveChange(false) }) {
                Icon(imageVector = ALIcons.Back, contentDescription = null)
            }
        }
    }

    val trailingIcon = @Composable {
        if (!active) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = ALIcons.Account, contentDescription = null)
            }
        }
    }

    if (contentType == ContentType.SINGLE_PANE) {
        ALSearchBar(
            placeholderRes = R.string.search_bar_placeholder_text,
            text = text,
            active = active,
            onSearch = onSearch,
            onTextChange = onTextChange,
            onActiveChange = onActiveChange,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        ) {
            if (recentSearchQueriesUiState is RecentSearchQueriesUiState.Success) {
                RecentSearchesPanel(
                    recentSearchQueries = recentSearchQueriesUiState.recentQueries.map { it.query },
                ) {}
            }
        }
    } else {
        ALDockedSearchBar(
            placeholderRes = R.string.search_bar_placeholder_text,
            text = text,
            active = active,
            onSearch = onSearch,
            onTextChange = onTextChange,
            onActiveChange = onActiveChange,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = Modifier.padding(12.dp),
        ) {
            if (recentSearchQueriesUiState is RecentSearchQueriesUiState.Success) {
                RecentSearchesPanel(
                    recentSearchQueries = recentSearchQueriesUiState.recentQueries.map { it.query },
                ) {}
            }
        }
    }
}

@Composable
private fun RecentSearchesPanel(
    recentSearchQueries: List<String>,
    onRecentSearchClicked: (String) -> Unit,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.search_bar_recent_searches),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(recentSearchQueries) { recentSearchQuery ->
                RecentSearchItem(
                    recentSearchQuery = recentSearchQuery,
                    onRecentSearchClicked = { onRecentSearchClicked(recentSearchQuery) },
                )
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    recentSearchQuery: String,
    onRecentSearchClicked: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 14.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClickLabel = null,
                onClick = { onRecentSearchClicked(recentSearchQuery) },
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )

        Text(
            text = recentSearchQuery,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 18.dp)
                .fillMaxWidth(),
        )
    }
}