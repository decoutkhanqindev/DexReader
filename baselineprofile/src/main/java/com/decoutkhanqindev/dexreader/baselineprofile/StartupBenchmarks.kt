package com.decoutkhanqindev.dexreader.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val PACKAGE = "com.decoutkhanqindev.dexreader"
private const val FEED_WAIT_MS = 10_000L

@RunWith(AndroidJUnit4::class)
class StartupBenchmarks {
  @get:Rule
  val rule = MacrobenchmarkRule()

  // Baseline: profile applied — fails loudly if assets/dexopt/baseline.prof is missing.
  @Test
  fun startupBaselineProfile() = rule.measureRepeated(
    packageName = PACKAGE,
    metrics = listOf(StartupTimingMetric()),
    iterations = 10,
    startupMode = StartupMode.COLD,
    compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
  ) {
    pressHome()
    startActivityAndWait()
    device.wait(Until.hasObject(By.desc("home_feed")), FEED_WAIT_MS)
  }

  // A/B control: no profile — compare medians to prove the profile moved the number.
  @Test
  fun startupNoCompilation() = rule.measureRepeated(
    packageName = PACKAGE,
    metrics = listOf(StartupTimingMetric()),
    iterations = 10,
    startupMode = StartupMode.COLD,
    compilationMode = CompilationMode.None(),
  ) {
    pressHome()
    startActivityAndWait()
    device.wait(Until.hasObject(By.desc("home_feed")), FEED_WAIT_MS)
  }

  // Scroll perf: P95 frameOverrunMs is the headline number (negative = on time).
  @Test
  fun feedScrollPerformance() = rule.measureRepeated(
    packageName = PACKAGE,
    metrics = listOf(FrameTimingMetric()),
    iterations = 5,
    startupMode = StartupMode.WARM,
    compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
  ) {
    startActivityAndWait()
    device.wait(Until.hasObject(By.desc("home_feed")), FEED_WAIT_MS)
    val feed = device.findObject(By.desc("home_feed")) ?: return@measureRepeated
    feed.setGestureMargin(device.displayWidth / 5)
    repeat(3) { i -> feed.fling(if (i < 2) Direction.DOWN else Direction.UP) }
  }
}
