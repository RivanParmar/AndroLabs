package com.rivan.androlabs.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.rivan.androlabs.core.designsystem.component.*
import com.rivan.androlabs.core.designsystem.icon.ALIcons
import com.rivan.androlabs.core.designsystem.icon.Icon.DrawableResourceIcon
import com.rivan.androlabs.core.designsystem.icon.Icon.ImageVectorIcon
import com.rivan.androlabs.feature.npw.navigation.navigateToNewProjectWizard
import com.rivan.androlabs.navigation.ALNavHost
import com.rivan.androlabs.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)

@Composable
fun AndroLabsApp(
    windowSizeClass: WindowSizeClass,
    appState: AndroLabsAppState = rememberAndroLabsAppState(
        windowSizeClass = windowSizeClass
    )
) {
    // TODO: Make the if-else statements related to this, less cluttered
    val isNewProjectWizardScreen: Boolean =
        appState.currentDestination.isNewProjectWizardDestination()

    ALBackground {

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                // TODO: Add proper enter and exit transition here!
                AnimatedVisibility(
                    visible = !isNewProjectWizardScreen && appState.shouldShowBottomBar
                ) {
                    ALBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag("ALBottomBar")
                    )
                }
            },
            floatingActionButton = {
                // TODO: Maybe use animations here too when navigating between destinations
                if (appState.shouldShowBottomBar && !isNewProjectWizardScreen &&
                    appState.currentTopLevelDestination != TopLevelDestination.SETTINGS) {
                    ALFab(
                        onClick = {
                            appState.navController.navigateToNewProjectWizard()
                        },
                        elevation = FloatingActionButtonDefaults.elevation()
                    )
                }
            }
        ) { padding ->

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                // TODO: Remove AnimatedVisibility if we use dialog for New Project Wizard on large
                //  screen devices
                AnimatedVisibility(
                    visible = appState.shouldShowNavRail && !isNewProjectWizardScreen
                ) {
                    ALNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        header = {
                            // TODO: Maybe disable the FAB rather than hiding it
                            if (!isNewProjectWizardScreen &&
                                appState.currentTopLevelDestination != TopLevelDestination.SETTINGS
                            ) {
                                ALFab(
                                    onClick = {
                                        appState.navController.navigateToNewProjectWizard()
                                    },
                                    elevation =
                                    FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .testTag("ALNavRail")
                            .safeDrawingPadding()
                    )
                }

                Column(Modifier.fillMaxSize()) {
                    // Show the top app bar on top level destinations.
                    val destination = appState.currentTopLevelDestination
                    if (destination != null) {
                        ALTopAppBar(
                            titleRes = destination.titleTextId,
                            actionIcon = ALIcons.Account,
                            actionIconContentDescription = stringResource(
                                // TODO: Add proper string resource here.
                                id = android.R.string.untitled
                            ),
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent
                            ),
                            onActionClick = { /*TODO*/ }
                        )
                    }

                    ALNavHost(appState.navController)
                }
            }
        }
    }
}

@Composable
private fun ALBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    ALNavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            ALNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }

                    when (icon) {
                        is ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )
                        is DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.iconTextId)) }
            )
        }
    }
}

@Composable
private fun ALNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    header: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    ALNavigationRail(
        modifier = modifier,
        header = header
    ) {
        // Align the items to the bottom of the navigation rail.
        // Both the spacers above and below combine to align the items to the center.
        Spacer(modifier = Modifier.weight(1F))
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            ALNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }

                    when (icon) {
                        is ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )
                        is DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.iconTextId)) }
            )
        }
        // Align the items to the top of the navigation rail.
        Spacer(modifier = Modifier.weight(1F))
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

private fun NavDestination?.isNewProjectWizardDestination() =
    this?.hierarchy?.any {
        it.route?.contains("new_project_wizard_route", true) ?: false
    } ?: false

@Composable
private fun ALFab(
    onClick: () -> Unit,
    elevation: FloatingActionButtonElevation,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer
) {
    ALFloatingActionButton(
        onClick = onClick,
        elevation = elevation,
        containerColor = containerColor,
        modifier = modifier
    ) {
        // TODO: Use string resource for contentDescription
        Icon(ALIcons.Add, contentDescription = null)
    }
}