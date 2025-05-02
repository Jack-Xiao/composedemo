package com.tianyejia.composedemo.week4

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 第四周学习内容：主题与动画结合
 *
 * 本示例展示如何结合主题和动画，创建平滑的主题切换效果
 */

// 主题选项数据类
data class ThemeOption(
    val name: String,
    val icon: ImageVector,
    val lightColors: ColorScheme,
    val darkColors: ColorScheme
)

@Composable
fun AnimatedThemeDemo() {
    // 可选主题列表：默认、蓝色和紫色
    val themes = listOf(
        ThemeOption(
            name = "默认主题",
            icon = Icons.Default.Check,
            lightColors = lightColorScheme(),
            darkColors = darkColorScheme()
        ),
        ThemeOption(
            name = "蓝色主题",
            icon = Icons.Default.Check,
            lightColors = lightColorScheme(
                primary = Color(0xFF1976D2),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFBBDEFB),
                secondary = Color(0xFF0288D1),
                background = Color(0xFFF5F5F5),
                surface = Color.White,
                onBackground = Color(0xFF0D47A1),
                onSurface = Color(0xFF0D47A1)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFF82B1FF),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF1565C0),
                secondary = Color(0xFF80D8FF),
                background = Color(0xFF102027),
                surface = Color(0xFF102027),
                onBackground = Color(0xFFE1F5FE),
                onSurface = Color(0xFFE1F5FE)
            )
        ),
        ThemeOption(
            name = "紫色主题",
            icon = Icons.Default.Check,
            lightColors = lightColorScheme(
                primary = Color(0xFF6200EA),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE8DEF8),
                secondary = Color(0xFF9C27B0),
                background = Color(0xFFF5F5F5),
                surface = Color.White,
                onBackground = Color(0xFF4A148C),
                onSurface = Color(0xFF4A148C)
            ),
            darkColors = darkColorScheme(
                primary = Color(0xFFBB86FC),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF6200EA),
                secondary = Color(0xFFCE93D8),
                background = Color(0xFF121212),
                surface = Color(0xFF121212),
                onBackground = Color(0xFFF3E5F5),
                onSurface = Color(0xFFF3E5F5)
            )
        )
    )
    
    // 状态：是否使用深色主题
    var isDarkTheme by remember { mutableStateOf(false) }
    
    // 状态：选择的主题索引
    var selectedThemeIndex by remember { mutableStateOf(0) }
    
    // 获取当前选择的主题
    val currentTheme = themes[selectedThemeIndex]
    
    // 创建动画颜色值
    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkTheme) currentTheme.darkColors.background else currentTheme.lightColors.background,
        animationSpec = tween(durationMillis = 500),
        label = "backgroundColorAnimation"
    )
    
    val primaryColor by animateColorAsState(
        targetValue = if (isDarkTheme) currentTheme.darkColors.primary else currentTheme.lightColors.primary,
        animationSpec = tween(durationMillis = 500),
        label = "primaryColorAnimation"
    )
    
    val surfaceColor by animateColorAsState(
        targetValue = if (isDarkTheme) currentTheme.darkColors.surface else currentTheme.lightColors.surface,
        animationSpec = tween(durationMillis = 500),
        label = "surfaceColorAnimation"
    )
    
    val onSurfaceColor by animateColorAsState(
        targetValue = if (isDarkTheme) currentTheme.darkColors.onSurface else currentTheme.lightColors.onSurface,
        animationSpec = tween(durationMillis = 500),
        label = "onSurfaceColorAnimation"
    )
    
    // 应用当前主题
    MaterialTheme(
        colorScheme = if (isDarkTheme) currentTheme.darkColors else currentTheme.lightColors
    ) {
        // 使用动画颜色而不是主题颜色，这样可以平滑过渡
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 标题
                Text(
                    text = "主题和动画结合",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = primaryColor,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
                
                // 当前主题信息
                Text(
                    text = "当前: ${currentTheme.name} (${if (isDarkTheme) "深色" else "浅色"})",
                    style = MaterialTheme.typography.titleMedium,
                    color = onSurfaceColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // 主题切换按钮 (深色/浅色)
                ThemeModeToggle(
                    isDarkTheme = isDarkTheme,
                    onToggle = { isDarkTheme = !isDarkTheme },
                    primaryColor = primaryColor,
                    surfaceColor = surfaceColor,
                    onSurfaceColor = onSurfaceColor
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 主题选择器
                Text(
                    text = "选择主题",
                    style = MaterialTheme.typography.titleMedium,
                    color = onSurfaceColor,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 主题选项列表
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    themes.forEachIndexed { index, theme ->
                        ThemeColorOption(
                            themeColor = if (isDarkTheme) theme.darkColors.primary else theme.lightColors.primary,
                            isSelected = selectedThemeIndex == index,
                            onClick = { selectedThemeIndex = index },
                            primaryColor = primaryColor
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 动画卡片示例
                AnimatedThemeCard(
                    surfaceColor = surfaceColor,
                    onSurfaceColor = onSurfaceColor,
                    primaryColor = primaryColor
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 按钮示例
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("主题按钮示例")
                }
            }
        }
    }
}

@Composable
fun ThemeModeToggle(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    primaryColor: Color,
    surfaceColor: Color,
    onSurfaceColor: Color
) {
    // 按钮大小动画
    val scale by animateFloatAsState(
        targetValue = if (isDarkTheme) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScaleAnimation"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onToggle() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isDarkTheme) "切换为浅色主题" else "切换为深色主题",
                style = MaterialTheme.typography.bodyLarge,
                color = onSurfaceColor
            )
            
            // 使用缩放动画的图标
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                contentDescription = "切换主题",
                tint = primaryColor,
                modifier = Modifier
                    .size(32.dp)
                    .scale(scale)
            )
        }
    }
}

@Composable
fun ThemeColorOption(
    themeColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryColor: Color
) {
    // 选中项的缩放动画
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "selectionScaleAnimation"
    )
    
    Box(
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(themeColor)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(themeColor)
                )
            }
        }
    }
}

@Composable
fun AnimatedThemeCard(
    surfaceColor: Color,
    onSurfaceColor: Color,
    primaryColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "动画主题卡片",
                style = MaterialTheme.typography.titleMedium,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "这个卡片展示主题颜色随着主题切换而平滑过渡。颜色变化使用动画插值，创造流畅的视觉效果。",
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceColor
            )
        }
    }
} 