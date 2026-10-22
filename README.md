# PocketStrike (PSA)

**PocketStrike** is an advanced autonomous AI Agent and mobile LLM orchestration platform built with Kotlin, Jetpack Compose, and Material Design 3.

## Overview

PocketStrike provides multi-provider LLM intelligence and autonomous agent execution right on mobile devices, featuring:
- **Autonomous ReAct Agent Loop**: Live step execution, reasoning, tool invocation, and observation synthesis.
- **Local Hardware Tools**:
  - `get_battery_status`: Real-time battery charge, temperature, and charging state telemetry.
  - `get_device_info`: Hardware model, processor architecture, Android OS version.
  - `get_storage_info`: Free and total internal storage bytes.
  - `get_network_status`: Active link status and latency monitoring.
  - `generate_image`: Image generation with aspect-ratio management and full preview.
- **Model Context Protocol (MCP)**: First-class client integration with filesystem, GitHub, Brave search, and database MCP servers.
- **Hermes Memory Core**: Autonomous persistent memory and knowledge store for self-evolution across user sessions.
- **Multi-Provider Support**: Google Gemini, Anthropic Claude, OpenAI, Local Ollama, and DeepSeek.
- **AI Image Gallery**: Visual asset repository with prompt tags, dimensions, and dialog inspector.
- **Scheduled Background Tasks**: Cron-driven autonomous triggers for briefings and monitoring.
- **OLED Dark Tech Aesthetic**: Polished black glassmorphic theme with real-time token speed indicators (`⚡ t/s`).

## Build & APK Generation

The application compiles with the Gradle build system targeting Android SDK 36:

```bash
gradle assembleDebug
```

Output APK is located at:
- `PSA.apk` (Root directory)
- `app/build/outputs/apk/debug/app-debug.apk`

## Architecture

- **Language**: Kotlin 2.2+
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM with StateFlow & Coroutines
- **Target SDK**: Android 36 (Minimum SDK: 26)
