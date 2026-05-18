package com.decoutkhanqindev.dexreader.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
  @get:Rule
  val rule = BaselineProfileRule()

  @Test
  fun generate() = rule.collect(
    packageName = "com.decoutkhanqindev.dexreader",
    includeInStartupProfile = true,
  ) {
    pressHome()
    startActivityAndWait()

    // Wait for the real content column (not shimmer) — contentDescription set in HomeContent
    device.wait(Until.hasObject(By.desc("home_feed")), 10_000)

    repeat(3) { i ->
      val feed = device.findObject(By.desc("home_feed")) ?: return@repeat
      feed.setGestureMargin(device.displayWidth / 5)
      feed.fling(if (i < 2) Direction.DOWN else Direction.UP)
    }
  }
}
