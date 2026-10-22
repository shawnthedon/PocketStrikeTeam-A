package com.pocketstrike.app.model

enum class MessageRole {
    USER, ASSISTANT, TOOL, SYSTEM
}

enum class ChatMode {
    CHAT, AGENT, LOCAL
}

data class ToolCallInfo(
    val id: String,
    val name: String,
    val argumentsJson: String,
    val result: String? = null,
    val isError: Boolean = false
)

data class AgentStep(
    val type: String, // "toolCall", "observation", "denied"
    val timestamp: Long = System.currentTimeMillis(),
    val toolName: String? = null,
    val argumentsJson: String? = null,
    val result: String? = null,
    val isError: Boolean = false
)

data class ChatMessage(
    val id: String,
    val role: MessageRole,
    val content: String,
    val toolCalls: List<ToolCallInfo> = emptyList(),
    val toolCallId: String? = null,
    val toolName: String? = null,
    val tokensPerSecond: Double? = null,
    val tokenCount: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class Conversation(
    val id: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastMessage: String = ""
)

data class AIProviderConfig(
    val id: String,
    val name: String,
    val type: String, // "gemini", "anthropic", "openai", "local", "deepseek"
    val apiKey: String = "",
    val baseUrl: String = "",
    val defaultModel: String = "",
    val isEnabled: Boolean = true,
    val isCustom: Boolean = false
)

data class McpServerConfig(
    val id: String,
    val name: String,
    val commandOrUrl: String,
    val status: String = "Connected",
    val toolsCount: Int = 4,
    val isConnected: Boolean = true
)

data class CronTask(
    val id: String,
    val title: String,
    val scheduleExpression: String,
    val actionPrompt: String,
    val isEnabled: Boolean = true,
    val lastRun: String = "Never"
)

data class HermesMemoryItem(
    val id: String,
    val category: String,
    val key: String,
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class GalleryImage(
    val id: String,
    val prompt: String,
    val imageUrl: String,
    val timestamp: Long = System.currentTimeMillis(),
    val aspectRatio: String = "1:1"
)

data class Persona(
    val id: String,
    val name: String,
    val role: String,
    val systemPrompt: String
)
