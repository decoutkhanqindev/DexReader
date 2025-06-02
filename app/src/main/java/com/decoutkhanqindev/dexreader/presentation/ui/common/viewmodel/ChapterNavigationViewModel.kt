package com.decoutkhanqindev.dexreader.presentation.ui.common.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChapterNavigationViewModel @Inject constructor() : ViewModel() {
  private val _navState = MutableStateFlow<ChapterNavigationState?>(null)
  val navState = _navState.asStateFlow()

  private var chapterRelationsList: List<ChapterRelation> = emptyList()
  private var currentCanLoadNextChapterPages: Boolean = false

  /**
   * Được gọi bởi MangaDetailsViewModel khi danh sách chương được tải hoặc cập nhật.
   * Nó thiết lập toàn bộ bối cảnh điều hướng cho các chương đã tải.
   */
  fun setChapterListContext(
    relationsList: List<ChapterRelation>,
    canLoadNextPages: Boolean
  ) {
    Log.d(
      "ChapterNavigationVM",
      "Setting new chapter list context. Relations count: ${relationsList.size}, Can load Next pages: $canLoadNextPages"
    )
    chapterRelationsList = relationsList
    currentCanLoadNextChapterPages = canLoadNextPages
    // Nếu navState hiện tại có currentChapterId, hãy thử làm mới nó với context mới
    // Hoặc đợi navigateToChapterId được gọi.
    _navState.value?.currentChapterId?.let { currentId ->
      navigateToChapterId(
        targetChapterId = currentId,
        forceUpdate = true
      ) // forceUpdate even if ID is same, context might have changed
    }
  }

  /**
   * Điều hướng đến một chapterId cụ thể.
   * Sẽ cập nhật _navState với thông tin prev/next dựa trên chapterRelationsList.
   * @param targetChapterId ID của chương muốn điều hướng đến.
   * @param forceUpdate Cờ để buộc cập nhật ngay cả khi targetChapterId giống với currentChapterId trong state.
   * Hữu ích khi context (danh sách relations) thay đổi.
   */
  fun navigateToChapterId(targetChapterId: String, forceUpdate: Boolean = false) {
    if (!forceUpdate && _navState.value?.currentChapterId == targetChapterId) {
      Log.d(
        "ChapterNavigationVM",
        "Already on chapter $targetChapterId and not forcing update. Skipping."
      )
      return
    }

    val relation = chapterRelationsList.find { it.id == targetChapterId }

    if (relation != null) {
      val newState = ChapterNavigationState(
        currentChapterId = relation.id,
        previousChapterId = relation.previousId,
        nextChapterId = relation.nextId,
        canLoadNextChapterPages = currentCanLoadNextChapterPages
      )
      Log.d("ChapterNavigationVM", "Navigating to $targetChapterId. New state: $newState")
      _navState.value = newState
    } else {
      // Nếu không tìm thấy chapter trong danh sách hiện tại, có thể nó nằm ở trang chưa tải.
      // Tạm thời, chúng ta chỉ có thể nói là không tìm thấy trong batch này.
      // _navState sẽ không thay đổi hoặc bạn có thể phát ra một trạng thái lỗi/không xác định.
      // Quan trọng là ReaderViewModel phải xử lý trường hợp nextChapterId là null.
      Log.w(
        "ChapterNavigationVM",
        "Target chapter $targetChapterId not found in current relations list."
      )
      // Để an toàn, nếu không tìm thấy, có thể phát ra trạng thái không có prev/next
      _navState.value = ChapterNavigationState(
        currentChapterId = targetChapterId, // Vẫn báo là đang cố tới đây
        previousChapterId = null,
        nextChapterId = null,
        canLoadNextChapterPages = currentCanLoadNextChapterPages // Cờ này là của cả danh sách
      )
    }
  }
}