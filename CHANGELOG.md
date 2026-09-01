# Changelog

Dated log of notable multi-file / cross-cutting work sessions. Newest entry first.

---

## 2026-09-01 — Màn onboarding 4 page (HorizontalPager) chèn giữa Splash và Main

Thêm `screens/onboarding/` — onboarding dạng slide, **chỉ hiện một lần**, nằm giữa Splash và Main
trên host ngoài: `Splash → Onboarding → Main`, mỗi bước đều `navigateClearStack` nên Back không quay
lại được.

- **Cấu trúc một page** (trên xuống): ảnh → title (`headlineMedium`) → description (`bodyLarge`),
  gói trong `OnboardingPage`. Page indicator và hàng Skip / Next / Get Started nằm **ngoài** pager,
  trong `OnboardingContent`, nên chúng đứng yên khi page trượt. Page cuối đổi Next → Get Started và
  ẩn Skip (`isLastPage` quyết định cả label, hành động lẫn việc hiện Skip); Skip và Get Started gọi
  chung `onCompleteClick`. Nút dùng lại `ActionButton(isHighlighted = true, backgroundColor = primary)`
  đúng khuôn `SignInButton`.
- **`OnboardingPageValue`** (`model/value/onboarding/`) giữ `imageRes` + `titleRes` + `descriptionRes`,
  cùng hình dạng với `BottomTabItemValue` — thêm page mới = thêm một entry, không đụng composable.
- **Ảnh là screenshot thật của chính app này**, chụp trên emulator rồi ghép thành mockup điện thoại
  (bo góc + viền + đổ bóng; 2 máy chồng nhau ở page 2-4), lưu **WebP** trong `drawable/`
  (~150 KB/ảnh, PNG gốc ~1.3 MB). Điều phải giữ khi làm lại ảnh: tỉ lệ ảnh ghép phải bám sát khung ảnh
  của pager (**~0.7 w/h**) — bản đầu ghép quá rộng nên `ContentScale.Fit` co lại và chừa một mảng
  trống to phía trên.
- **`drawable/` trần ở đây là ổn, và điều này được đo chứ không phải đoán**: vào onboarding native
  heap chỉ tăng ~5.5 MB với page 1-2 đã compose, khớp mức decode 1:1 (4.5 + 5.4 MB). `painterResource`
  của Compose không nhân density mdpi→xxhdpi 3× như `BitmapDrawable` — nếu có thì riêng một page đã
  ~49 MB. Trong phiên này tôi từng khẳng định ngược lại và bắt để ở `drawable-xxhdpi/`; đo lại thì sai,
  **đừng chuyển ngược lại**.
- **Cờ "đã xem" nằm trong `SettingsRepository`**, không tạo repository mới — vẫn DataStore đó, nên
  `observeIsOnboardingCompleted()` / `saveIsOnboardingCompleted()` nằm cạnh cặp theme và **không phải
  sửa DI**. Kèm 2 use case `ObserveIsOnboardingCompletedUseCase` / `SaveIsOnboardingCompletedUseCase`.
- **`OnboardingUiState.isCompleted` là `Boolean?` có chủ đích**: `null` = chưa đọc xong DataStore, và
  Splash chỉ đi sang Onboarding khi giá trị là `false` tường minh — mọi trường hợp khác đi thẳng Main,
  nên đọc chậm hoặc lỗi không bao giờ nhốt người dùng cũ trong onboarding (`onFailure` cũng set `true`).
- **`SplashScreen` bọc cờ và 2 callback bằng `rememberUpdatedState`** — không phải thừa: nó cần đổi chữ
  ký (`isOnboardingCompleted` + `onNavigateToOnboardingScreen`/`onNavigateToMainScreen` thay cho
  `onNavigateToHome`), mà `LaunchedEffect(Unit)` được compose **trước** khi DataStore emit, nên nếu
  capture param theo cách thường thì sau 3 giây giá trị vẫn là `null` và **mọi** người dùng lần đầu sẽ
  bị bỏ qua onboarding.
- Muốn xem lại onboarding khi dev: `adb shell pm clear com.decoutkhanqindev.dexreader` (không có nút
  reset trong app).

**Verify**: `:app:compileDebugKotlin` BUILD SUCCESSFUL; chạy thật trên emulator Pixel_7_Pro — 4 page
trượt đúng, indicator và Next/Get Started đổi đúng, bấm Get Started xong force-stop rồi mở lại thì vào
thẳng Main.

**Ảnh chốt lại sau vài vòng**: page 4 (`ob_track`) ban đầu ghép tạm từ Manga Details + danh sách
chapter vì Favorites/History/Statistics cần đăng nhập; sau khi có tài khoản thật thì thay bằng
**Profile hub** (Favorites + History có thanh %) ghép với màn **Statistics**. Chụp Profile phải **cuộn
qua khỏi header** — ảnh chưa cuộn dính tên và email thật của người dùng, mà ảnh này ship trong APK.
Page 3 (`ob_read`) cũng chụp lại: bản đầu lấy Manga Details đã cuộn sâu vào danh sách chapter nên mất
hết info manga; bản chốt cuộn vừa đủ để còn cover, tên, tác giả, badge năm/status/rating, Summary,
chip thể loại và đầu mục Chapters.

---

## 2026-08-31 — Sửa `ClassNotFoundException` do ASM transform hỏng + đồng bộ lại CLAUDE.md

Hai việc trong cùng phiên: gỡ một crash lúc khởi động **không phải do code**, và soát lại CLAUDE.md
cho khớp code thật sau đợt refactor bottom nav.

### Crash: `ClassNotFoundException: com.decoutkhanqindev.dexreader.App`

App crash ngay lúc `Unable to instantiate application`, dù `App.kt`/`MainActivity.kt` và
`AndroidManifest.xml` đều đúng và `assembleDebug` luôn BUILD SUCCESSFUL.

- **Nguyên nhân**: state incremental hỏng ở **`transformDebugClassesWithAsm`** (task ASM
  instrumentation của Firebase Perf/Crashlytics), nằm giữa `compileDebugKotlin` và
  `dexBuilderDebug`.
  `built_in_kotlinc/debug/.../App.class` có mặt (nên compile vẫn xanh), nhưng thư mục output của ASM
  `app/build/intermediates/classes/debug/transformDebugClassesWithAsm/dirs/com/decoutkhanqindev/dexreader/`
  **thiếu hẳn `App.class` và `MainActivity.class`** — chỉ còn mấy class Hilt sinh ra (
  `App_*.class`).
  Dex và APK vì thế thật sự không chứa 2 class đó.
- **Fix**: `./gradlew clean` rồi build lại. **`--rerun-tasks` không cứu được** — nó chạy lại task
  nhưng vẫn ghi vào đúng thư mục output cũ.
- **Bẫy khi chẩn đoán**: `grep -a "Lcom/decoutkhanqindev/dexreader/App;"` trên file dex cho
  **false positive** — chuỗi đó xuất hiện như một *reference* từ class Hilt sinh ra kể cả khi class
  gốc vắng mặt. Kiểm tra thư mục output của ASM mới là phép thử quyết định.
- Cùng một gốc này giải thích luôn 2 hiện tượng lạ trước đó trong phiên:
  `Unresolved reference 'Main'`
  khi source hoàn toàn đúng, và KSP
  `FileNotFoundException: ForgotPasswordViewModel_HiltModules.java`.
- Đã ghi thành mục **Build Troubleshooting** trong CLAUDE.md (`## Project Setup`).

### Đồng bộ CLAUDE.md với code thật

4 chỗ doc lệch so với code sau đợt bottom nav, sửa hết:

- **Alpha của scrim bottom bar** là `persistentListOf(0f, 0.9f, 1f, 1f)`, doc đang ghi `0.8f`.
- **`AppBottomBar` không tự trang trí**: nó chỉ nhận `modifier` trần; `blurBackground` →
  `navigationBarsPadding()` → `padding(horizontal = 16.dp, vertical = 8.dp)` đều nằm ở call site
  trong `MainScreen`. Doc cũ đọc như thể bar tự lo phần này.
- **Nút đáy Profile chừa `78.dp`**, không phải `114.dp` như doc ghi. `LogoutButton`
  (`ProfileContent`) và `SignInButton` (nhánh chưa đăng nhập của `ProfileScreen`) dùng chung
  `.padding(horizontal = 16.dp).padding(bottom = 78.dp)` — giữ 2 số này bằng nhau để nút nằm đúng
  một chỗ dù đã đăng nhập hay chưa.
- **Dòng credit bị bỏ hẳn, không phải chuyển chỗ**: doc (và entry bottom-nav phía dưới) nói
  `MenuFooter` dời xuống đáy `ProfileContent`, thực tế không nơi nào render
  `R.string.decoutkhanqindev` nữa ⇒ string resource này đang **mồ côi**. Vô hại, nhưng cần quyết
  định
  rõ: xoá string, hoặc thêm lại dòng credit với style `bodySmall + Italic + onSurfaceVariant` căn
  giữa.
- Tiện thể: chữ ký `NavRoute.CategoryDetails` trong doc bổ sung `categoryDescription: String = ""`
  (thêm ở commit `831a63c`), và mục typography bỏ dòng credit khỏi ví dụ "footer/quiet-caption".

---

## 2026-08-31 — `blurBackground` nhận list alpha thay cho 4 param top/center/bottom

`Modifier.blurBackground` đổi chữ ký từ 4 param cố định
(`topAlpha`/`topCenterAlpha`/`bottomCenterAlpha`/`bottomAlpha`, 2 cái center nullable và fallback về
hàng xóm) sang **một `alphas: ImmutableList<Float>`** — mỗi phần tử là một stop chia đều.

- **Lý do**: bộ 4 param cố định không diễn tả được gradient 2, 3 hay 5+ stop, và mỗi call site phải
  tự
  suy luận slot nào fallback về slot nào. Giờ chỉ là
  `blurBackground(alphas = persistentListOf(0f, 0.1f, 0.8f, 1f))`.
- **`alphas` không có default** — mọi call site tự khai gradient của nó; `color`/`startY`/`endY` giữ
  nguyên default.
- **Migrate 14 call site, giữ nguyên hình ảnh**: số lượng stop *có* ảnh hưởng tới đường cong, nên
  `topAlpha = 0f, bottomAlpha = 1f` cũ (nở thành `[0, 0, 1, 1]`: phẳng — dốc ở khoảng giữa — phẳng)
  được
  ghi lại đúng thành `persistentListOf(0f, 0f, 1f, 1f)`, **không** rút gọn thành `[0f, 1f]` (đó là
  đường dốc tuyến tính khác hẳn). Các trường hợp phẳng đều (`0.7f` cả trên lẫn dưới) rút còn 2 phần
  tử
  vì màu không đổi nên kết quả giống hệt.
- **Lưu ý còn lại**: list là colors của `Brush.verticalGradient` nên **phải có ít nhất 2 phần tử** —
  1 phần tử sẽ ném `IllegalArgumentException: colors must have length of at least 2`.

## 2026-08-31 — Bỏ menu drawer, chuyển sang bottom navigation bar (2 back stack)

Điều hướng chính đổi từ `ModalNavigationDrawer` sang bottom nav bar. Cả package
`presentation/screens/common/menu/` (`MenuDrawer`, `MenuHeader`, `MenuBody`, `MenuItemRow`,
`MenuFooter`) bị **xoá hẳn**. Back stack tách làm hai tầng lồng nhau.

- **Hai `NavHost`**: host **ngoài** (`NavGraph`) giữ `Splash`, 3 màn auth, **`NavRoute.Main` (mới)
  **,
  và mọi màn đi ra từ tab (`MangaDetails`, `CategoryDetails`, `Search`, `Reader`, `Favorites`,
  `History`, `Statistics`); host **trong** (`screens/main/MainScreen.kt`) chỉ có đúng 3 tab
  `Home` / `Categories` / `Profile`. Nhờ vậy mọi màn ở host ngoài **tự động không có bottom bar** —
  không cần một cái `if` nào gác theo destination, bar nằm trong `MainScreen` nên biến mất đúng
  lúc host ngoài điều hướng đi.
- **Bar là overlay, không phải `Scaffold.bottomBar`**: `AppBottomBar`
  (`common/bottom_bar/AppBottomBar.kt`) đặt `Modifier.align(Alignment.BottomCenter)` trong `Box` bọc
  `NavHost` trong, có `blurBackground(topAlpha = 0f, bottomAlpha = 1f)` để nội dung cuộn *xuyên
  dưới*
  lớp gradient — cùng công thức với cụm sort/filter của CategoryDetails và cụm continue-reading/
  favorite của MangaDetails. **Không** dùng `NavigationBar` của Material3: nó tự vẽ container màu
  đặc,
  phá mất scrim. Vì là overlay nên không đẩy content ⇒ mỗi tab tự chừa `bottom = 82.dp`
  (`HomeContent`, `CategoriesGrid.contentPadding`, dòng credit cuối `ProfileContent`), đúng con số
  `CategoryDetailsContent` đã dùng sẵn; nút ở đáy Profile chừa `114.dp` (82 + 32 vốn có).
- **Chuyển tab giữ state**: tab click gọi `navigatePreserveState<NavRoute.Home>(...)`, tức
  `popUpTo<Home> { saveState = true }` + `launchSingleTop` + `restoreState`. Bản kế hoạch ban đầu
  định
  **bỏ** `popUpTo` để Back quay về *tab trước đó*, nhưng như thế `saveState`/`restoreState` không có
  gì
  để móc vào: mỗi lần đổi tab sẽ tạo `NavBackStackEntry` mới ⇒ `ViewModelStore` mới ⇒ refetch và mất
  vị
  trí cuộn.
- **`NavTransitions` giữ `inline`/`reified`, chỉ siết type theo `NavRoute`**: đích điều hướng là
  `route: NavRoute` và type param là `Root`/`T : NavRoute` (không phải `Any`) —
  `NavController.navigate`
  chỉ đòi `Any` nên phần siết này là chủ ý, để truyền nhầm thứ không phải route thành lỗi compile.
  Trong đợt này có thử bỏ generic (đổi đích pop sang `KClass<out NavRoute>` rồi sang `NavRoute`
  value)
  nhưng đã **revert**, vì `popUpTo(route: T, …)` khác `popUpTo(route: KClass<T>, …)` ở 2 điểm: (1)
  nó
  khớp theo **route đã điền đủ args**, nên route data class như `NavRoute.MangaDetails` phải lấy
  đúng
  instance trên back stack qua `entry.toRoute<…>()`; (2) `generateRouteFilled()` **ném
  `IllegalArgumentException`** khi route không nằm trong graph của chính controller đó, trong khi
  nhánh
  reified/`KClass` chỉ log `Ignoring popBackStack …` rồi trả false. Với 2 `NavHost` (route tab chỉ
  có ở
  graph trong, phần còn lại chỉ ở graph ngoài) thì điểm (2) là rủi ro crash thật. Bytecode xác nhận
  bản
  reified đi nhánh `popUpTo:(Lkotlin/reflect/KClass;…)`.
- **Đánh đổi**: `util/NavTransitions.kt` import `presentation.navigation.NavRoute`, tức file util
  này
  cố ý dính vào presentation layer (nó là navigation glue chứ không phải helper dùng chung như
  `CoroutineHandler`/`DateTimeHandler`).
- **Back bị nuốt hẳn trong khu vực tab**: `MainScreen` khai một `BackHandler {}` rỗng, nên đổi tab
  **chỉ bằng cách chạm tab**, Back không nhảy qua lại giữa các tab. Handler này sống theo
  composition
  của `Main` nên bị dispose ngay khi host ngoài điều hướng sang màn con — Back ở
  MangaDetails/Search/Reader vẫn bình thường và vẫn quay về tab. Đánh đổi: Back ở tab root **không
  còn
  thoát app** nữa; nếu cần lại thì gác handler theo `selectedTab != BottomTabItemValue.HOME`.
- **Hoist 3 shared VM lên `NavRoute.Main`**: `FavoritesViewModel`/`HistoryViewModel`/
  `StatisticsViewModel` trước đây scope vào entry của `NavRoute.Profile`. Profile giờ nằm ở host
  **trong** còn 3 màn riêng nằm ở host **ngoài**, nên `getBackStackEntry<NavRoute.Profile>()` gọi
  trên
  controller ngoài sẽ ném `IllegalArgumentException`. Chúng được khai báo thành param
  `= hiltViewModel()` của `MainScreen` (resolve về entry `Main`), rồi host ngoài lấy lại đúng
  instance đó bằng `hiltViewModel(mainEntry)`. Hệ quả: **2 bất biến điều hướng cũ đã hết hiệu lực**
  (3 màn đó không được là drawer item; "More »" phải dùng `navigateTo` chứ không
  `navigatePreserveState`)
  — đã gỡ khỏi CLAUDE.md.
- **`MenuValue` → `BottomTabItemValue`** (`model/value/menu/` → `model/value/bottom_bar/`,
  `git mv`),
  còn đúng 3 entry, bỏ `isDrawerItem`/`drawerItems` vì mọi thứ trong enum giờ *đều* là tab.
  `MenuMapper` → `BottomTabItemMapper`. 3 section header của Profile trước đây mượn
  `MenuValue.FAVORITES/HISTORY/STATISTICS` nay tự khai `Icons.Default.Favorite/History/Timeline` +
  `R.string.*_menu_item`.
- **`BaseScreen` rút gọn mạnh**: bỏ `ModalNavigationDrawer`/`rememberDrawerState`/`coroutineScope`,
  bỏ slot `bottomBar`, bỏ param `isUserLoggedIn`/`currentUser`/`onNavigateToSignInScreen`/
  `onNavigateToMenuItemScreen`. Còn `Scaffold` + `AppTopBar` (title theo `selectedTab`, `rightIcon`
  search tuỳ chọn, **không có left slot**) + `Box` nội dung.
- **Cứu lối vào Sign In**: nút Sign In trước chỉ nằm trong `MenuHeader` của drawer, bỏ drawer là
  user
  chưa đăng nhập mất đường vào Login (lối còn lại duy nhất là dialog "must sign in to favorite" ở
  MangaDetails). Thêm `profile/components/actions/SignInButton.kt` (`ActionButton` +
  `colorScheme.primary` + `Icons.AutoMirrored.Filled.Login`, **không** dialog xác nhận vì đăng nhập
  không phải hành động phá huỷ), đặt đúng vị trí nút Logout ở nhánh chưa đăng nhập của
  `ProfileScreen`.
- **Dòng credit của `MenuFooter`** (`R.string.decoutkhanqindev`) chuyển xuống đáy `ProfileContent`,
  giữ nguyên `bodySmall + Italic + onSurfaceVariant + canh giữa`.
- **Sửa 2 chỗ doc lệch sẵn có**: (1) CLAUDE.md nói `BaseScreen` bọc `AppTopBar` trong
  `Surface(alpha 0.95f)` — thực tế `Surface` nằm **bên trong** `AppTopBar` (`AppTopBar.kt:50`) nên
  mọi
  caller đều có; (2) CLAUDE.md nói `navigateTo`/`navigateBack` tự debounce 500ms —
  `NavTransitions.kt`
  hiện chỉ là wrapper mỏng của `navigate`/`popBackStack`, không có debounce nào.

## 2026-08-31 — Xoá màn Settings, theme picker dọn vào Profile hub

Màn Settings bị **xoá hẳn** (cả `screens/settings/`: `SettingsScreen`, `SettingsContent`,
`ThemeOptionList`, `ThemeOptionItem`), kèm `NavRoute.Settings`, `composable<NavRoute.Settings>`,
`MenuValue.SETTINGS` và nhánh `SETTINGS ->` trong `MenuMapper`. Drawer còn 3 mục: Home /
Categories /
Profile. `SettingsViewModel` + `SettingsUiState` **không sửa dòng nào**, vẫn ở
`common/viewmodels/settings/`
vì `NavGraph` cần `appliedThemeOption` để lái `DexReaderTheme` bất kể UI chọn theme nằm ở đâu; giờ
cùng
instance đó truyền vào `ProfileScreen(settingsViewModel = ...)`.

- **UI cũ bỏ hoàn toàn, không port**: `ThemeOptionList` là `Card` cố định `width(165.dp)` canh
  giữa —
  hợp lý khi nó đứng một mình giữa màn trống, nhưng lạc lõng trong trang cuộn của Profile. Thay bằng
  `ProfileSettingsSection` + `ThemeOptionItem`: một `Row` gồm 3 lựa chọn, mỗi cái `weight(1f)` (
  icon +
  nhãn, canh giữa), chọn/không chọn phân biệt bằng màu `primary` vs `onSurfaceVariant`. Đặt ngay
  trên
  nút Logout — nhóm hành vi tài khoản dồn xuống cuối trang.
- **Nhãn theme rút còn một từ**: string `dark`/`light` bỏ chữ "Mode" → "Dark"/"Light", khớp với
  "System" vốn đã không có. Đây là điều kiện để 3 lựa chọn xếp ngang vừa một hàng trên máy hẹp mà
  không bị cắt chữ.
- **Đổi theme áp dụng ngay khi chạm, bỏ dialog xác nhận + dialog thành công.** Luồng cũ: chạm
  option →
  dialog "Are you sure you want to change the theme?" → dialog success. Hai modal để lật một cái
  theme
  toggle là quá nặng, nhất là khi nằm trong trang profile; app đổi màu ngay chính là phản hồi rồi.
  `ProfileScreen` gọi `updateThemeOption(it)` rồi `saveThemeOption()` liền nhau — an toàn vì
  `MutableStateFlow.update` là đồng bộ nên `saveThemeOption` đọc được giá trị staged vừa set.
- **`resetThemeOption()` vẫn còn tác dụng**: khi ghi lỗi thì snap `selectedThemeOption` về
  `appliedThemeOption`, để radio không đứng ở giá trị chưa từng lưu được. Lỗi hiện **inline** bằng
  `LoadPageErrorMessage` có nút retry, đúng quy ước các section khác của hub (không modal).
- **`SectionHeader.onMoreClick` thành nullable** (`(() -> Unit)? = null`) — section Settings không
  còn
  màn riêng nào để "More »" dẫn tới, nên khi null thì cụm "More »" đơn giản là không render. Các
  call
  site cũ đều dùng named argument nên không vỡ. `SectionHeader` cũng bỏ luôn phần padding tự ôm; giờ
  mọi call site tự truyền `padding(start/end = 16.dp, top = 8.dp, ...)`.
- **`onMoreClick` của 3 section Profile cũng thành optional** (`(() -> Unit)? = null`, đẩy xuống
  cuối
  danh sách param) cho đồng bộ với `SectionHeader`. Đánh đổi: quên wire thì **không còn lỗi compile
  **,
  cụm "More »" chỉ lặng lẽ biến mất.
- **Nhịp giãn cách giữa các section gom về một chỗ**: `Column` cuộn của `ProfileContent` dùng
  `Arrangement.spacedBy(16.dp)`, các section chỉ nhận `Modifier.fillMaxWidth()` trần thay vì mỗi cái
  tự
  mang `padding(top = 8.dp)`.

## 2026-08-30 — Profile hub polish: header ngang, tách `ProfileEditSection`, retune
`ListLoadingIndicator`

Đợt tinh chỉnh tiếp theo của màn Profile hub (xem entry ngay dưới).

- **`ProfileEditSection` tách riêng** vào `profile/components/sections/`, đứng cùng hàng với 3
  section
  kia. `ProfileContent` giờ thuần lắp ráp (4 section + `LogoutButton` + dialog cấp màn), không còn
  giữ
  chi tiết layout nào. Section tự ôm padding của mình, call site chỉ truyền
  `Modifier.fillMaxWidth()`.
  Dọn kèm 10 import chết + biến `val currentUser` không còn ai dùng trong `ProfileContent`.
- **Khối profile chuyển sang layout ngang** soi theo `MenuHeader`: avatar trái, name + email xếp dọc
  bên phải, `spacedBy(16.dp)`; nút Update vẫn nằm dưới hàng như cũ. **Không** cho avatar `weight` —
  `ProfilePicture` vốn cố định `size(80.dp)` nên weight chỉ tạo ô trống thừa quanh nó trên máy rộng;
  để nó wrap 80dp, cột text lấy `weight(1f)`.
- **`ProfileNameEdit` đổi sang `Arrangement.Start`** (bỏ `TextAlign.Center`), Text/TextField lấy
  `weight(1f)` để nút Edit không bị đẩy khỏi màn khi tên dài. Lý do: trong layout ngang, tên canh
  giữa
  nằm cạnh email canh trái trong cùng một cột trông như lỗi. Component chỉ `ProfileContent` dùng nên
  đổi an toàn.
- **Name + email cố ý wrap, không ellipsize** (đã thử `maxLines = 1` + `TextOverflow.Ellipsis` rồi
  bỏ
  theo yêu cầu) — đây là danh tính của chính user, cắt chữ tệ hơn là xuống thêm một dòng.
- **`ListLoadingIndicator` đổi hình dạng** (một `LinearProgressIndicator` đơn, `fillMaxWidth(0.4f)`,
  tự
  canh giữa — thay cho Row 2 thanh + icon 36dp trước đây) → **retune cả 9 call site**: bỏ hết
  padding
  ngang (bar không bao giờ chạm mép, padding chỉ làm nó ngắn đi), và ở các slot load-more thì cho
  padding dọc **khớp với nhánh `IDLE` cùng chỗ**. Không làm bước sau thì hàng LOADING tụt từ ~44dp
  xuống ~16dp và list giật nảy ngay khi bấm "Load More". Giữ nguyên `bottom = 82.dp` ở
  `CategoryDetailsContent` vì đó là khoảng chừa cho cụm nút Sort/Filter nổi, không liên quan
  indicator.

## 2026-08-30 — Profile screen → user hub (favorites + history + stats + logout)

Gom 4 màn user-centric vào 1 màn Profile cuộn dọc: profile edit → top-5 Favorites → top-5 History →
month chart → nút Logout. 4 màn cũ (Favorites / History / Statistics) **giữ nguyên không đổi** — "
More »"
ở mỗi section chính là đường vào chúng.

`FavoritesViewModel` / `HistoryViewModel` / `StatisticsViewModel` (+ `RemoveFromHistoryUiState`,
`StatisticsUiState`) **chuyển sang `common/viewmodels/{favorites,history,statistics}/`**, tạo bằng
`hiltViewModel()` **trong `composable<NavRoute.Profile>`** nên scope theo `NavBackStackEntry` của
Profile;
3 màn riêng lấy lại đúng instance đó qua
`hiltViewModel(remember(it) { navController.getBackStackEntry<NavRoute.Profile>() })`. Mục đích:
Profile
đã load rồi thì bấm "More »" sang màn riêng **không load lại**. 3 màn riêng nhận `viewModel:` là *
*param
bắt buộc** (không có `= hiltViewModel()`) để không ai vô tình tạo instance thứ 2.

**Hai điều kiện bắt buộc để không crash** (`getBackStackEntry` ném `IllegalArgumentException` nếu
destination không có trên stack):

1. **Favorites/History/Statistics bị gỡ khỏi drawer** (`MenuValue.isDrawerItem = false`,
   `MenuDrawer`
   render `MenuValue.drawerItems`) — Profile thành lối vào duy nhất, đảm bảo Profile luôn được tạo
   trước.
   Trước đó 3 mục này là tab ngang hàng trong drawer, vào thẳng từ Home thì Profile chưa hề tồn
   tại → crash.
   Kèm theo, 3 màn này chuyển từ `BaseScreen` (icon Menu + drawer) sang **`BaseDetailsScreen` (icon
   Back)**
   — giữ hamburger mở ra cái drawer không còn chứa chính nó là ngõ cụt. Đổi luôn signature: bỏ
   `onNavigateToMenuItemScreen` / `onNavigateToLoginScreen` (chỉ phục vụ drawer), thêm
   `onNavigateBack`.
2. **"More »" dùng `navigateTo`, không dùng `navigatePreserveState`** —
   `navigatePreserveState<Home>` làm
   `popUpTo<Home> { saveState = true }`, pop luôn Profile khỏi stack ngay khi sang Favorites → cũng
   crash.

`MangaSectionViewModel` thì ngược lại: scope vào chính entry của Home bằng `hiltViewModel()` default
trên
`HomeScreen`, **không** share. Không thể scope vào `NavRoute.Splash` dù Splash đứng trước Home, vì
`navigateClearStack<Splash>(Home)` pop Splash `inclusive = true` → `ViewModelStore` của Splash bị
clear
đúng lúc Home cần. Scope-vào-entry-cha chỉ dùng được khi entry cha chắc chắn còn trên back stack.

**Cả 3 VM không sửa một dòng logic nào** — API public sẵn có (`updateUserId` / `refresh` /
`retry*` /
uiState) đã đủ. "Top 5" chỉ là `.take(5)` ở tầng composable vì VM vốn đã fetch page đầu 20 item →
**không đụng tới domain/data layer, không thêm use case, không thêm param `limit`**.

- `SectionHeader` (`common/sections/`) — tách nguyên si header "icon + title + More »" đang inline
  trong Home's `MangaListSection`; Home refactor gọi lại nó (pure extraction, visual không đổi), 3
  section mới của Profile dùng chung. Tránh 4 bản copy của cùng một affordance.
- `ProfileFavoritesSection` — `LazyRow` các `FavoriteMangaItem` có sẵn, size `194×250dp` giống hệt
  `HorizontalMangaList` của Home.
- `ProfileHistoryItem` + `ProfileHistorySection` — biến thể ngang của history item (`300×184dp`),
  tái
  dùng `ReadingHistoryInfo` nguyên vẹn nên `ReadingProgressBar` có sẵn. **Bỏ `SwipeToDismissBox`**
  vì
  swipe-ngang-để-xoá đánh nhau trực tiếp với scroll ngang của `LazyRow` — xoá history vẫn làm ở màn
  History đầy đủ. Đây là khác biệt hành vi duy nhất so với màn gốc, có chủ đích. Tap item → đúng
  dialog
  2 lựa chọn (Manga details / Continue reading) copy từ `HistoryContent`.
- `ProfileStatisticsSection` — `ReadingActivityChart` với `monthlyBreakdown`. Không thêm code zoom:
  đã verify trong source Vico `CartesianChartHost.kt` rằng `zoomState` mặc định là
  `rememberDefaultVicoZoomState(...)` với `zoomEnabled = true`, tức pinch-to-zoom vốn đã hoạt động.
- `UpdateAndLogoutUserBottomBar` bị **xoá**, tách thành `UpdateProfileButton` (inline ngay dưới
  khung
  edit, `AnimatedVisibility` khi có thay đổi chưa lưu) và `LogoutButton` (cuối trang) — `BaseScreen`
  của Profile không còn `bottomBar`.
- Lỗi từng section render **inline** bằng `LoadPageErrorMessage` trong `Box` cao cố định, **không
  dùng
  modal `AlertDialog`** — 3 section load song song, modal sẽ chồng lên nhau; box cao cố định cũng
  chặn
  layout nhảy khi từng section resolve xong. Loading của section dùng `ListLoadingIndicator` (thanh
  load-more mảnh), **không dùng `LoadingScreen`** — `LoadingScreen` là treatment cả màn, đặt trong 1
  section sẽ đọc thành "cả trang đang load", và 3 section resolve độc lập thì thành 3 spinner
  full-screen
  xếp chồng dọc trang.

Pull-to-refresh refresh cả 3 section một lượt. Nhánh chưa đăng nhập giữ nguyên `IdleScreen` như cũ.

**Ghi chú build**: lần compile đầu fail với hàng loạt "Unresolved reference" ngay trên các dòng
`import` — nguyên nhân là 2 build Gradle chạy đè nhau (`Detected multiple Kotlin daemon sessions`)
do
một build nền còn treo, **không phải lỗi code**. `./gradlew --stop` rồi build lại `--rerun-tasks` (
11/11
task chạy thật) là sạch.

## 2026-08-30 — LaunchedEffect → SideEffect migration (23 files)

Bumped `composeBom` from `2026.05.00` to `2026.08.00` (`compose-runtime 1.11.1` → `1.12.0`) to pick
up
the new keyed `SideEffect(key1, ..., effect)` overloads, then migrated every `LaunchedEffect` call
site
in the codebase whose body was fully synchronous (no `delay`, no `.animateTo`/`.collect`, no calling
a `suspend fun`) over to it — 23 files, ~27 call sites. Verified against the actual `compose-runtime
1.12.0` sources (not just the announcement blog post) before touching anything: `SideEffect`'s keyed
overloads take `effect: () -> Unit` (no `CoroutineScope`), so the swap only applies to effects that
never needed the coroutine in the first place — mostly the "flip a dialog-visibility flag when a
UiState field changes" pattern and the "`viewModel.updateUserId(...)` on login-state change" pattern
that opens nearly every `*Screen.kt`, both confirmed to call only plain (non-suspend) functions.

5 files were deliberately left on `LaunchedEffect` because their bodies do real suspend work:
`AnimatedLogoAndSlogan` (`Animatable.animateTo`), `SplashScreen` (`delay`), `MangaBanner` (two
effects,
`delay`/`animateScrollToPage`/`animateTo`), `ReadingActivityChart` (Vico's
`modelProducer.runTransaction`
is suspend). `ChapterPagesSection`'s
`LaunchedEffect(pagerState.currentPage) { onUpdateChapterPage(...) }`
is technically synchronous too but was held back from this pass for separate testing — the Reader's
`HorizontalPager` already has documented fragile unmount/remount timing dependencies elsewhere in
this
file (see the "Forcing a same-screen `HorizontalPager` jump" note below), so it isn't being batched
with the safe, mechanical majority of this migration.

## 2026-08-24 — Yearly reading-activity chart + non-atomic increment bugfix

Added a 3rd chart to Statistics: `ReadingStats.buildYearlyBreakdown(list): List<YearlyReadingStat>`
(new companion on `ReadingStats`, same shape/reasoning as `buildMonthlyBreakdown` — groups the
entire
stats history by 4-char `yyyy` date-prefix instead of 7-char `yyyy-MM`) feeds a 3rd
`ReadingActivityChart` call in `StatisticsContent`, with the "Total Reading Time" caption moved from
under the monthly chart to under the yearly one (a grand-total figure reads more naturally next to
the
most macro chart). `YearlyReadingStat(year, durationMillis)` (domain) and `StatisticsMapper.
toYearlyChartPoint()` (presentation) are the only new files — everything else is pure client-side
aggregation over the exact same `ObserveStatisticsUseCase` result the weekly/monthly charts already
use.

**A persisted-Firestore-rollup design (new `/statistics_yearly/{userId}_{year}` collection, a 2nd
`observe*` use case, a batched atomic write, new security rules) was fully implemented and then
reverted** after direct pushback questioning why a client-side aggregation (like monthly) wasn't
enough — it wasn't necessary at this app's usage scale, and added a second Firestore collection +
rules to keep in sync for no problem that currently exists. Revisit that design specifically if
`observeStatistics`'s unbounded/unpaginated full-history read ever becomes an actual measured cost,
not
by default. See the corresponding CLAUDE.md note under "Business logic in companions" for the full
reasoning trail — worth reading before reaching for a persisted rollup on the next time-bucketed
chart.

Also fixed a real bug found while tracing that write path: `FirebaseStatisticsFirestoreSourceImpl.
incrementReadingDuration` (called every 15s by `ReaderViewModel`'s reading timer) was doing a
**read-then-write** (`.get()` the existing doc, add the delta in Kotlin, `.set()` a full new
`ReadingStatsRequest`) — not atomic (races under concurrent writers), and cost 1 read + 1 write per
tick instead of 1 write. Switched to a real `FieldValue.increment()` partial write via
`SetOptions.merge()` on a raw `Map<String, Any>` — this is what CLAUDE.md's "Firestore paths"
section
already documented as the design intent, but the code never actually did it until now.
`ReadingStatsRequest.kt` (the now-fully-unused typed write DTO) was deleted; `FirestoreFields`
gained
`DATE`/`DURATION_MILLIS` constants for the map keys (previously only inline `@PropertyName` string
literals on the DTOs, per CLAUDE.md's now-corrected claim that map keys go through
`FirestoreFields`).

## 2026-08-24 — Statistics screen redesigned with Vico charts

Replaced the Statistics screen's 3 plain text `StatCard`s entirely with 2 charts using
[Vico](https://github.com/patrykandpatrick/vico) (`com.patrykandpatrick.vico:compose-m3:3.3.0` —
confirmed the actual latest stable release directly against Maven Central's `maven-metadata.xml`
rather than trusting a summarized doc fetch): a **weekly** column chart (last 7 days) and a
**monthly** column chart (the user's entire reading history, grouped by `yyyy-MM`) — every
`ReadingStats` record returned by Firestore now ends up represented in a chart, not just summed into
a flat "Total" number. This required bumping `compileSdk` from 36 to 37 in `app/build.gradle.kts`
(Vico's `compose-android` artifact requires it); AGP 9.1.0 prints an advisory "unsupported compile
SDK
37" warning at build time, which is expected and non-fatal.

Also fixed a real bug found while touching this code: `StatisticsViewModel.calculateStats()` was
assigning `weeklyTimeMillis = totalTime` — the "weekly" stat was silently identical to the all-time
total, never actually filtered to the last 7 days.

- `ReadingStats` (domain) gained `getLastDates(days = 7)` and `buildWeeklyBreakdown(list)` (
  zero-fills
  any day in the last 7 with no Firestore record, so the weekly chart always renders exactly 7
  columns) and `buildMonthlyBreakdown(list)` (groups the full history by `yyyy-MM`, summed and
  sorted
  oldest→newest; falls back to a single zero-duration entry for the current month if the list is
  empty
  — a `ColumnCartesianLayerModel` series can never be empty). New `MonthlyReadingStat(month,
  durationMillis)` domain entity for the latter's return type.
- New generic `ReadingChartPointModel(id, label, minutes)` (presentation) + `StatisticsMapper`
  gained
  `toWeeklyChartPoint()` (weekday label) and `toMonthlyChartPoint()` (month label) — both map onto
  the
  same model so one chart composable can render either granularity.
- New `ReadingActivityChart.kt` (`statistics/components/`, replaces the single-purpose
  `WeeklyReadingChart.kt` from the same day) — a reusable Vico `CartesianChartHost` column chart
  driven
  by `dataPoints: ImmutableList<ReadingChartPointModel>`, minutes (" m" suffix) on the Y axis,
  themed via
  `ProvideVicoTheme(rememberM3VicoTheme())` so it tracks the app's light/dark
  `MaterialTheme.colorScheme`
  automatically, plus a tap-to-reveal marker showing the exact minute count per bar (ported from
  Vico's
  own sample `rememberMarker()` helper). `StatisticsContent` renders it twice — once for
  `weeklyBreakdown`, once for `monthlyBreakdown`.
- `StatCard.kt` deleted outright — no longer used anywhere. `StatisticsContent`'s `Success` branch
  is
  now `verticalScroll`-wrapped: title → weekly chart (with small "Daily/Weekly Reading Time" caption
  text above it, not a boxed card) → monthly chart (with a "Total Reading Time" caption).

## 2026-08-24 — Pull-to-refresh on every list/data screen

Home and Categories already had `PullToRefreshBox`; added the same pattern to the 6 remaining
list/data screens for consistency: Search (results only, not suggestions), Favorites, History,
CategoryDetails, Statistics, MangaDetails (info + chapter list). `ProfileContent` was left alone —
it's
a form, not a list.

Every `*Content.kt` now wraps its outermost root in `PullToRefreshBox` (`isRefreshing = false`
hard-coded, matching Home/Categories — the spinner isn't wired to a real loading flag) and takes a
new
required `onRefresh: () -> Unit` param threaded from its Screen. Wiring reused each ViewModel's
public
first-page fetch where one existed (`SearchViewModel.fetchMangaListFirstPage()`); everywhere else
added
a public `fun refresh() = xxxFirstPage()` wrapper around the existing private fetch — never the
existing conditional `retry()`/`retryXxx()` functions, since those only refetch on Error and would
no-op on pull-to-refresh from a Success state.

Two screens needed real restructuring, not just a wrap: **Favorites** had no outer `Box` around its
`when`, so one was added; **Statistics** had no root container at all (`when` was function-root,
each branch set its own `modifier`) — added `PullToRefreshBox` as the new root and normalized every
branch to `Modifier.fillMaxSize()`.

**Search** — `PullToRefreshBox` has no `contentAlignment` param (unlike the plain `Box` it replaced
in
`ResultsSection`), so the empty-results message needed its own `Box(fillMaxSize, contentAlignment =
Center)` wrapper to keep its centered position.

**MangaDetails** is the one deliberate exception: `refresh()` calls the same 5 fetches as the
existing
`retry()` but unconditionally, and — unlike every other screen — does *not* flash the page to a
`LoadingScreen`, because `fetchMangaDetails()`/`fetchFirstChapter()` don't reset
`mangaDetailsUiState`
before fetching. This is correct here specifically: the page has a full-bleed cover-art background
behind a `LazyColumn`, so wiping to a generic loading screen would hide it and cause a jarring flash
on
every pull-to-refresh. Every other screen's `refresh()` inherits the "flash to Loading" behavior for
free, since their existing first-page fetchers already reset state before fetching.

`compileDebugKotlin` clean (including a full `--rerun-tasks` pass, not just incremental).

---

## 2026-08-04 — Categories redesign v2: genre cover-card grid (supersedes v1 carousels)

v1's per-genre carousels (below) were rejected: each carousel is really "one category + its list" (a
mini-CategoryDetails), so the screen focused on one category at a time. The Categories screen's job
is to
let users **scan many categories and pick one** — the deep-dive is `CategoryDetailsScreen`. Web
research
(Webtoon's Genres grid, Crunchyroll Manga) + a user pick settled on the **Netflix/Webtoon genre-tile
**
pattern.

`CategoriesContent` is now a **`LazyVerticalGrid`** (2 columns, full-span type headers) grouped by
Genre/Theme/Format/Content. Every category is a **`CategoryCard`** — a ~3:4 tile with its #1
TRENDING
cover as a cropped background + scrim + name overlay; tap → `CategoryDetails(TRENDING)` (so the
detail's
first item matches the card). Covers load **lazily per card** (`LaunchedEffect`), VM-deduped, held
in a
separate `categoryCoverStates` flow; empty/error → a named, tappable fallback tile (a category is
never
hidden).

**Efficiency:** a card needs one cover, so `GetMangaListUseCase` gained `limit` + `includeStats`
params
(defaulting to the old behavior, so browse callers are unchanged); the cover fetch calls it with
`limit = 1, sortCriteria = TRENDING, includeStats = false` and takes the first cover — **1
stats-less
call, tiny payload** (`includeStats = false` skips the `MangaStatsRepository` merge; also added a
`limit`
param to `CategoryRepository.getMangaList`). New `CategoryCoverUiState`; deleted v1's
`FeaturedCategorySection`, `CategoryPreviewUiState`, and
the old chip components (`CategoryTypeSection`/`CategoryTypeHeader`/`CategoryList`); consolidated
the
Categories nav to one card→CategoryDetails callback. Verified on emulator. `compileDebugKotlin`
clean.

## 2026-08-04 — Categories screen redesign: "Featured" genre preview rows

The Categories screen was a flat list of chips grouped by type — you couldn't tell what a category
held
without tapping in. Added a **Featured** section at the top: ~12 popular-genre preview rows in the
Home-section style (header + horizontal manga row + "More »"), over the existing chip browse (now
labelled **All Categories**).

**No new use case or API** — reuses `GetMangaListUseCase(categoryId, sortCriteria = TRENDING)` (the
generalized browse from the prior feature) for each preview; `HorizontalMangaList`,
`shimmerLoading`, and
the `MangaListSection` header pattern are all reused. Featured genres are matched from a curated
title
list against the fetched Genre group in `CategoriesViewModel`.

**Lazy + cached per row:** each `FeaturedCategorySection` fires its own fetch via
`LaunchedEffect(category.id)` only when it scrolls into view; the VM dedups so a row never
refetches —
avoids a 12-call burst against MangaDex's rate limit. Previews are held in a separate
`categoryPreviews: StateFlow<ImmutableMap<String, CategoryPreviewUiState>>` so one row resolving
doesn't
recopy the tag map. New `CategoryPreviewUiState` (Loading→shimmer skeleton / Success→
`HorizontalMangaList`
/ Error→inline retry; empty hides the row).

**Preview matches its detail** (same lesson as the Home fix): previews sort by TRENDING and "More »"
opens
`CategoryDetails(…, initialSortCriteria = TRENDING)`, so the row's items equal the detail's first
page —
and TRENDING (most-followed) shows the recognizable hits, which is the whole point. Covers open
MangaDetails (new `CategoriesScreen` nav callback). `CategoriesContent` became a `LazyColumn`; the
chip
components (`CategoryTypeSection`/`CategoryTypeHeader`/`CategoryList`) are unchanged.
`compileDebugKotlin`
clean.

---

## 2026-08-04 — Drop redundant `remember` around composable callbacks (strong skipping)

The codebase wrapped every ViewModel callback passed into a `*Content` composable in
`remember { viewModel::method }` — a convention from before strong skipping was default. On Kotlin
2.3.21 strong skipping is on (nothing in `composeCompiler {}` disables it) and auto-memoizes lambdas
& method references with unstable captures at a `@Composable` call site, so those wraps are
redundant.

Converted **51 keyless sites across all 14 `*Screen.kt`** (50 `remember { viewModel::method }` + 1
`remember { { viewModel.updateChapterPage(it) } }`) to plain `{ viewModel.method() }` lambdas — the
lambda-literal form is *unambiguously* memoized, so it sidesteps any method-reference uncertainty
(chosen over bare `viewModel::method` for that reason). Removed the now-unused
`import androidx.compose.runtime.remember` from the 10 screens that had no other `remember` usage.

**Deliberately kept** (not lambda memoization / not in `@Composable` scope): all
`remember(key) { … }`
— per-item list callbacks inside `items { }` (`MangaItem`, `FavoriteMangaItem`,
`ReadingHistoryItem`,
`MangaChapterItem`, `CategoryList`, …), Coil `ImageRequest` builders, `associateBy` maps,
`derivedStateOf`, `mutableStateOf`, and the keyed `remember(isUserLoggedIn, isFavorite)` /
`remember(chapterPagesUiState)` in MangaDetails/Reader. Updated the stale CLAUDE.md rule
(`viewModel::method` is NOT auto-memoized → the opposite is now true) with the scope caveats.
`compileDebugKotlin` clean.

---

## 2026-08-04 — Home section "More »" → full CategoryDetails browse (generalized by optional tag)

Each Home section (Trending / Latest Update / New Release / Top Rated) showed a fixed ~20-item
horizontal row with no way to browse the full list. Added a per-section **"More »"** control that
opens
the existing `CategoryDetailsScreen` with its full feature set (infinite-scroll load-more, sort,
filter), seeded to that section's sort.

**Key realization:** a Home section is nothing but a preset sort criterion over the whole catalog
with
no tag filter — the four section endpoints and `getMangaListByTag` all hit the same `GET /manga`,
differing only by `includedTags[]`, and Retrofit omits a null `@Query`. So instead of a parallel
use case/screen, the browse was **generalized end-to-end on a nullable tag**:

- **Data:** `ApiService.getMangaListByTag` → `getMangaList`, `tagId: String? = null` (non-null path
  byte-identical); `CategoryRepositoryImpl` override renamed + nullable `categoryId`.
- **Domain:** `CategoryRepository.getMangaList(categoryId: String? = null, …)`; moved/renamed
  `GetMangaListByCategoryUseCase` (`usecase/category/`) → `GetMangaListUseCase` (`usecase/manga/`),
  nullable `categoryId`.
- **Navigation:** `NavRoute.CategoryDetails` gained `categoryId: String? = null` +
  `initialSortCriteria: MangaSortCriteriaValue = LATEST_UPDATE` (enum now `@Serializable` for
  type-safe nav); the two existing call sites (Categories, MangaDetails) switched to named args.
- **Presentation:** new `MangaSectionValue.toSortCriteriaValue()` in `CriteriaMapper` (1:1, with
  `NEW_RELEASE → MOST_VIEWED` since both are createdAt-ordered); `CategoryDetailsViewModel` seeds
  criteria from the route + passes the nullable id; `MangaListSection` header became a
  `SpaceBetween`
  row with a `Modifier.onClick` "More »" (label + auto-mirrored arrow), threaded through
  `HomeContent` → `HomeScreen` → `NavGraph`; new `more` string.

**Decisions (confirmed with user):** generalize the shared path rather than add a parallel use case.
The section browse's **first page matches the Home row byte-for-byte**: the VM seeds criteria per
entry
point via `CategoryDetailsCriteriaUiState.forSection(sortCriteria)` when `categoryId == null`, which
reproduces the section endpoint's exact query — no status + no content-rating filter (empty list ⇒
Retrofit omits the `@Query` ⇒ server default), except `LATEST_UPDATE` (status = Ongoing). Category
entry keeps the plain `CategoryDetailsCriteriaUiState()` Ongoing+Safe default. (First cut wrongly
gave
the section browse the category default, so its first page loaded 20 *different*, narrower items
than
the Home row — fixed by `forSection`.) `compileDebugKotlin` clean.

---

## 2026-07-26 — Standardize the Statistics feature to match Favorites/History conventions

`data/network/firebase/firestore/statistics/` was added later (commit `00a2f30`, different author
than
whatever established the favorite/history pattern) and never brought in line with the rest of the
codebase's Firebase conventions. A full-stack trace found the drift concentrated entirely in the
data
and presentation layers — domain (`ReadingStats`, `StatisticsRepository`, both use cases) and DI
(`RepositoryModule`, `FirebaseModule`) were already fully compliant, no changes needed there.

**Data layer:** removed 5 `Timber.tag("StatisticsDebug")` debug calls from
`FirebaseStatisticsFirestoreSourceImpl`; replaced raw string field-name literals (`"user_id"`,
`"date"`, `"duration_millis"`) with new `FirestoreFields` constants; deleted
`ReadingStatsRequest` (zero callers, and structurally unable to represent the feature's only write —
`FieldValue.increment()` can't be assigned to a `Long`-typed DTO field in Kotlin, so the raw
`Map<String, Any>` write stays, just with constants instead of literals); added `ReadingStatsMapper`
(matching the `object` + extension-function shape every other mapper in this codebase uses,
confirmed
against `FavoriteMangaMapper`); fixed `StatisticsRepositoryImpl.incrementReadingDuration` using the
wrong exception mapper (`toFirebaseFirestoreFlowException()` on a suspend write — CLAUDE.md's own
table says suspend writes use `toFirebaseFirestoreException()`); added the missing
`.distinctUntilChanged()` in the exact operator position confirmed from `FavoritesRepositoryImpl`
(after `.flowOn`, not before `.catch`).

**Deliberately NOT changed:** the flat top-level `/statistics/{userId}_{date}` collection (vs.
Favorites/History's nested `/users/{userId}/...`) — migrating would orphan existing production
documents with no migration tooling in this repo to handle it; confirmed with the user before
finalizing the plan and kept as-is.

**Presentation layer:** converted `StatisticsUiState` from a flat data class to a
`Loading`/`Success`/`Error` sealed interface (matching `CategoryListUiState`, the closest sibling: a
single non-paginated resource load) and rewrote `StatisticsViewModel` to match
`CategoriesViewModel`'s shape — failures now surface into UI state via
`ErrorMapper.toFeatureError()`
instead of only being Timber-logged, and the 5× per-step debug logging collapsed to one error-path
log
tagged with `this::class.java.simpleName`. Split the single 117-line `StatisticsScreen.kt` into a
thin
`StatisticsScreen.kt` + `components/StatisticsContent.kt` (owns the `when(uiState)` dispatch and the
established error-dialog `remember`/`LaunchedEffect` pattern) + `components/StatCard.kt`, matching
the
`*Screen.kt`/`*Content.kt` split used everywhere else in this codebase.

**Explicitly flagged, not fixed** (found while tracing, unrelated to Firestore style):
`StatisticsViewModel.calculateStats()` sets `weeklyTimeMillis` to the same value as
`totalTimeMillis`
— not actually filtered to a 7-day window. Preserved as-is; raise separately if it should be fixed.

**Verified:** `./gradlew compileDebugKotlin` after each of the 6 change batches (BUILD SUCCESSFUL
throughout). Caught and fixed one mistake in the process: the `StatCard` composable was initially
reconstructed from memory using its pre-session `headlineSmall` style instead of the `titleLarge` it
was already converged to earlier in the same session's typography-unification pass — corrected
before
the final compile by re-reading the live file instead of trusting recall.

---

## 2026-07-26 — Unify text styling (size/style/weight/color) across the whole app

A full-codebase audit of every `Text(...)` call in `presentation/screens/` (101 sites, 54 files)
found the same semantic role styled 2-5 different ways depending on which file's author wrote it,
plus 8 call sites across 7 files using Material3's `headlineX`/`displayX` styles, which were never
customized in `Type.kt` and silently rendered in the wrong font family (stock Android default
instead of the app's custom `JsFont`) — a real bug, not just an inconsistency.

**Fix, by category:**

- `Type.kt`: promoted `titleLarge`'s baked-in weight `Bold` → `ExtraBold` (every real call site
  already overrode it to ExtraBold anyway, so this is the single canonical "header" style now with
  no per-call override needed) and newly customized `headlineLarge`/`headlineMedium` with `JsFont`
  at Material3's own stock sizes (32sp/40sp and 28sp/36sp respectively, verified against the actual
  M3 1.4.0 default type scale rather than assumed) — these two stay reserved for hero/branding text
  that should render bigger than an ordinary header, confirmed with the user before implementing
  (the alternative of collapsing everything to `titleLarge`'s 22sp was explicitly rejected).
- Deleted ~15 now-redundant `fontWeight = FontWeight.ExtraBold` overrides made unnecessary by the
  `titleLarge` promotion, plus 3 no-op `fontWeight = FontWeight.Medium` overrides on `labelMedium`
  (already the token's own default).
- Deleted dead code: `manga_details/components/info/MangaInfo.kt` (unreferenced outside its own
  `@Preview`) — its `previewManga` fixture, which 3 *other* files' previews actually depended on via
  an `internal val` cross-package import, was moved into `MangaInfoSection.kt` (same package) first.
- Converged: chip/badge label weight (`MangaGenreChip`, `MangaInfoSection`'s `InfoChip`, Bold→Black
  to match their siblings); the Favorite button's hardcoded `Color(0xFFE0245E)` formalized as a
  named
  `FavoriteRed` constant in `Color.kt` (kept as a deliberate non-theme-adaptive brand color, per the
  user's choice, not switched to `colorScheme.error`); `MangaBanner`'s "Read Now" CTA (was
  `labelLarge`+Bold, now matches every other button's `titleMedium`+ExtraBold); 5 empty-state/
  pagination/not-found messages onto `titleMedium + Italic + Center` with no weight override;
  chapter/volume identifier + chapter-row subtitle text across `MangaChapterItem`/
  `ReadingHistoryInfo`/`ReaderScreen`; `MangaInfoSection`'s hero title and `StatisticsScreen`'s stat
  number off the uncustomized `headlineSmall` onto `titleLarge`; footer/quiet-caption text (menu
  drawer email, app-credit line, Login's "Don't have an account?") onto `Italic + onSurfaceVariant`,
  explicitly leaving `LoginForm`'s clickable "Forgot Password?"/"Sign Up" links alone since their
  color signals tappability rather than de-emphasis.

**Verified:** `./gradlew compileDebugKotlin` after each of the 11 change batches (BUILD SUCCESSFUL
throughout, including recovering from the `previewManga` cross-file dependency the initial dead-code
audit missed).

---

## 2026-07-26 — Merge MainTopBar + DetailsTopBar into one generic AppTopBar

`MainTopBar` (used by `BaseScreen`'s 7 tab-root screens) and `DetailsTopBar` (used by
`BaseDetailsScreen`'s 2 screens + `ReaderScreen` directly) were near-twin composables — both wrapped
a `CenterAlignedTopAppBar` with a title, one left icon, and one optional right icon — but took
different-shaped params. Replaced both with a single `AppTopBar`
(`presentation/screens/common/top_bars/`): `leftIcon`/`rightIcon` are `ImageVector?` (presence is
the only visibility switch, no `isEnabled`-style boolean), no content-description param (both icons
render `contentDescription = null`), colors are 4 flat `Color` params instead of a bundled
`TopAppBarColors`, and the `actionsContent` custom-slot escape hatch is gone entirely —
`ReaderScreen`'s conditional reset-progress icon is now just `rightIcon = if (...)
Icons.Default.RestartAlt else null`.

**Verified via decompiled M3 1.4.0 sources** (not just inference) that dropping the manual
`tint = MaterialTheme.colorScheme.primary` on `MainTopBar`'s icons in favor of baking
`leftIconContentColor`/`rightIconContentColor` into the merged component's `colors` argument
reproduces identical rendering — `IconButton`'s default colors and `Icon`'s default `tint` both
resolve through `LocalContentColor`, which `CenterAlignedTopAppBar` provides from exactly those
`colors` fields (`DetailsTopBar`'s shipping code already relied on this same mechanism).

**Fix:** created `AppTopBar.kt` + 5 previews; migrated `BaseDetailsScreen.kt`, `ReaderScreen.kt`,
`BaseScreen.kt` (Menu variant now wraps its own `AppTopBar(...)` call in a local translucent
`Surface`, since that wrapping was only ever needed by this one caller); deleted `MainTopBar.kt`/
`DetailsTopBar.kt`. Kept `BaseScreen`/`BaseDetailsScreen` as two separate composables — their only
real difference is `BaseScreen`'s `MenuDrawer` wrapper, a genuine compositional fork, not a
parameter to generalize. Also fixed a pre-existing stale CLAUDE.md claim in the same section: only
2 screens (`MangaDetailsScreen`, `CategoryDetailsScreen`) sit behind `BaseDetailsScreen`, not 4 —
`ForgotPasswordScreen`/`RegisterScreen` have no top bar at all.

**Verified:** `./gradlew compileDebugKotlin` → BUILD SUCCESSFUL after every step (per-file
migration order: create `AppTopBar.kt` → `BaseDetailsScreen.kt` → `ReaderScreen.kt` →
`BaseScreen.kt` → delete old files).

---

## 2026-07-12 — Fix shimmer bleeding into a square instead of following its shape

`shimmerLoading`/`shimmerHighlight` (from the migration below) draw their gradient sweep with
`DrawScope.drawRect(brush = ...)`, which always fills the full rectangular draw bounds of the node —
it has no awareness of the actual (possibly circular/transparent) visual silhouette of the content
underneath. Any call site without its own external `.clip()` positioned *after* the shimmer modifier
in the chain showed the sweep bleeding out as a visible square/rectangle around a round icon —
reported by the user via a screenshot of the Login screen logo.

**Root cause confirmed via Compose's modifier-chain draw-ordering semantics** (earlier modifiers
wrap/constrain later ones): `AnimatedLogoAndSlogan.kt` (Splash) and `AuthHeader.kt`
(Login/Register/ForgotPassword) had no clip at all — the exact bug in the report.
`ListLoadingIndicator.kt`
had the same gap, less noticeable at 36dp. `MangaBanner.kt` had a `.clip()` but positioned *after*
`shimmerLoading` in the chain, so it never bounded the shimmer's own draw. `MangaItem.kt` /
`FavoriteMangaItem.kt` / `ReadingHistoryItem.kt` were safe as-is — their ancestor `Card`'s
shape-clip
already hierarchically bounds everything drawn inside it. `LoadingScreen.kt` had already been fixed
independently (`.clip(CircleShape).shimmerLoading()`).

**Fix:** added a `shape: Shape? = null` param to both `shimmerLoading` and `shimmerHighlight` in
`common/Modifiers.kt`, applying `.clip(shape)` as part of each modifier's own self-contained chain,
right before its `drawWithContent { }`. Updated call sites: `AnimatedLogoAndSlogan.kt` and
`AuthHeader.kt` → `shape = CircleShape`; `ListLoadingIndicator.kt` → `shape = CircleShape`;
`MangaBanner.kt` → `shape = MaterialTheme.shapes.medium` passed directly to `shimmerLoading`,
removing
the now-redundant standalone `.clip(MaterialTheme.shapes.medium)` line (and its now-unused `clip`
import) that used to sit after it in the chain.

**Verified:** `./gradlew compileDebugKotlin` → BUILD SUCCESSFUL, then confirmed visually on-device
(`Pixel_10_Pro_XL` emulator) — Login screen logo now shows a clean circular glow tightly following
the
icon's silhouette, no square bleed.

**Cleanup:** removed the stray `Modifier.kt~` backup file left over from the `Modifier.kt` →
`Modifiers.kt` rename (untracked editor backup, not part of the Gradle source set).

---

## 2026-07-12 — Modifier consolidation: onClick, shimmerLoading, shimmerHighlight

Replaced `Modifier.onScalableClick` and `Modifier.shimmer` (backed by the third-party
`com.valentinilk:shimmer` library) with three new primitives in `common/Modifier.kt`:

- `Modifier.onClick(shape = CircleShape, ripple = true, action)` — rewritten with `composed { }` +
  manual `pointerInput`/`awaitEachGesture` press detection instead of
  `MutableInteractionSource.collectIsPressedAsState()`. Default shape changed from `null` (no clip)
  to `CircleShape`.
- `Modifier.shimmerLoading(isEnable = true, durationMillis = 1000)` — hand-drawn diagonal gradient
  sweep via `drawWithContent { drawContent(); drawShimmerGradient(...) }`, no external dependency.
  Same `isEnable` toggle as the old `shimmer()`.
- `Modifier.shimmerHighlight(backgroundColor, highlightColor, durationMillis = 1400)` — same
  gradient
  sweep but drawn `drawRect(backgroundColor); gradient; drawContent()`, i.e. behind the content
  instead of on top. No `isEnable` toggle — always on while composed.

**onClick migration — the shape default change was the risk.** 6 call sites already passed an
explicit shape (mechanical rename). 8 call sites relied on the old implicit `null` (no clip) and
would have been silently clipped into a circle by the new default — added explicit
`shape = RectangleShape` at each to preserve the original look: `LoginForm.kt` (forgot-password and
sign-up text links), `LoadMoreMessage.kt`, `MangaDescription.kt`, `ThemeOptionItem.kt` (also renamed
its `block =` named argument to `action =`), `MangaChaptersHeader.kt`,
`ChapterLanguageListBottomSheet.kt`, `CategoryTypeHeader.kt`.

**shimmer migration — split by semantic intent, not by draw mechanics.** Determined for each call
site whether the shimmer represents a genuine loading/fetch state or a permanent decorative
flourish:

- `shimmerLoading` (6 sites, all keep `isEnable` tied to a real loading condition or *are* the
  loading indicator itself): `LoadingScreen.kt`, `ListLoadingIndicator.kt`, `MangaItem.kt`,
  `FavoriteMangaItem.kt`, `ReadingHistoryItem.kt`, `MangaBanner.kt` (all `isEnable = !isImageLoaded`
  except `LoadingScreen`/`ListLoadingIndicator`, which have no toggle since the component itself
  only
  renders while loading).
- `shimmerHighlight` (2 sites, always-on branding effect unrelated to any fetch state):
  `AnimatedLogoAndSlogan.kt` (Splash) and `AuthHeader.kt` (Login/Register/ForgotPassword) — both
  shimmer the app logo continuously regardless of load state. Used `backgroundColor = Transparent`
    + a theme-derived `highlightColor` (`onBackground`/`onSurface` at 0.35 alpha, matching the old
      hardcoded white-alpha convention) since the new function requires both explicitly. Preserved
      `AuthHeader`'s explicit 1800ms duration; let `AnimatedLogoAndSlogan` fall back to the new
      1400ms default since the old code never set one either.

**Verified:** `./gradlew compileDebugKotlin` → BUILD SUCCESSFUL after all 21 call sites migrated.

**Follow-up (same day):** fixed the phase-deferral regression in all three new animated modifiers —
`onClick` read `scale` via `by animateFloatAsState(...)` before `graphicsLayer { }`, and both
`shimmerLoading`/`shimmerHighlight` read `progress` via `by transition.animateFloat(...)` before
`drawWithContent { }`, all outside the deferred block. Since the shimmer modifiers run an infinite
animation for as long as they're composed (every loading `MangaItem`/`FavoriteMangaItem`/
`ReadingHistoryItem`/`MangaBanner` cover, plus the two logo highlights), this was recomposing every
composed instance on every animation frame instead of only redrawing. Fixed by keeping `scale`/
`progress` as plain `State<Float>` and reading `.value` inside the `graphicsLayer { }` /
`drawWithContent { }` block instead. Verified with `./gradlew compileDebugKotlin` → BUILD
SUCCESSFUL.
File was also renamed `Modifier.kt` → `Modifiers.kt` during this session; `CLAUDE.md` updated to
match.

**Open item:** `libs.compose.shimmer` (`com.valentinilk:shimmer`) in `app/build.gradle.kts` is no
longer imported anywhere in the codebase — candidate for removal in a follow-up, not done here since
it's a build-file change outside this migration's scope.

---

## 2026-07-12 — Compose performance audit (all features + common/)

Full code-level Diagnose → Fix pass across every feature screen (Auth, Categories, Category Details,
Favorites, History, Home, Manga Details, Profile, Reader, Search, Settings, Splash, Statistics) plus
the shared `presentation/screens/common/` component library. No physical device was available this
session, so this was a code-level audit, not a Macrobenchmark-measured one — see Open items.

**Cross-cutting patterns fixed** (each showed up in more than one unrelated feature):

- `Modifier.blur(8.dp)` used for loading/dim overlays — no-op below API 31 (app `minSdk = 24`), and
  a
  heavier hardware layer than the alternative even above it. Fixed in `LoginContent.kt`,
  `HistoryContent.kt`, `ProfileContent.kt` → replaced with `Modifier.blurBackground(topAlpha,
  bottomAlpha)`.
- Per-item callback lambda built fresh inside a plain `forEach` inside a composable, without
  `remember(key)` — tapping one item forced every sibling in the loop to recompose too. Fixed in
  `CategoriesContent.kt` (`onExpandClick`) and `FilterValueOptions.kt` (checkbox `onClick`).
- State scoped to one page of a `HorizontalPager` hoisted above the content lambda instead of inside
  it. Fixed in `MangaBanner.kt` — `isImageLoaded` was shared by every banner page, so the first
  image
  to load switched off shimmer everywhere.
- Animated `State<Float>` read via `by` delegate at the top of a `graphicsLayer`-driven modifier
  function instead of via `.value` inside the `graphicsLayer { }` block — caused full recomposition
  every animation frame instead of a redraw-only pass. Fixed in `common/Modifier.kt`'s
  `animateItemOnAppear()` and `onScalableClick()` (also swapped deprecated `updateTransition()` for
  `rememberTransition()`).

**Other fixes:**

- `SuggestionList.kt`: `key = { it.hashCode() }` risked a duplicate-key crash since suggestion
  strings aren't guaranteed unique (two different manga can share a title). Corrected to omit `key`
  entirely rather than key on the value itself — the list is always replaced wholesale per debounced
  query, so there's no reorder/insert identity to preserve anyway.
- `ProfilePicture.kt`: Coil `ImageRequest` was built inline every recomposition instead of
  `remember(url) { ... }`, unlike `MangaCoverArt` / `ChapterPageImage` / `MangaDetailsBackground`.

**Explored and reverted:** tried migrating `MangaItem`/`FavoriteMangaItem`/`ReadingHistoryItem`/
`MangaChapterItem` from the hand-rolled `animateItemOnAppear()` to Compose's native
`LazyGridItemScope`/`LazyItemScope.animateItem()`. Rolled back after confirming `animateItem()` only
animates genuine list mutations (insert/remove/reorder), not "item enters view for the first time,"
which is what these screens actually want — kept `animateItemOnAppear()` and optimized it instead
(see above).

**No bugs found** (verified clean): Manga Details, Favorites, Reader, Settings, Splash, Statistics.

**`CLAUDE.md` updated** — added the `blur()` vs `blurBackground()` rule, the LazyList-key uniqueness
exception, the Coil `ImageRequest` remember rule, the per-page/per-item state scoping rule, and the
`graphicsLayer` phase-deferral rule for custom animation modifiers, under **Compose Conventions** /
**Compose Performance**.

**Open items:**

- No Macrobenchmark numbers captured (no physical device this session) — the `:baselineprofile`
  module already exists; a real cold-startup + scroll `FrameTimingMetric` P50/P90/P99 pass on
  release + R8 would confirm these fixes actually move frame times.
- Compose Compiler stability reports and a Layout Inspector recomposition-count pass not done this
  session.
- `StatisticsViewModel` has leftover `Timber.tag("StatisticsDebug")` debug logging — cleanup
  candidate, not a perf issue.
