package com.pocketstrike.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pocketstrike.app.data.AgentRunEvent
import com.pocketstrike.app.data.PocketStrikeRepository
import com.pocketstrike.app.model.AIProviderConfig
import com.pocketstrike.app.model.AgentStep
import com.pocketstrike.app.model.ChatMessage
import com.pocketstrike.app.model.ChatMode
import com.pocketstrike.app.model.Conversation
import com.pocketstrike.app.model.CronTask
import com.pocketstrike.app.model.GalleryImage
import com.pocketstrike.app.model.HermesMemoryItem
import com.pocketstrike.app.model.McpServerConfig
import com.pocketstrike.app.model.MessageRole
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class UiState(
    val conversations: List<Conversation> = emptyList(),
    val activeConversationId: String? = null,
    val messages: List<ChatMessage> = emptyList(),
    val chatMode: ChatMode = ChatMode.AGENT,
    val isGenerating: Boolean = false,
    val streamingText: String = "",
    val activeStatus: String? = null,
    val activeSteps: List<AgentStep> = emptyList(),
    val providers: List<AIProviderConfig> = emptyList(),
    val mcpServers: List<McpServerConfig> = emptyList(),
    val cronTasks: List<CronTask> = emptyList(),
    val memories: List<HermesMemoryItem> = emptyList(),
    val gallery: List<GalleryImage> = emptyList(),
    val activeProviderName: String = "Google Gemini"
)

class PocketStrikeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PocketStrikeRepository(application)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var currentRunJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        val convs = repository.getConversations()
        val initialConv = convs.firstOrNull()
        val msgs = initialConv?.let { repository.getMessages(it.id) } ?: emptyList()

        _uiState.update {
            it.copy(
                conversations = convs,
                activeConversationId = initialConv?.id,
                messages = msgs,
                providers = repository.getProviders(),
                mcpServers = repository.getMcpServers(),
                cronTasks = repository.getCronTasks(),
                memories = repository.getMemories(),
                gallery = repository.getGallery()
            )
        }
    }

    fun selectConversation(id: String) {
        val msgs = repository.getMessages(id)
        _uiState.update {
            it.copy(
                activeConversationId = id,
                messages = msgs,
                streamingText = "",
                activeStatus = null,
                activeSteps = emptyList()
            )
        }
    }

    fun createNewConversation(title: String = "New Chat") {
        val newConv = repository.createConversation(title)
        val convs = repository.getConversations()
        _uiState.update {
            it.copy(
                conversations = convs,
                activeConversationId = newConv.id,
                messages = emptyList(),
                streamingText = "",
                activeStatus = null,
                activeSteps = emptyList()
            )
        }
    }

    fun deleteConversation(id: String) {
        repository.deleteConversation(id)
        val convs = repository.getConversations()
        val nextActive = convs.firstOrNull()?.id
        val msgs = nextActive?.let { repository.getMessages(it) } ?: emptyList()
        _uiState.update {
            it.copy(
                conversations = convs,
                activeConversationId = nextActive,
                messages = msgs
            )
        }
    }

    fun renameConversation(id: String, newTitle: String) {
        repository.renameConversation(id, newTitle)
        _uiState.update {
            it.copy(conversations = repository.getConversations())
        }
    }

    fun setChatMode(mode: ChatMode) {
        _uiState.update { it.copy(chatMode = mode) }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        val convId = _uiState.value.activeConversationId ?: return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            role = MessageRole.USER,
            content = trimmed
        )
        repository.addMessage(convId, userMessage)

        val updatedMessages = repository.getMessages(convId)
        _uiState.update {
            it.copy(
                messages = updatedMessages,
                isGenerating = true,
                streamingText = "",
                activeStatus = "Thinking...",
                activeSteps = emptyList()
            )
        }

        currentRunJob?.cancel()
        currentRunJob = viewModelScope.launch {
            repository.runAgent(trimmed, _uiState.value.chatMode).collect { event ->
                when (event) {
                    is AgentRunEvent.Status -> {
                        _uiState.update { it.copy(activeStatus = event.status) }
                    }
                    is AgentRunEvent.StepAdded -> {
                        _uiState.update { it.copy(activeSteps = it.activeSteps + event.step) }
                    }
                    is AgentRunEvent.TextDelta -> {
                        _uiState.update { it.copy(streamingText = it.streamingText + event.text) }
                    }
                    is AgentRunEvent.Done -> {
                        val assistantMessage = ChatMessage(
                            id = UUID.randomUUID().toString(),
                            role = MessageRole.ASSISTANT,
                            content = event.fullText,
                            toolCalls = event.toolCalls,
                            tokensPerSecond = event.tps,
                            tokenCount = event.tokenCount
                        )
                        repository.addMessage(convId, assistantMessage)
                        _uiState.update {
                            it.copy(
                                messages = repository.getMessages(convId),
                                isGenerating = false,
                                streamingText = "",
                                activeStatus = null,
                                activeSteps = emptyList(),
                                gallery = repository.getGallery()
                            )
                        }
                    }
                    is AgentRunEvent.Error -> {
                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                activeStatus = "Error: ${event.message}"
                            )
                        }
                    }
                }
            }
        }
    }

    fun stopGeneration() {
        currentRunJob?.cancel()
        currentRunJob = null
        _uiState.update {
            it.copy(
                isGenerating = false,
                streamingText = "",
                activeStatus = null,
                activeSteps = emptyList()
            )
        }
    }

    fun regenerateLastMessage() {
        val convId = _uiState.value.activeConversationId ?: return
        val lastUserMessage = _uiState.value.messages.lastOrNull { it.role == MessageRole.USER }
        if (lastUserMessage != null) {
            sendMessage(lastUserMessage.content)
        }
    }

    fun toggleMcp(id: String) {
        repository.toggleMcpServer(id)
        _uiState.update { it.copy(mcpServers = repository.getMcpServers()) }
    }

    fun toggleCron(id: String) {
        repository.toggleCronTask(id)
        _uiState.update { it.copy(cronTasks = repository.getCronTasks()) }
    }

    fun deleteMemory(id: String) {
        repository.deleteMemory(id)
        _uiState.update { it.copy(memories = repository.getMemories()) }
    }

    fun clearAllMemories() {
        repository.clearAllMemories()
        _uiState.update { it.copy(memories = emptyList()) }
    }

    fun deleteGalleryImage(id: String) {
        repository.deleteGalleryImage(id)
        _uiState.update { it.copy(gallery = repository.getGallery()) }
    }

    fun updateProvider(provider: AIProviderConfig) {
        repository.updateProvider(provider)
        _uiState.update { it.copy(providers = repository.getProviders()) }
    }
}
