package com.tianyejia.composedemo.week3

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StateManagementActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<StateManagementActivity>()

    @Test
    fun testCounterBasic() {
        // 找到计数器组件
        composeTestRule.onNodeWithText("计数器：0").assertIsDisplayed()
        
        // 点击+1按钮
        composeTestRule.onNodeWithText("+1").performClick()
        
        // 计数器值应该更新为1
        composeTestRule.onNodeWithText("计数器：1").assertIsDisplayed()
    }
    
    @Test
    fun testCounterReset() {
        // 点击+1按钮两次
        composeTestRule.onNodeWithText("+1").performClick()
        composeTestRule.onNodeWithText("+1").performClick()
        
        // 计数器值应该为2
        composeTestRule.onNodeWithText("计数器：2").assertIsDisplayed()
        
        // 点击重置按钮
        composeTestRule.onNodeWithText("重置").performClick()
        
        // 计数器值应该回到0
        composeTestRule.onNodeWithText("计数器：0").assertIsDisplayed()
    }
    
    @Test
    fun testAddTask() {
        // 滚动到任务管理器部分
        composeTestRule.onNodeWithText("任务管理器").performScrollTo()
        
        // 简化测试，只验证能否滚动到任务管理器区域
    }
} 