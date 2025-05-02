package com.tianyejia.composedemo.week4

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 第四周学习内容：Compose 基础动画
 *
 * 本示例展示 Compose 中的基础动画类型：
 * 1. 单值动画 (animateXxxAsState)
 * 2. 可见性动画 (AnimatedVisibility)
 * 3. 内容动画 (AnimatedContent)
 */

@Composable
fun BasicAnimationDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Compose 基础动画",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // 1. 单值动画
        SimpleValueAnimationExample()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 2. 可见性动画
        VisibilityAnimationExample()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 3. 内容动画
        ContentAnimationExample()
    }
}

@Composable
fun SimpleValueAnimationExample() {
    Text(
        text = "单值动画示例",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 颜色动画
        var colorState by remember { mutableStateOf(false) }
        val color by animateColorAsState(
            targetValue = if (colorState) Color.Red else Color.Blue,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            label = "colorAnimation"
        )
        
        // 尺寸动画
        var sizeState by remember { mutableStateOf(false) }
        val size by animateDpAsState(
            targetValue = if (sizeState) 100.dp else 60.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "sizeAnimation"
        )
        
        // 位置动画
        var offsetState by remember { mutableStateOf(false) }
        val offset by animateDpAsState(
            targetValue = if (offsetState) 40.dp else 0.dp,
            animationSpec = tween(500),
            label = "offsetAnimation"
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { colorState = !colorState },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("颜色")
            }
            
            Button(
                onClick = { sizeState = !sizeState },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("尺寸")
            }
            
            Button(
                onClick = { offsetState = !offsetState },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("位置")
            }
        }
        
        Box(
            modifier = Modifier
                .offset(x = offset)
                .size(size)
                .clip(CircleShape)
                .background(color)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "点击按钮\n改变状态",
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VisibilityAnimationExample() {
    Text(
        text = "可见性动画示例",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    var isVisible by remember { mutableStateOf(true) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { isVisible = !isVisible },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(if (isVisible) "隐藏元素" else "显示元素")
        }
        
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { -it }, // 从顶部滑入
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            ) + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it }, // 滑出到顶部
                animationSpec = tween(durationMillis = 300)
            ) + shrinkVertically(
                shrinkTowards = Alignment.Top
            ) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "这个元素可以动画显示和隐藏",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ContentAnimationExample() {
    Text(
        text = "内容动画示例",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    var count by remember { mutableStateOf(0) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { if (count > 0) count-- }) {
                Text("-")
            }
            
            Button(onClick = { count++ }) {
                Text("+")
            }
        }
        
        // 内容动画
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = count,
                    transitionSpec = {
                        if (targetState > initialState) {
                            // 如果目标值大于初始值（递增），则从底部滑入
                            slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()
                        } else {
                            // 如果目标值小于初始值（递减），则从顶部滑入
                            slideInVertically { height -> -height } + fadeIn() with
                            slideOutVertically { height -> height } + fadeOut()
                        }.using(
                            SizeTransform(clip = false)
                        )
                    },
                    label = "countAnimation"
                ) { targetCount ->
                    Text(
                        text = "计数: $targetCount",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // 自定义内容过渡动画
        var expanded by remember { mutableStateOf(false) }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { expanded = !expanded },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with
                    fadeOut(animationSpec = tween(300))
                },
                label = "expandedAnimation"
            ) { isExpanded ->
                if (isExpanded) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "展开的内容",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "点击卡片可以在展开和收起状态之间切换，并且会有平滑的淡入淡出动画效果。这是 AnimatedContent 的另一种用法示例。",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "点击展开详情",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
} 