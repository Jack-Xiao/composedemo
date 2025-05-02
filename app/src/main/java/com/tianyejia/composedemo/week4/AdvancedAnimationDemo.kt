package com.tianyejia.composedemo.week4

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 第四周学习内容：高级动画效果
 *
 * 本示例展示 Compose 中的高级动画效果：
 * 1. 无限动画 (InfiniteTransition)
 * 2. 手势驱动的动画
 */

@Composable
fun AdvancedAnimationDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "高级动画示例",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // 1. 无限动画示例
        InfiniteAnimationExample()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 2. 手势驱动的动画示例
        GestureAnimationExample()
    }
}

@Composable
fun InfiniteAnimationExample() {
    Text(
        text = "无限动画示例",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    // 创建无限转换动画控制器
    val infiniteTransition = rememberInfiniteTransition()
    
    // 脉动动画 - 大小变化
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // 旋转动画
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        )
    )
    
    // 颜色变化动画
    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFF6200EE),
        targetValue = Color(0xFF03DAC5),
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 脉动效果
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "脉动",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            
            // 旋转效果
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "旋转",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
            
            // 颜色变化效果
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "颜色变化",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun GestureAnimationExample() {
    Text(
        text = "手势动画示例",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. 拖拽动画
        DragAnimationExample()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 2. 滑动动画
        SwipeAnimationExample()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 3. 缩放动画
        PinchToZoomExample()
    }
}

@Composable
fun DragAnimationExample() {
    Text(
        text = "拖拽示例",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom =
        8.dp)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        
        // 可拖拽的圆形
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(60.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "拖拽我",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun SwipeAnimationExample() {
    Text(
        text = "滑动示例",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    
    var swipeState by remember { mutableStateOf(0f) }
    var maxWidth by remember { mutableStateOf(0f) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
            .onSizeChanged { size ->
                // 获取容器实际宽度
                maxWidth = size.width.toFloat()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "滑动值: ${swipeState.roundToInt()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 滑动条背景
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.2f))
            ) {
                // 滑动进度条
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(swipeState / 100f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                
                // 滑动手柄
                Box(
                    modifier = Modifier
                        .offset {
                            // 简化计算：基于百分比直接计算位置
                            // 调整手柄位置，使其居中在滑动位置上，考虑手柄宽度(32.dp)
                            val handleWidth = 32.dp.toPx()
                            val availableWidth = maxWidth - handleWidth
                            val xOffset = (swipeState / 100f * availableWidth).roundToInt()
                            IntOffset(xOffset, 0)
                        }
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                change.consume()
                                // 更新滑动状态，限制在 0-100 之间
                                val newValue = swipeState + (dragAmount / size.width * 100f)
                                swipeState = newValue.coerceIn(0f, 100f)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun PinchToZoomExample() {
    Text(
        text = "捏合缩放示例",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    
    var scale by remember { mutableStateOf(1f) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        // 限制缩放范围在 0.5 到 3 之间
                        scale = (scale * zoom).coerceIn(0.5f, 3.0f)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "捏合缩放",
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
} 