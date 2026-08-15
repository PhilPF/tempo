package com.temp0.workout.ui.builder

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

/**
 * A hand-rolled drag-to-reorder list — not a third-party library, since this is the only
 * use site in the app. The drag gesture is attached only to the small handle glyph the
 * caller passes into [itemContent] (not the whole row), so taps on the sets/reps steppers
 * never trigger a drag, per the design chat's explicit "handle subtle and on the left, text
 * and buttons right next to it" direction.
 *
 * The actual reorder ([onMove]) commits once, on drag *end* (not continuously mid-drag) —
 * this keeps the drag gesture's [pointerInput] key ([key]) stable for the whole gesture, so
 * a live list mutation never restarts the gesture detector under the still-down finger.
 * The dragged row simply floats (translationY) until release.
 */
@Composable
fun <T> DragReorderColumn(
    items: List<T>,
    key: (T) -> Any,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 8.dp,
    itemContent: @Composable (item: T, dragHandleModifier: Modifier) -> Unit,
) {
    var draggingIndex by remember { mutableStateOf(-1) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    val rowHeightsPx = remember { SnapshotStateMap<Any, Int>() }

    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            val itemKey = key(item)
            val isDragging = draggingIndex == index

            if (index > 0) Spacer(modifier = Modifier.height(itemSpacing))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates -> rowHeightsPx[itemKey] = coordinates.size.height }
                    .zIndex(if (isDragging) 1f else 0f)
                    .graphicsLayer { translationY = if (isDragging) dragOffsetY else 0f },
            ) {
                val handleModifier = Modifier.pointerInput(itemKey) {
                    detectDragGestures(
                        onDragStart = {
                            draggingIndex = index
                            dragOffsetY = 0f
                        },
                        onDragEnd = {
                            val rowHeight = rowHeightsPx[itemKey]
                            if (rowHeight != null && rowHeight > 0) {
                                val delta = (dragOffsetY / rowHeight).roundToInt()
                                val targetIndex = (index + delta).coerceIn(0, items.lastIndex)
                                if (targetIndex != index) onMove(index, targetIndex)
                            }
                            draggingIndex = -1
                            dragOffsetY = 0f
                        },
                        onDragCancel = {
                            draggingIndex = -1
                            dragOffsetY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragOffsetY += dragAmount.y
                        },
                    )
                }
                itemContent(item, handleModifier)
            }
        }
    }
}
