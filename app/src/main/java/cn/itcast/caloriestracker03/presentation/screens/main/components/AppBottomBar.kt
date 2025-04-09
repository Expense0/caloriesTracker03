package cn.itcast.caloriestracker03.presentation.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.presentation.screens.main.BottomNavItem


@Composable
fun AppBottomBar(
    navItems: List<BottomNavItem>,
    backStackEntry: NavBackStackEntry?,
    modifier: Modifier = Modifier,
    onNavItemClick: (BottomNavItem) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp // 符合 Material 3 的默认高度
    ) {
        navItems.forEach { navItem ->
            val isSelected = backStackEntry?.destination?.hierarchy
                ?.any { it.route == navItem.route } ?: false

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavItemClick(navItem) }, // 正确的点击事件写法
                icon = {
                    AnimatedVisibility(visible = isSelected) {
                        Icon(
                            painter = painterResource(id = navItem.iconResId),
                            contentDescription = stringResource(R.string.desc_bottom_bar_icon),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = navItem.titleResId),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                alwaysShowLabel = true // 强制显示标签
            )
        }
    }

}