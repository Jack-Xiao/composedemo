package com.tianyejia.composedemo.week4

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第四周学习内容：主题切换和自定义主题
 *
 * 本示例展示如何在浅色和深色主题间切换，以及如何创建自定义主题
 */

// 自定义浅色主题颜色
private val CustomLightColors = lightColorScheme(
    primary = Color(0xFF006837),          // 深绿色
    onPrimary = Color.White,
    secondary = Color(0xFF4CAF50),        // 浅绿色
    tertiary = Color(0xFF00C853),         // 亮绿色
    background = Color(0xFFF5F5F5),       // 亮灰色
    surface = Color.White,
    onBackground = Color(0xFF1B5E20),     // 深绿文本色
    onSurface = Color(0xFF1B5E20)         // 深绿文本色
)

// 自定义深色主题颜色
private val CustomDarkColors = darkColorScheme(
    primary = Color(0xFF4CAF50),          // 浅绿色
    onPrimary = Color.Black,
    secondary = Color(0xFF81C784),        // 柔和绿色
    tertiary = Color(0xFFA5D6A7),         // 更浅的绿色
    background = Color(0xFF121212),       // 深灰色
    surface = Color(0xFF1E1E1E),          // 浅黑色
    onBackground = Color(0xFFAEEA00),     // 亮绿文本色
    onSurface = Color(0xFFAEEA00)         // 亮绿文本色
)

// 自定义字体样式
private val CustomTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
)

@Composable
fun ThemeSwitcherDemo() {
    // 主题状态 (true 为深色主题，false 为浅色主题)
    var isDarkTheme by remember { mutableStateOf(false) }
    
    // 使用自定义主题 (true) 或默认主题 (false)
    var useCustomTheme by remember { mutableStateOf(false) }
    
    // 动画颜色变化，使切换更流畅
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isDarkTheme && useCustomTheme -> CustomDarkColors.background
            isDarkTheme && !useCustomTheme -> darkColorScheme().background
            !isDarkTheme && useCustomTheme -> CustomLightColors.background
            else -> lightColorScheme().background
        },
        label = "backgroundColor"
    )
    
    // 应用选定的主题
    MaterialTheme(
        colorScheme = when {
            isDarkTheme && useCustomTheme -> CustomDarkColors
            isDarkTheme && !useCustomTheme -> darkColorScheme()
            !isDarkTheme && useCustomTheme -> CustomLightColors
            else -> lightColorScheme()
        },
        typography = if (useCustomTheme) CustomTypography else Typography()
    ) {
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
                Text(
                    text = "主题切换演示",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                )
                
                // 当前主题信息
                Text(
                    text = "当前：${if (isDarkTheme) "深色" else "浅色"}主题，${if (useCustomTheme) "自定义" else "默认"}样式",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // 主题切换按钮
                Button(
                    onClick = { isDarkTheme = !isDarkTheme },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "切换主题"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "切换到${if (isDarkTheme) "浅色" else "深色"}主题")
                }
                
                // 样式切换开关
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "使用自定义主题",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Switch(
                        checked = useCustomTheme,
                        onCheckedChange = { useCustomTheme = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
                
                // 主题预览卡片
                ThemePreviewCard()
            }
        }
    }
}

@Composable
fun ThemePreviewCard() {
    // 主题预览卡片
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "主题预览",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "这是正文文本样式",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { }
                ) {
                    Text("Primary 按钮")
                }
                
                OutlinedButton(
                    onClick = { }
                ) {
                    Text("次要按钮")
                }
            }
            
            // 颜色示例
            Text(
                text = "主题颜色",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorBox("Primary", MaterialTheme.colorScheme.primary)
                ColorBox("Secondary", MaterialTheme.colorScheme.secondary)
                ColorBox("Tertiary", MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

@Composable
fun ColorBox(name: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
} 