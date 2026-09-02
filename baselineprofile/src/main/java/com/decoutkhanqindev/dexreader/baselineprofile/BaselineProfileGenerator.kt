package com.decoutkhanqindev.dexreader.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val PACKAGE = "com.decoutkhanqindev.dexreader"
private const val CONTENT_WAIT_MS = 40_000L
private const val TAB_WAIT_MS = 10_000L

private const val TAB_HOME = "tab_home"
private const val TAB_CATEGORIES = "tab_categories"
private const val TAB_PROFILE = "tab_profile"
private const val HOME_SCROLL = "home_scroll"
private const val HOME_SECTION_PREFIX = "home_section_"
private const val CATEGORIES_GRID = "categories_grid"
private const val PROFILE_SCROLL = "profile_scroll"
private const val PROFILE_FAVORITES_ROW = "profile_favorites_row"
private const val PROFILE_HISTORY_ROW = "profile_history_row"
private const val PROFILE_STATISTICS_CHART = "profile_statistics_chart"

private val HOME_SECTIONS = listOf("TRENDING", "LATEST_UPDATE", "NEW_RELEASE", "TOP_RATED")

private fun UiDevice.awaitTag(tag: String, timeoutMs: Long = CONTENT_WAIT_MS): UiObject2? =
  wait(Until.findObject(By.res(tag)), timeoutMs)

private fun UiDevice.tapTab(tag: String) {
  awaitTag(tag, TAB_WAIT_MS)?.click()
}

private fun UiObject2.sweep(device: UiDevice, forward: Direction, backward: Direction) {
  setGestureMargin(device.displayWidth / 5)
  scrollUntil(forward, Until.scrollFinished(forward))
  scrollUntil(backward, Until.scrollFinished(backward))
}

private fun UiObject2.sweepVertical(device: UiDevice) = sweep(device, Direction.DOWN, Direction.UP)

private fun UiObject2.sweepHorizontal(device: UiDevice) =
  sweep(device, Direction.RIGHT, Direction.LEFT)

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
  @get:Rule
  val rule = BaselineProfileRule()

  @Test
  fun generate() = rule.collect(
    packageName = PACKAGE,
    includeInStartupProfile = true,
  ) {
    pressHome()
    startActivityAndWait()

    homeTab(device)
    categoriesTab(device)
    profileTab(device)
  }

  private fun homeTab(device: UiDevice) {
    val feed = device.awaitTag(HOME_SCROLL) ?: return
    feed.sweepVertical(device)

    HOME_SECTIONS.forEach { section ->
      val tag = HOME_SECTION_PREFIX + section
      val scroll = device.findObject(By.res(HOME_SCROLL)) ?: return@forEach
      scroll.setGestureMargin(device.displayWidth / 5)
      scroll.scrollUntil(Direction.DOWN, Until.findObject(By.res(tag)))
      device.findObject(By.res(tag))?.sweepHorizontal(device)
    }

    device.findObject(By.res(HOME_SCROLL))?.let {
      it.setGestureMargin(device.displayWidth / 5)
      it.scrollUntil(Direction.UP, Until.scrollFinished(Direction.UP))
    }
  }

  private fun categoriesTab(device: UiDevice) {
    device.tapTab(TAB_CATEGORIES)
    device.awaitTag(CATEGORIES_GRID)?.sweepVertical(device)
  }

  private fun profileTab(device: UiDevice) {
    device.tapTab(TAB_PROFILE)

    val profile = device.awaitTag(PROFILE_SCROLL) ?: return
    device.awaitTag(PROFILE_FAVORITES_ROW)
    device.awaitTag(PROFILE_HISTORY_ROW)
    device.awaitTag(PROFILE_STATISTICS_CHART)

    profile.sweepVertical(device)

    listOf(PROFILE_FAVORITES_ROW, PROFILE_HISTORY_ROW).forEach { tag ->
      val scroll = device.findObject(By.res(PROFILE_SCROLL)) ?: return@forEach
      scroll.setGestureMargin(device.displayWidth / 5)
      scroll.scrollUntil(Direction.DOWN, Until.findObject(By.res(tag)))
      device.findObject(By.res(tag))?.sweepHorizontal(device)
    }

    device.findObject(By.res(PROFILE_SCROLL))?.let {
      it.setGestureMargin(device.displayWidth / 5)
      it.scrollUntil(Direction.UP, Until.scrollFinished(Direction.UP))
    }
  }
}
