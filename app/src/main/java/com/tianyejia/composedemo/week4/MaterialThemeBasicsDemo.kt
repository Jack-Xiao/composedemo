package com.tianyejia.composedemo.week4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 第四周学习内容：Material Design 主题基础
 *
 * 本示例展示 MaterialTheme 的基本组成部分：colorScheme、typography 和 shapes，
 * 以及如何在 Composable 函数中引用主题属性
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialThemeBasicsDemo() {
    // 使用 MaterialTheme 包装整个 UI
    MaterialTheme {
        // 应用主题的 background 颜色
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MaterialTheme 基础",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // 颜色系统演示
                ColorSchemeSection()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 文本样式演示
                TypographySection()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 形状演示
                ShapesSection()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 组件示例
                ComponentsSection()
            }
        }
    }
}

@Composable
fun ColorSchemeSection() {
    Text(
        text = "颜色系统",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    
    Column(modifier = Modifier.fillMaxWidth()) {
        ColorItem("primary", MaterialTheme.colorScheme.primary)
        ColorItem("onPrimary", MaterialTheme.colorScheme.onPrimary)
        ColorItem("primaryContainer", MaterialTheme.colorScheme.primaryContainer)
        ColorItem("onPrimaryContainer", MaterialTheme.colorScheme.onPrimaryContainer)
        ColorItem("secondary", MaterialTheme.colorScheme.secondary)
        ColorItem("onSecondary", MaterialTheme.colorScheme.onSecondary)
        ColorItem("tertiary", MaterialTheme.colorScheme.tertiary)
        ColorItem("background", MaterialTheme.colorScheme.background)
        ColorItem("surface", MaterialTheme.colorScheme.surface)
        ColorItem("error", MaterialTheme.colorScheme.error)
    }
}

@Composable
fun ColorItem(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color)
        )
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TypographySection() {
    Text(
        text = "文本样式",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "displayLarge",
                style = MaterialTheme.typography.displayLarge
            )
            
            Text(
                text = "displayMedium",
                style = MaterialTheme.typography.displayMedium
            )
            
            Text(
                text = "headlineLarge",
                style = MaterialTheme.typography.headlineLarge
            )
            
            Text(
                text = "headlineMedium",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "titleLarge",
                style = MaterialTheme.typography.titleLarge
            )
            
            Text(
                text = "titleMedium",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = "bodyLarge",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = "bodyMedium",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "labelLarge",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun ShapesSection() {
    Text(
        text = "形状",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 展示不同的形状
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "extraSmall",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "medium", 
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "large",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsSection() {
    Text(
        text = "组件示例",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button 示例
        Button(
            onClick = { },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("基础按钮")
        }
        
        // ElevatedButton 示例
        ElevatedButton(
            onClick = { },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("凸起按钮")
        }
        
        // OutlinedButton 示例
        OutlinedButton(
            onClick = { },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("轮廓按钮")
        }
        
        // TextButton 示例
        TextButton(
            onClick = { },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("文本按钮")
        }
        
        // TextField 示例
        TextField(
            value = "文本输入框",
            onValueChange = { },
            label = { Text("标签") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // OutlinedTextField 示例
        OutlinedTextField(
            value = "轮廓输入框",
            onValueChange = { },
            label = { Text("标签") },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Card 示例
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "卡片标题",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = "这是一个使用 MaterialTheme 样式的卡片示例。卡片使用了主题中定义的颜色、字体和形状属性。",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        
        // Switch 示例
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "开关",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Switch(
                checked = true,
                onCheckedChange = { }
            )
        }
    }
} 