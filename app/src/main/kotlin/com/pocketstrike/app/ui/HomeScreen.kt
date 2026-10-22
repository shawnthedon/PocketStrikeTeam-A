package com.pocketstrike.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketstrike.app.model.AgentStep
import com.pocketstrike.app.model.ChatMessage
import com.pocketstrike.app.model.ChatMode
import com.pocketstrike.app.model.MessageRole
import com.pocketstrike.app.theme.CyanAccent
import com.pocketstrike.app.theme.DarkBase
import com.pocketstrike.app.theme.DarkBaseAlt
import com.pocketstrike.app.theme.DarkGlass
import com.pocketstrike.app.theme.DarkGlassBorder
import com.pocketstrike.app.theme.EmeraldAccent
import com.pocketstrike.app.theme.TerminalDark
import com.pocketstrike.app.theme.TextPrimaryDark
import com.pocketstrike.app.theme.TextSecondaryDark
import com.pocketstrike.app.ui.components.GlassButton
import com.pocketstrike.app.ui.components.GlassCard
import com.pocketstrike.app.ui.components.SpeedBadge
import com.pocketstrike.app.ui.components.StatusDot
import com.pocketstrike.app.viewmodel.PocketStrikeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PocketStrikeViewModel,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages.size, uiState.streamingText) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DarkBaseAlt,
                drawerContentColor = TextPrimaryDark,
                modifier = Modifier.width(320.dp)
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkGlass)
                                .border(1.dp, EmeraldAccent, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PS",
                                color = EmeraldAccent,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "PocketStrike",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Autonomous Mobile Agent",
                                color = TextSecondaryDark,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassButton(
                        onClick = {
                            viewModel.createNewConversation("Chat #${uiState.conversations.size + 1}")
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("new_chat_button"),
                        backgroundColor = Color(0xFF141417),
                        borderColor = EmeraldAccent
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "New Chat",
                                tint = EmeraldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("New Conversation", color = EmeraldAccent, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                HorizontalDivider(color = DarkGlassBorder, thickness = 1.dp)

                // Conversation list
                Text(
                    text = "CONVERSATIONS",
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    items(uiState.conversations) { conv ->
                        val isSelected = conv.id == uiState.activeConversationId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DarkGlass else Color.Transparent)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isSelected) EmeraldAccent else Color.Transparent
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    viewModel.selectConversation(conv.id)
                                    scope.launch { drawerState.close() }
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = if (isSelected) EmeraldAccent else TextSecondaryDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = conv.title,
                                    color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                            }
                            if (uiState.conversations.size > 1) {
                                IconButton(
                                    onClick = { viewModel.deleteConversation(conv.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFF71717A),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                HorizontalDivider(color = DarkGlassBorder, thickness = 1.dp)

                // Subsystem Navigation
                Column(modifier = Modifier.padding(12.dp)) {
                    DrawerNavLink(Icons.Default.Image, "AI Image Gallery") {
                        scope.launch { drawerState.close() }
                        onNavigate("gallery")
                    }
                    DrawerNavLink(Icons.Default.Tune, "Model Providers") {
                        scope.launch { drawerState.close() }
                        onNavigate("providers")
                    }
                    DrawerNavLink(Icons.Default.Build, "Hardware Tools") {
                        scope.launch { drawerState.close() }
                        onNavigate("tools")
                    }
                    DrawerNavLink(Icons.Default.Hub, "MCP Servers") {
                        scope.launch { drawerState.close() }
                        onNavigate("mcp")
                    }
                    DrawerNavLink(Icons.Default.Memory, "Hermes Memory") {
                        scope.launch { drawerState.close() }
                        onNavigate("hermes")
                    }
                    DrawerNavLink(Icons.Default.Schedule, "Scheduled Tasks") {
                        scope.launch { drawerState.close() }
                        onNavigate("cron")
                    }
                    DrawerNavLink(Icons.Default.Settings, "System & Theme") {
                        scope.launch { drawerState.close() }
                        onNavigate("appearance")
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = DarkBase,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusDot(color = EmeraldAccent, sizeDp = 8)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PocketStrike",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("menu_button")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimaryDark)
                        }
                    },
                    actions = {
                        // Mode Selector Chips
                        ChatModeSelector(
                            currentMode = uiState.chatMode,
                            onModeSelected = { viewModel.setChatMode(it) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { onNavigate("gallery") }) {
                            Icon(Icons.Default.Image, contentDescription = "Gallery", tint = TextSecondaryDark)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBase)
                )
            },
            bottomBar = {
                ChatInputBar(
                    text = inputText,
                    onTextChanged = { inputText = it },
                    isGenerating = uiState.isGenerating,
                    currentMode = uiState.chatMode,
                    onSend = {
                        if (inputText.isNotBlank()) {
                            val msg = inputText
                            inputText = ""
                            viewModel.sendMessage(msg)
                        }
                    },
                    onStop = { viewModel.stopGeneration() }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.messages.isEmpty() && !uiState.isGenerating) {
                    EmptyChatPromptView(
                        onPromptSelected = { prompt ->
                            viewModel.sendMessage(prompt)
                        }
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(uiState.messages) { message ->
                            ChatMessageCard(message = message)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Live In-Progress Agent Steps & Streaming response
                        if (uiState.isGenerating) {
                            item {
                                ActiveRunCard(
                                    status = uiState.activeStatus,
                                    steps = uiState.activeSteps,
                                    streamingText = uiState.streamingText
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerNavLink(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, tint = TextSecondaryDark, modifier = Modifier.size(18.dp)) },
        label = { Text(title, color = TextPrimaryDark, fontSize = 14.sp) },
        selected = false,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.height(44.dp)
    )
}

@Composable
fun ChatModeSelector(
    currentMode: ChatMode,
    onModeSelected: (ChatMode) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkGlass)
            .border(1.dp, DarkGlassBorder, RoundedCornerShape(20.dp))
            .padding(2.dp)
    ) {
        ModeChip(
            label = "AGENT",
            isSelected = currentMode == ChatMode.AGENT,
            accentColor = EmeraldAccent,
            onClick = { onModeSelected(ChatMode.AGENT) }
        )
        ModeChip(
            label = "CHAT",
            isSelected = currentMode == ChatMode.CHAT,
            accentColor = CyanAccent,
            onClick = { onModeSelected(ChatMode.CHAT) }
        )
        ModeChip(
            label = "LOCAL",
            isSelected = currentMode == ChatMode.LOCAL,
            accentColor = Color(0xFFA855F7),
            onClick = { onModeSelected(ChatMode.LOCAL) }
        )
    }
}

@Composable
fun ModeChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.2f) else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) accentColor else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = if (isSelected) accentColor else TextSecondaryDark
        )
    }
}

@Composable
fun ChatMessageCard(message: ChatMessage) {
    val isUser = message.role == MessageRole.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Role & Info Tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 4.dp)
        ) {
            Text(
                text = if (isUser) "YOU" else "POCKETSTRIKE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (isUser) TextSecondaryDark else EmeraldAccent
            )
            if (!isUser && message.tokensPerSecond != null) {
                Spacer(modifier = Modifier.width(8.dp))
                SpeedBadge(tps = message.tokensPerSecond, tokenCount = message.tokenCount)
            }
        }

        // Message Box
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(
                    BorderStroke(
                        1.dp,
                        if (isUser) Color(0xFF27272A) else DarkGlassBorder
                    ),
                    RoundedCornerShape(16.dp)
                ),
            color = if (isUser) Color(0xFF18181B) else DarkGlass
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // If there are tool calls in this message, display them as accordion pills
                if (message.toolCalls.isNotEmpty()) {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        message.toolCalls.forEach { call ->
                            ToolCallPill(call)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    HorizontalDivider(color = Color(0xFF27272A), thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))
                }

                Text(
                    text = message.content,
                    color = TextPrimaryDark,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun ToolCallPill(call: com.pocketstrike.app.model.ToolCallInfo) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(TerminalDark)
            .border(1.dp, Color(0xFF27272A), RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldAccent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = call.name,
                    color = EmeraldAccent,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = if (expanded) "▲" else "▼",
                color = TextSecondaryDark,
                fontSize = 10.sp
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 6.dp)) {
                if (call.argumentsJson.isNotBlank() && call.argumentsJson != "{}") {
                    Text(
                        text = "Args: ${call.argumentsJson}",
                        color = TextSecondaryDark,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                if (call.result != null) {
                    Text(
                        text = "Result: ${call.result}",
                        color = Color(0xFFD4D4D8),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveRunCard(
    status: String?,
    steps: List<AgentStep>,
    streamingText: String
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = EmeraldAccent.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = EmeraldAccent
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = status ?: "Agent reasoning...",
                    color = EmeraldAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace
                )
            }

            if (steps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                steps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (step.type == "toolCall") "⚡ Invoked:" else "👁️ Observed:",
                            color = if (step.type == "toolCall") CyanAccent else EmeraldAccent,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = step.toolName ?: step.result ?: "",
                            color = TextSecondaryDark,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                    }
                }
            }

            if (streamingText.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = DarkGlassBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = streamingText,
                    color = TextPrimaryDark,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun EmptyChatPromptView(
    onPromptSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(DarkGlass)
                .border(1.dp, EmeraldAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = EmeraldAccent,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "PocketStrike Operational",
            color = TextPrimaryDark,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Autonomous AI Agent • Multi-Provider • Hardware Tools",
            color = TextSecondaryDark,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        val promptSuggestions = listOf(
            "Query battery level and internal storage telemetry",
            "Generate cyberpunk mobile terminal wallpaper",
            "Recall knowledge and preferences from Hermes memory",
            "Verify connected MCP servers and available tools"
        )

        promptSuggestions.forEach { prompt ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPromptSelected(prompt) }
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = EmeraldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = prompt,
                        color = TextPrimaryDark,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChanged: (String) -> Unit,
    isGenerating: Boolean,
    currentMode: ChatMode,
    onSend: () -> Unit,
    onStop: () -> Unit
) {
    Surface(
        color = DarkBaseAlt,
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, DarkGlassBorder), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChanged,
                placeholder = {
                    Text(
                        text = if (currentMode == ChatMode.AGENT) "Ask agent to act or execute tools..." else "Message PocketStrike...",
                        color = TextSecondaryDark,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldAccent,
                    unfocusedBorderColor = DarkGlassBorder,
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark,
                    cursorColor = EmeraldAccent
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (isGenerating) {
                IconButton(
                    onClick = onStop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF27272A))
                        .testTag("stop_button")
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop", tint = Color.Red)
                }
            } else {
                IconButton(
                    onClick = onSend,
                    enabled = text.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (text.isNotBlank()) EmeraldAccent else Color(0xFF1E1E24))
                        .testTag("send_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (text.isNotBlank()) DarkBase else Color(0xFF71717A)
                    )
                }
            }
        }
    }
}
