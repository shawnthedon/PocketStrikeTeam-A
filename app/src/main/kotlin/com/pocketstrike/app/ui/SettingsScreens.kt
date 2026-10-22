package com.pocketstrike.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketstrike.app.model.AIProviderConfig
import com.pocketstrike.app.theme.CyanAccent
import com.pocketstrike.app.theme.DarkBase
import com.pocketstrike.app.theme.DarkGlass
import com.pocketstrike.app.theme.DarkGlassBorder
import com.pocketstrike.app.theme.EmeraldAccent
import com.pocketstrike.app.theme.StatusSuccess
import com.pocketstrike.app.theme.TerminalDark
import com.pocketstrike.app.theme.TextPrimaryDark
import com.pocketstrike.app.theme.TextSecondaryDark
import com.pocketstrike.app.ui.components.GlassButton
import com.pocketstrike.app.ui.components.GlassCard
import com.pocketstrike.app.ui.components.StatusDot
import com.pocketstrike.app.viewmodel.PocketStrikeViewModel

// ==========================================
// 1. Providers Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvidersScreen(
    viewModel: PocketStrikeViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var editingProvider by remember { mutableStateOf<AIProviderConfig?>(null) }

    Scaffold(
        containerColor = DarkBase,
        topBar = {
            TopAppBar(
                title = { Text("Model Providers", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.providers) { provider ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                StatusDot(color = if (provider.isEnabled) EmeraldAccent else Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = provider.name,
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Model: ${provider.defaultModel}",
                                color = TextSecondaryDark,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            if (provider.baseUrl.isNotBlank()) {
                                Text(
                                    text = "Endpoint: ${provider.baseUrl}",
                                    color = Color(0xFF71717A),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        IconButton(onClick = { editingProvider = provider }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = EmeraldAccent)
                        }
                    }
                }
            }
        }
    }

    editingProvider?.let { prov ->
        var apiKey by remember { mutableStateOf(prov.apiKey) }
        var model by remember { mutableStateOf(prov.defaultModel) }
        var baseUrl by remember { mutableStateOf(prov.baseUrl) }

        AlertDialog(
            onDismissRequest = { editingProvider = null },
            containerColor = DarkGlass,
            title = { Text("Configure ${prov.name}", color = TextPrimaryDark) },
            text = {
                Column {
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API Key / Token", color = TextSecondaryDark) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Default Model", color = TextSecondaryDark) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (prov.type == "local") {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = baseUrl,
                            onValueChange = { baseUrl = it },
                            label = { Text("Base URL", color = TextSecondaryDark) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimaryDark,
                                unfocusedTextColor = TextPrimaryDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateProvider(
                        prov.copy(apiKey = apiKey, defaultModel = model, baseUrl = baseUrl)
                    )
                    editingProvider = null
                }) {
                    Text("Save", color = EmeraldAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingProvider = null }) {
                    Text("Cancel", color = TextSecondaryDark)
                }
            }
        )
    }
}

// ==========================================
// 2. Hardware Tools Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    viewModel: PocketStrikeViewModel,
    onBack: () -> Unit
) {
    var toolResult by remember { mutableStateOf<String?>(null) }

    val toolsList = listOf(
        Triple("get_battery_status", "Battery & Power Sentinel", "Reads real-time device battery charge, temperature & status."),
        Triple("get_device_info", "Device & Kernel Telemetry", "Inspects CPU architecture, Android OS version, and model hardware."),
        Triple("get_storage_info", "Internal Storage Profiler", "Measures free and total bytes on internal flash storage."),
        Triple("get_network_status", "Network Connectivity", "Monitors active WiFi/Cellular link latency and interface states."),
        Triple("hermes_recall_memory", "Hermes Memory Recall", "Queries the autonomous persistent memory key-value store."),
        Triple("generate_image", "Synthesize AI Image", "Triggers neural image generation and saves result to Gallery.")
    )

    Scaffold(
        containerColor = DarkBase,
        topBar = {
            TopAppBar(
                title = { Text("Local Hardware Tools", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            toolResult?.let { res ->
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    borderColor = CyanAccent
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Execution Output:", color = CyanAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = res, color = TextPrimaryDark, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(toolsList) { (id, name, desc) ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = name, color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = desc, color = TextSecondaryDark, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = id, color = EmeraldAccent, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            FilledTonalButton(
                                onClick = {
                                    val repo = com.pocketstrike.app.data.PocketStrikeRepository(
                                        viewModel.getApplication()
                                    )
                                    toolResult = repo.executeLocalTool(id, "{}")
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color(0xFF1E1E24),
                                    contentColor = EmeraldAccent
                                )
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Test", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. MCP Servers Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McpScreen(
    viewModel: PocketStrikeViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = DarkBase,
        topBar = {
            TopAppBar(
                title = { Text("Model Context Protocol (MCP)", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.mcpServers) { server ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                StatusDot(color = if (server.isConnected) StatusSuccess else Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = server.name, color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = server.commandOrUrl,
                                color = TextSecondaryDark,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                            Text(
                                text = "${server.toolsCount} registered tools • ${server.status}",
                                color = EmeraldAccent,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Switch(
                            checked = server.isConnected,
                            onCheckedChange = { viewModel.toggleMcp(server.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = EmeraldAccent,
                                checkedTrackColor = DarkGlass,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF1E1E24)
                            )
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. Hermes Memory Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HermesMemoryScreen(
    viewModel: PocketStrikeViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = DarkBase,
        topBar = {
            TopAppBar(
                title = { Text("Hermes Memory Core", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                actions = {
                    if (uiState.memories.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearAllMemories() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear All", tint = Color.Red)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
            )
        }
    ) { paddingValues ->
        if (uiState.memories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No persistent memories stored yet.", color = TextSecondaryDark, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.memories) { mem ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "[${mem.category}] ${mem.key}",
                                    color = EmeraldAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mem.value,
                                    color = TextPrimaryDark,
                                    fontSize = 13.sp
                                )
                            }
                            IconButton(onClick = { viewModel.deleteMemory(mem.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextSecondaryDark, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. Cron Tasks Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronTasksScreen(
    viewModel: PocketStrikeViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = DarkBase,
        topBar = {
            TopAppBar(
                title = { Text("Scheduled Agent Tasks", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.cronTasks) { task ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = task.title, color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Cron: ${task.scheduleExpression}",
                                color = EmeraldAccent,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = task.actionPrompt, color = TextSecondaryDark, fontSize = 12.sp)
                        }
                        Switch(
                            checked = task.isEnabled,
                            onCheckedChange = { viewModel.toggleCron(task.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = EmeraldAccent,
                                checkedTrackColor = DarkGlass,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF1E1E24)
                            )
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. Appearance & System Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = DarkBase,
        topBar = {
            TopAppBar(
                title = { Text("Appearance & System", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Theme Palette", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Pure OLED Black with Glassmorphism accents.", color = TextSecondaryDark, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AccentColorCircle(EmeraldAccent, isSelected = true)
                        AccentColorCircle(CyanAccent, isSelected = false)
                        AccentColorCircle(Color(0xFF8B5CF6), isSelected = false)
                        AccentColorCircle(Color(0xFFFFFFFF), isSelected = false)
                    }
                }
            }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About PocketStrike", color = TextPrimaryDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Version 1.0.0-PROD (Build 101)", color = TextSecondaryDark, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Autonomous Mobile AI Agent & LLM Execution Engine", color = Color(0xFF71717A), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AccentColorCircle(color: Color, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color)
            .border(2.dp, if (isSelected) Color.White else Color.Transparent, CircleShape)
    )
}
