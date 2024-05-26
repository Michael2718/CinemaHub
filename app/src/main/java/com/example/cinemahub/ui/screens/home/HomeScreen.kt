package com.example.cinemahub.ui.screens.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cinemahub.R
import com.example.cinemahub.navigation.Routes

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val navigationItemContentList = listOf(
        NavigationItemContent(
            route = Routes.Home.route,
            icon = Icons.Filled.Home
        ),
        NavigationItemContent(
            route = Routes.Search.route,
            icon = Icons.Filled.Search
        ),
        NavigationItemContent(
            route = Routes.Favorite.route,
            icon = Icons.Filled.Favorite
        ),
        NavigationItemContent(
            route = Routes.Profile.route,
            icon = Icons.Filled.AccountCircle
        ),
    )

    Scaffold(
        topBar = {
            HomeScreenTopBar()
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onTabPressed = {

                },
                navigationItemContentList = navigationItemContentList,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Text("Hello Movies!", modifier = modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = modifier
    )
}

@Composable
private fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onTabPressed: ((String) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                onClick = { onTabPressed(navItem.route) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.route
                    )
                },
                alwaysShowLabel = false
            )
        }
    }
}

private data class NavigationItemContent(
    val route: String,
    val icon: ImageVector
)

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}
