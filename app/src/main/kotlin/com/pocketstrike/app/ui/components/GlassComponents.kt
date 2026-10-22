package com.pocketstrike.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketstrike.app.theme.DarkGlass
import com.pocketstrike.app.theme.DarkGlassBorder
import com.pocketstrike.app.theme.EmeraldAccent
import com.pocketstrike.app.theme.TerminalDark
import com.pocketstrike.app.theme.TextPrimaryDark
import com.pocketstrike.app.theme.TextSecondaryDark

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = DarkGlass,
    borderColor: Color = DarkGlassBorder,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .border(BorderStroke(1.dp, borderColor), shape)
            .clip(shape),
        color = backgroundColor,
        shape = shape
    ) {
        content()
    }
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = DarkGlass,
    contentColor: Color = TextPrimaryDark,
    borderColor: Color = DarkGlassBorder,
    content: @Composable () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp)),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        content()
    }
}

@Composable
fun StatusDot(
    color: Color = EmeraldAccent,
    modifier: Modifier = Modifier,
    sizeDp: Int = 8
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun SpeedBadge(
    tps: Double,
    tokenCount: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF18181B))
            .border(1.dp, Color(0xFF27272A), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "⚡ %.1f t/s".format(tps),
            color = EmeraldAccent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )
        if (tokenCount != null && tokenCount > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "• $tokenCount toks",
                color = TextSecondaryDark,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun CodeBlockView(
    code: String,
    language: String = "",
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF27272A), RoundedCornerShape(8.dp)),
        color = TerminalDark
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Text(
                text = code,
                color = Color(0xFFE4E4E7),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp
            )
        }
    }
}
