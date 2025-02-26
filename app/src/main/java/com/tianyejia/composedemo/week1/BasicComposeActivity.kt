package com.tianyejia.composedemo.week1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tianyejia.composedemo.ui.theme.ComposeDemoTheme

class BasicComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                BasicComposeScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicComposeScreen(onBackClick: () -> Unit) {
    // Scaffold 是一个Material Design风格的应用栏和底部导航栏的组合。
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Compose 基础学习") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )
        }
    ) { innerPadding ->
            BasicComposeContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BasicComposeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 标题部分
        Text(
            text = "第一周：Compose 基础",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false  // 设置为 false 来移除字体内边距
                )
            )
        )

        // 1. 基本文本示例
        SectionTitle("1. 基本文本")
        Text(
            text = "这是一个基本的Text组件示例",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        // 2. 按钮示例
        SectionTitle("2. 按钮组件")
        var buttonClickCount by remember { mutableStateOf(0) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { buttonClickCount++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("点击我")
            }

            OutlinedButton(
                onClick = { buttonClickCount = 0 },
                modifier = Modifier.weight(1f)
            ) {
                Text("重置")
            }
        }
        Text(
            text = "按钮点击次数: $buttonClickCount",
            modifier = Modifier.padding(top = 8.dp)
        )

        // 3. 修饰符示例
        SectionTitle("3. Modifier修饰符")
        Text(
            text = "Modifier可以修改组件的外观和行为",
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        )

        // 4. 状态示例
        SectionTitle("4. 状态管理")
        var textFieldValue by remember { mutableStateOf("") }
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            // label = { Text("请输入文本") },
            placeholder = { Text("请输入文本") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                // 设置未获取焦点时的背景色
                unfocusedContainerColor = Color.White,
                // 设置获取焦点时的背景色
                focusedContainerColor = Color.White
            )
        )
        Text(
            text = "您输入的文本: $textFieldValue",
            modifier = Modifier.padding(top = 8.dp)
        )

        // 5. 布局示例
        SectionTitle("5. 布局示例")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "1",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "2",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 6. 交互示例
        SectionTitle("6. 交互示例")
        var isSelected by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { isSelected = !isSelected }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isSelected) "已选中" else "点击选择",
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        //Spacer 组件说明, Spacer 是 Compose 中用于创建空白间距的组件，类似于传统 Android 中的空 View。
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun BasicComposeScreenPreview() {
    ComposeDemoTheme {
        BasicComposeScreen(onBackClick = {})
    }
} 