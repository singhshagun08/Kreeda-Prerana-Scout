package com.example.kreedaperana.ui.components

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun MPLineChart(
    modifier: Modifier = Modifier,
    entries: List<Entry>,
    label: String,
    lineColor: Int,
    xAxisLabels: List<String>? = null
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = AndroidColor.WHITE
                setTouchEnabled(true)
                setPinchZoom(true)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = AndroidColor.LTGRAY
                    setDrawGridLines(false)
                    xAxisLabels?.let {
                        valueFormatter = IndexAxisValueFormatter(it)
                        granularity = 1f
                    }
                }
                
                axisLeft.apply {
                    textColor = AndroidColor.LTGRAY
                    setDrawGridLines(true)
                    gridColor = AndroidColor.DKGRAY
                }
                
                axisRight.isEnabled = false
                setBackgroundColor(AndroidColor.TRANSPARENT)
            }
        },
        modifier = modifier,
        update = { chart ->
            val dataSet = LineDataSet(entries, label).apply {
                color = lineColor
                setCircleColor(lineColor)
                lineWidth = 2.5f
                circleRadius = 4f
                setDrawCircleHole(true)
                circleHoleColor = AndroidColor.BLACK
                valueTextColor = AndroidColor.WHITE
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillColor = lineColor
                fillAlpha = 40
                setDrawValues(false)
            }
            chart.data = LineData(dataSet)
            chart.animateX(1000)
        }
    )
}

@Composable
fun MPBarChart(
    modifier: Modifier = Modifier,
    entries: List<BarEntry>,
    label: String,
    barColor: Int,
    xAxisLabels: List<String>? = null
) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = AndroidColor.WHITE
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = AndroidColor.LTGRAY
                    setDrawGridLines(false)
                    xAxisLabels?.let {
                        valueFormatter = IndexAxisValueFormatter(it)
                        granularity = 1f
                    }
                }
                
                axisLeft.apply {
                    textColor = AndroidColor.LTGRAY
                    setDrawGridLines(true)
                    gridColor = AndroidColor.DKGRAY
                }
                
                axisRight.isEnabled = false
                setBackgroundColor(AndroidColor.TRANSPARENT)
            }
        },
        modifier = modifier,
        update = { chart ->
            val dataSet = BarDataSet(entries, label).apply {
                color = barColor
                valueTextColor = AndroidColor.WHITE
                setDrawValues(true)
            }
            chart.data = BarData(dataSet)
            chart.animateY(1000)
        }
    )
}

@Composable
fun MPPieChart(
    modifier: Modifier = Modifier,
    entries: List<PieEntry>,
    label: String,
    colors: List<Int>
) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = AndroidColor.WHITE
                setUsePercentValues(true)
                holeRadius = 60f
                transparentCircleRadius = 65f
                setHoleColor(AndroidColor.TRANSPARENT)
                setCenterTextSize(14f)
                setCenterTextColor(AndroidColor.WHITE)
                setDrawEntryLabels(false)
            }
        },
        modifier = modifier,
        update = { chart ->
            val dataSet = PieDataSet(entries, label).apply {
                setColors(colors)
                valueTextColor = AndroidColor.WHITE
                valueTextSize = 12f
                sliceSpace = 4f
            }
            chart.data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(chart))
            }
            chart.animateXY(1000, 1000)
        }
    )
}
