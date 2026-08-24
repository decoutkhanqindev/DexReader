package com.decoutkhanqindev.dexreader.presentation.screens.statistics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingChartPointModel
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LayeredComponent
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReadingActivityChart(
  dataPoints: ImmutableList<ReadingChartPointModel>,
  modifier: Modifier = Modifier,
) {
  val modelProducer = remember { CartesianChartModelProducer() }

  LaunchedEffect(dataPoints) {
    modelProducer.runTransaction {
      columnModel { series(dataPoints.map { it.minutes }) }
    }
  }

  val bottomAxisFormatter = remember(dataPoints) {
    CartesianValueFormatter { _, value, _ ->
      dataPoints.getOrNull(value.toInt())?.label.orEmpty()
    }
  }

  ProvideVicoTheme(rememberM3VicoTheme()) {
    CartesianChartHost(
      chart = rememberCartesianChart(
        rememberColumnCartesianLayer(),
        startAxis = VerticalAxis.rememberStart(
          valueFormatter = remember { CartesianValueFormatter.decimal(suffix = " m") },
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
          valueFormatter = bottomAxisFormatter,
          guideline = null,
        ),
        marker = rememberReadingMarker(),
      ),
      modelProducer = modelProducer,
      modifier = modifier,
    )
  }
}

@Composable
private fun rememberReadingMarker(): CartesianMarker {
  val labelBackgroundShape = MarkerCornerBasedShape(CircleShape)
  val labelBackground = rememberShapeComponent(
    fill = Fill(MaterialTheme.colorScheme.background),
    shape = labelBackgroundShape,
    strokeFill = Fill(MaterialTheme.colorScheme.outline),
    strokeThickness = 1.dp,
  )
  val label = rememberTextComponent(
    style = TextStyle(
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
      fontSize = 12.sp,
    ),
    padding = Insets(8.dp, 4.dp),
    background = labelBackground,
    minWidth = TextComponent.MinWidth.fixed(40.dp),
  )
  val indicatorFrontComponent = rememberShapeComponent(
    Fill(MaterialTheme.colorScheme.surface),
    CircleShape,
  )
  val guideline = rememberAxisGuidelineComponent()

  return rememberDefaultCartesianMarker(
    label = label,
    valueFormatter = DefaultCartesianMarker.ValueFormatter.default(suffix = " min"),
    indicator = { color ->
      LayeredComponent(
        back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CircleShape),
        front = LayeredComponent(
          back = ShapeComponent(fill = Fill(color), shape = CircleShape),
          front = indicatorFrontComponent,
          padding = Insets(5.dp),
        ),
        padding = Insets(10.dp),
      )
    },
    indicatorSize = 36.dp,
    guideline = guideline,
  )
}

@Preview
@Composable
private fun ReadingActivityChartWeeklyPreview() {
  val sample = persistentListOf(
    ReadingChartPointModel(id = "2026-08-18", label = "Mon", minutes = 12),
    ReadingChartPointModel(id = "2026-08-19", label = "Tue", minutes = 25),
    ReadingChartPointModel(id = "2026-08-20", label = "Wed", minutes = 0),
    ReadingChartPointModel(id = "2026-08-21", label = "Thu", minutes = 40),
    ReadingChartPointModel(id = "2026-08-22", label = "Fri", minutes = 18),
    ReadingChartPointModel(id = "2026-08-23", label = "Sat", minutes = 33),
    ReadingChartPointModel(id = "2026-08-24", label = "Sun", minutes = 22),
  )
  DexReaderTheme {
    ReadingActivityChart(
      dataPoints = sample,
      modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
    )
  }
}

@Preview
@Composable
private fun ReadingActivityChartMonthlyPreview() {
  val sample = persistentListOf(
    ReadingChartPointModel(id = "2026-06", label = "Jun", minutes = 320),
    ReadingChartPointModel(id = "2026-07", label = "Jul", minutes = 540),
    ReadingChartPointModel(id = "2026-08", label = "Aug", minutes = 210),
  )
  DexReaderTheme {
    ReadingActivityChart(
      dataPoints = sample,
      modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
    )
  }
}
