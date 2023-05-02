package com.hub.timeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hub.timeline.ui.theme.TimeLineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeLineTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CanvasView()
                }
            }
        }
    }
}

data class TimeLineData(val items: List<Pair<String, String>>)

private fun getItemList(): List<TimeLineData> {
    val firstList = listOf(
        Pair("Text 1", "app"),
        Pair("Text 2", "link"),
    )
    val secondList = listOf(
        Pair("Text 1", "app"),
        Pair("Text 2", "link"),
        Pair("Text 3", "app"),
        Pair("Text 4", "link"),
        Pair("Text 5", ""),
    )
    val thirdList = listOf(
        Pair("Text 1", "app"),
    )
    val fourList = listOf(
        Pair("Text 1", "app"),
        Pair("Text 2", "link"),
        Pair("Text 3", "app"),
    )
    return listOf(
        TimeLineData(firstList),
        TimeLineData(secondList),
        TimeLineData(thirdList),
        TimeLineData(fourList),
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun CanvasView() {
    val titleTextMeasurer = rememberTextMeasurer()
    val listOfPair = getItemList()

    val typeToColorMap = mapOf(
        "app" to Color.Black,
        "link" to Color.Blue
    )
    val titleText = AnnotatedString("Hello world!!")
    val titleTextLayoutResult = remember {
        titleTextMeasurer.measure(titleText, TextStyle(fontSize = 16.sp))
    }
    val endPadding = with(LocalDensity.current) { 10.dp.toPx() }
    val circleSize = with(LocalDensity.current) { 6.dp.toPx() }
    Column(Modifier.fillMaxSize()) {
        listOfPair.forEachIndexed { index, timeLineDataItem ->
            val multipleString = buildAnnotatedString {
                timeLineDataItem.items.forEachIndexed { _, item ->
                    val (text, type) = item
                    withStyle(SpanStyle(color = typeToColorMap[type] ?: Color.Red)) {
                        append("$text\n")
                    }
                }
            }
            val multipleTextLayoutResult = remember {
                titleTextMeasurer.measure(multipleString, TextStyle(fontSize = 14.sp, lineHeight = 1.3.em))
            }
            CanvasContent(
                titleTextLayoutResult,
                multipleTextLayoutResult,
                circleSize,
                endPadding,
                index != 0,
                index != listOfPair.lastIndex,
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun CanvasContent(
    titleTextLayoutResult: TextLayoutResult,
    multipleTextLayoutResult: TextLayoutResult,
    circleSize: Float,
    endPadding: Float,
    firstItem: Boolean,
    lastItem: Boolean,
) {
    val heightInDp = LocalDensity.current.run {
        titleTextLayoutResult.size.height.toDp() +
                multipleTextLayoutResult.size.height.toDp()
    }
    val viewMarginTop = LocalDensity.current.run {
        24.dp.toPx()
    }
    val subTitleTopMargin = LocalDensity.current.run { 12.dp.toPx() }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .height(heightInDp)
    ) {
        val circleX = (center.x / 2) + (titleTextLayoutResult.size.width / 2) - endPadding
        drawText(
            textLayoutResult = titleTextLayoutResult,
            topLeft = Offset(
                x = center.x - titleTextLayoutResult.size.width / 2,
                y = viewMarginTop,
            )
        )
        drawText(
            textLayoutResult = multipleTextLayoutResult,
            topLeft = Offset(
                x = center.x - titleTextLayoutResult.size.width / 2,
                y = viewMarginTop + (titleTextLayoutResult.size.height / 2) + subTitleTopMargin,
            )
        )
        if (firstItem) {
            drawLine(
                color = Color.Black,
                start = Offset(x = circleX, y = 0F),
                end = Offset(
                    x = circleX,
                    y = viewMarginTop + titleTextLayoutResult.size.height / 2 - circleSize
                ),
                strokeWidth = 2.dp.toPx(),
            )
        }
        if (lastItem) {
            drawLine(
                color = Color.Black,
                start = Offset(
                    x = circleX,
                    y = viewMarginTop + titleTextLayoutResult.size.height / 2 + circleSize
                ),
                end = Offset(x = circleX, y = size.height),
                strokeWidth = 2.dp.toPx(),
            )
        }
        drawCircle(
            color = Color.Black,
            radius = circleSize,
            center = Offset(circleX, viewMarginTop + titleTextLayoutResult.size.height / 2)
        )
    }
}