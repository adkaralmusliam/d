package com.xsandbox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.xsandbox.core.AppInfo
import com.xsandbox.ui.viewmodels.AppListState
import com.xsandbox.ui.viewmodels.AppListViewModel

/**
 * AppListScreen: Displays all installed applications for cloning selection
 */
@Composable
fun AppListScreen(
    viewModel: AppListViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onAppSelected: (String) -> Unit
) {
    val appListState by viewModel.appListState.collectAsStateWithLifecycle()
    val filterSystemApps by viewModel.filterSystemApps.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top app bar
        TopAppBar(
            title = {
                Text(
                    text = "Select App to Clone",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        // Search bar
        var searchText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                viewModel.searchApps(query)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search apps...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Filter toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Hide System Apps", fontSize = 14.sp)
            Switch(
                checked = filterSystemApps,
                onCheckedChange = { viewModel.toggleSystemAppFilter() }
            )
        }

        // App list
        when (appListState) {
            is AppListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AppListState.Success -> {
                val apps = (appListState as AppListState.Success).apps
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(apps) { app ->
                        AppListItem(
                            app = app,
                            onClick = { onAppSelected(app.packageName) }
                        )
                    }
                }
            }
            is AppListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${(appListState as AppListState.Error).message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * AppListItem: Individual app item in the list
 */
@Composable
fun AppListItem(
    app: AppInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App icon
        if (app.icon != null) {
            Image(
                bitmap = app.icon!!.toBitmap().asImageBitmap(),
                contentDescription = app.appName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text("?", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // App info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = app.appName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = app.packageName,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            if (app.isSystemApp) {
                Text(
                    text = "System App",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

// Helper extension to convert Drawable to Bitmap
fun android.graphics.drawable.Drawable.toBitmap(): android.graphics.Bitmap {
    val bitmap = android.graphics.Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        android.graphics.Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

// Placeholder for Image composable (use coil/glide in real implementation)
import androidx.compose.foundation.Image as ComposeImage
