# KotlinLab11: Permissions — Amaliy Mashg'ulot

**Mavzu:** Install-time, Runtime va Special ruxsatnomalar, Jetpack Compose (10-Ma'ruza — Permissions)  
**Loyiha nomi:** `KotlinLab11` / `PermissionsLab`  
**Paket nomi:** `com.example.kotlinlab11` (uz.ttpu.permissionslab)

---

## A Qism. Nazariy Savollar va To'liq Javoblar (1-19)

### 1-savol
**Kontekst:** Talaba ilovasiga rasmga olish funksiyasini qo'shmoqchi. U birinchi navbatda manifestga `CAMERA` ruxsatini yozdi va darhol dialog kodini yozishga o'tdi. Jamoadoshi esa: “Avval ruxsatnoma umuman kerakmi-yo'qmi, shuni hal qilish kerak”, deydi.  
**Vazifa:** Ruxsatnomalar bilan ishlash jarayonining (workflow) to'rtta qaror ketma-ketligi, ruxsatnomasiz bajariladigan holat va nega bu eng yaxshi variantligi:  
**Javob:**
1. **Workflow 4 qarori:**
   - **1-qaror:** Ruxsatnoma umuman kerakmi? Ilova topshiriqni Intent orqali ruxsatnomasiz bajara oladimi?
   - **2-qaror:** Toifani aniqlash (Install-time, Runtime yoki Special permission).
   - **3-qaror:** Kontekstda so'rash (foydalanuvchi funksiyani ishlatayotgan paytda ruxsat so'rash).
   - **4-qaror:** Natijani va rad etish holatlarini gracefully boshqarish (Rationale va Sozlamalarga yo'naltirish).
2. **Ruxsatnomasiz holatga misol:** Tizim kamerasini Intent orqali ochish (`ActivityResultContracts.TakePicture()`) yoki Android Photo Picker ishlatish.
3. **Nega eng yaxshi variant:** Foydalanuvchida ilovaga nisbatan ishonch oshadi, ortiqcha ruxsatnoma dialogi ko'rsatilmaydi, maxfiylik va xavfsizlik ta'minlanadi, va ilova kodi soddalashadi.

---

### 2-savol
**Kontekst:** Jamoadoshingiz Android ruxsatnomalarini “manifestdagi bitta qator” deb hisoblaydi va barcha ruxsatnomalar uchun bir xil kod yozadi.  
**Vazifa:** Ruxsatnomalarning 3 toifasi va ularning kiritilishi, kod hajmiga ta'siri:  
**Javob:**
1. **Install-time permissions:** Tizim tomonidan ilova o'rnatilayotganda avtomatik beriladi. Bunga Normal va Signature ruxsatnomalar kiradi. Kod hajmi minimal (faqat `AndroidManifest.xml`da e'lon qilinadi).
2. **Runtime permissions:** Maxfiy ma'lumotlarga taalluqli ruxsatnomalar (`CAMERA`, `LOCATION`, `CONTACTS`). Ilova ishlayotgan vaqtda foydalanuvchiga dialog chiqariladi. Kod hajmi o'rtacha (check, rationale, launch, result handler va permanently denied holatlarini talab qiladi).
3. **Special permissions:** Tizim darajasidagi ruxsatnomalar (`SYSTEM_ALERT_WINDOW`, `MANAGE_EXTERNAL_STORAGE`). Tizim Sozlamalari (Settings) sahifasiga Intent orqali yo'naltirish talab qilinadi. Kod hajmi eng katta (Intent, lifecycle event `ON_RESUME` va holatni qayta o'qish talab qilinadi).

---

### 3-savol
**Kontekst:** Dasturchi manifestga `INTERNET` ruxsatini qo'shdi, keyin esa har bir tarmoq so'rovi oldidan `checkSelfPermission` bilan tekshiruv yozmoqda. Boshqa ilovada esa xuddi shu qator unutilgan.  
**Vazifa:** Normal ruxsatnomalar va unutilganda chiqadigan exception:  
**Javob:**
1. **Nega runtime checks kerak emas:** Normal ruxsatnomalar foydalanuvchi maxfiyligiga jiddiy xavf tug'dirmaydi. Shuning uchun Android tizimi ularni ilova o'rnatilish vaqtida avtomatik beradi va `checkSelfPermission` har doim `PERMISSION_GRANTED` qaytaradi.
2. **Manifestda unutilsa:** Ilova `java.lang.SecurityException` tashlaydi (masalan, `Permission Denial: requires android.permission.INTERNET`).
3. **3 ta misol:** `INTERNET`, `ACCESS_NETWORK_STATE`, `RECEIVE_BOOT_COMPLETED`.

---

### 4-savol
**Kontekst:** Bir kompaniyaning ikkita ilovasi bor: “Bank” va “Hamyon”. Hamyon bankning AccountsService xizmatiga ulanmoqchi, xizmat esa `uz.ttpu.bank.permission.READ_ACCOUNTS` ruxsatnomasi bilan himoyalangan. Uchinchi tomon ilovasi ham xuddi shu ruxsatnomani so'ramoqda.  
**Vazifa:** Signature ruxsatnomasi shartlari:  
**Javob:**
1. **Kimga beriladi:** Faqat ruxsatnomani e'lon qilgan ilova bilan bir xil raqamli kalit (keystore / signing certificate) orqali imzolangan ilovalarga beriladi.
2. **protectionLevel:** `android:protectionLevel="signature"`.
3. **Natija va Dialog:** "Hamyon" (bir xil kalitga ega) o'rnatish vaqtida ruxsatni avtomatik oladi. Begona ilovaga esa ruxsat rad etiladi. Foydalanuvchiga **hech qanday dialog ko'rsatilmaydi**.

---

### 5-savol
**Kontekst:** Talaba install-time ruxsatnomalarni ham “ilova ishlayotganda dialog orqali so'rash kerak” deb o'ylaydi.  
**Vazifa:** Install-time ruxsatnomalari ko'rsatilishi va turlari:  
**Javob:**
1. **Qachon va qayerda:** Google Play Store / do'konning ilova tafsilotlari (App Details) sahifasida ilovani yuklab olish yoki yangilashdan oldin ko'rsatiladi.
2. **Qachon beriladi:** Ilova qurilmaga o'rnatilayotgan (installation) vaqtda tizim tomonidan avtomatik beriladi.
3. **Ikki tur:** **Normal permissions** va **Signature permissions**.

---

### 6-savol
**Kontekst:** Ilova minSdk 21 bilan chiqarilgan. Android 5.1 (API 22) qurilmasida `CAMERA` uchun dialog chiqmayapti, lekin kamera ishlayapti. Android 14 da esa dialog chiqadi.  
**Vazifa:** API 22 vs API 23+ va `granted(kind, sdk)` modeli:  
**Javob:**
1. **Runtime ruxsatnomalar qachon kiritilgan:** Android 6.0 (API 23, Marshmallow) da kiritilgan.
2. **Nega API 22 da dialog chiqmaydi:** API 22 va undan past versiyalarda eski xavfsizlik modeli amal qiladi — Manifest'da e'lon qilingan barcha ruxsatnomalar ilova o'rnatilayotganda to'liq beriladi.
3. **`granted(kind, sdk)` modeli:**
   - `sdk < 23` bo'lsa: Manifest'dagi barcha ruxsatnomalar o'rnatishda avtomatik `granted = true`.
   - `sdk >= 23` va `kind == RUNTIME` bo'lsa: `granted` qiymati runtime so'rovi va foydalanuvchi tasdiqlashiga bog'liq bo'ladi.

---

### 7-savol
**Kontekst:** Foydalanuvchi kamera ruxsatini ikki marta rad etdi. Dasturchi tugma bosilganda yana `launch()` chaqiradi, lekin ekranda hech narsa ko'rinmaydi va ruxsat darhol rad etilgan holda qaytadi.  
**Vazifa:** Runtime ruxsatnoma holatlari ("Denied twice" modeli):  
**Javob:**
1. **Hali so'ralmagan (Not requested):** `shouldShowRationale = false`, `isGranted = false`. Kod so'rov dialogini chiqarishi kerak (`launch()`).
2. **Bir marta rad etilgan (Denied once):** `shouldShowRationale = true`, `isGranted = false`. Kod tushuntirish (rationale) UI ko'rsatishi va qayta so'rash tugmasini berishi kerak.
3. **Ikki marta rad etilgan (Denied twice / Permanently denied):** `shouldShowRationale = false`, `isGranted = false`. Tizim dialogi boshqa chiqmaydi. Kod foydalanuvchini **Ilova Sozlamalari sahifasiga** (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`) yo'naltiruvchi tugma chiqarishi kerak.

---

### 8-savol
**Kontekst:** Yangi jamoadosh `CameraPermissionExample` composable'ini o'qimoqda. Unda `rememberPermissionState`, `@OptIn(ExperimentalPermissionsApi::class)` va `when` ichida uchta tarmoq bor.  
**Vazifa:** Accompanist Compose API tahlili:  
**Javob:**
1. **Uchta tarmoq:**
   - `status.isGranted`: Ruxsat berilgan -> Asosiy kamera ekrani ko'rsatiladi.
   - `status.shouldShowRationale`: Bir marta rad etilgan -> Nega ruxsat kerakligi tushuntiriladi va qayta so'rash tugmasi chiqariladi.
   - `else`: Hali so'ralmagan yoki ikki marta rad etilgan -> So'rash yoki Sozlamalarni ochish tugmasi ko'rsatiladi.
2. **Nega recomposition'dan omon qoladi:** `rememberPermissionState` ichki Compose `remember` hamda Activity Result API orqali holatni saqlaydi, har bir recomposition bo'lganda dialog qayta chiqishini va holat yo'qolishini oldini oladi.
3. **ExperimentalPermissionsApi:** Kutubxona ushbu API tajriba bosqichida ekanligini va kelgusi versiyalarda o'zgarishi mumkinligini bildiradi.

---

### 9-savol
**Kontekst:** Jamoa loyihaga qo'shimcha kutubxona qo'shishni xohlamaydi.  
**Vazifa:** Activity Result API yordamida ruxsat so'rash:  
**Javob:**
1. **So'rov yozish:** `val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> ... }` va `launcher.launch(Manifest.permission.CAMERA)`.
2. **Holatni o'qish:** `ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED`.
3. **Rationale aniqlash:** `ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)`.
4. **Accompanist bilan solishtirish:** Accompanist ushbu standart Android API kodlarini Compose state va enum holatlariga o'rab beruvchi wrapper hisoblanadi.

---

### 10-savol
**Kontekst:** Xarita ekrani aniq (`ACCESS_FINE_LOCATION`) va taxminiy (`ACCESS_COARSE_LOCATION`) joylashuvni birga so'raydi.  
**Vazifa:** RequestMultiplePermissions va joylashuv dialogi:  
**Javob:**
1. **Natija formati:** `Map<String, Boolean>` qaytadi. Masalan: `result[Manifest.permission.ACCESS_FINE_LOCATION] == true`.
2. **Bitta UI dialog:** Android 12 (API 31)+ tizimi foydalanuvchiga bitta interfeysda "Precise" (Aniq) yoki "Approximate" (Taxminiy) joylashuvni tanlash imkonini beradi.
3. **Ikkala rad etilsa:** Ilova joylashuvga bog'liq funksiyalarni o'chirib, foydalanuvchiga tushuntirish xabarini ko'rsatadi va Sozlamalar tugmasini taklif qiladi.

---

### 11-savol
**Kontekst:** Dasturchi `SYSTEM_ALERT_WINDOW` uchun oddiy runtime dialogini chiqarishga urinmoqda, lekin dialog umuman chiqmayapti.  
**Vazifa:** Special ruxsatnomalar farqi va jadval:  
Special ruxsatnomalar tizim darajasidagi xavfsizlikka ta'sir qilgani uchun oddiy dialog orqali berilmaydi. Foydalanuvchi Sozlamalar menyusiga o'tib, ruxsatni qo'lda yoqishi shart.

| Ruxsatnoma | Nimaga ruxsat beradi | Tekshirish chaqiruvi |
| :--- | :--- | :--- |
| `SYSTEM_ALERT_WINDOW` | Boshqa ilovalar ustidan oyna chiqarish (Overlay) | `Settings.canDrawOverlays(context)` |
| `WRITE_SETTINGS` | Tizim sozlamalarini o'zgartirish | `Settings.System.canWrite(context)` |
| `MANAGE_EXTERNAL_STORAGE` | Tashqi xotiradagi barcha fayllarga to'liq kirish | `Environment.isExternalStorageManager()` |
| `REQUEST_INSTALL_PACKAGES` | Noma'lum manbalardan APK o'rnatish | `context.packageManager.canRequestPackageInstalls()` |
| `ACCESS_BACKGROUND_LOCATION` | Fon rejimida joylashuv ma'lumotlarini olish | `ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PERMISSION_GRANTED` |

---

### 12-savol
**Kontekst:** Foydalanuvchi overlay ruxsatini Sozlamalar ekranida yoqib, ilovaga qaytdi, lekin ilova hamon “ruxsat yo'q” deb ko'rsatmoqda.  
**Vazifa:** "Tekshir -> Sozlamalarni och -> qaytganda tekshir" andozasi va `ON_RESUME`:  
1. **Nega callback kelmaydi:** Sozlamalar ekrani oddiy Intent orqali ochilgani uchun Android uning natijasini qaytaruvchi callback chaqirmaydi.
2. **Tuzatilgan kod:**
```kotlin
@Composable
fun OverlayScreen() {
    val context = LocalContext.current
    var canDraw by remember {
        mutableStateOf(Settings.canDrawOverlays(context))
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        canDraw = Settings.canDrawOverlays(context)
    }

    Switch(
        checked = canDraw,
        onCheckedChange = { openOverlaySettings(context) }
    )
}
```

---

### 13-savol
**Kontekst:** Dasturchi `ACCESS_FINE_LOCATION` va `ACCESS_BACKGROUND_LOCATION` ni bitta `launch` chaqiruvida birga so'ramoqda. Fon joylashuvi ishlamayapti.  
**Vazifa:** Fon joylashuvi 2 bosqichli tartibi:  
1. **1-bosqich:** Avval faqat old fon joylashuvi (`ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`) so'raladi.
2. **2-bosqich:** Old fon joylashuvi berilgandan so'nggina fon joylashuvi (`ACCESS_BACKGROUND_LOCATION`) so'raladi. Ikkalasini birga so'ralgan taqdirda Android so'rovni avtomatik bekor qiladi.
3. **SDK_INT tekshiruvi:** `ACCESS_BACKGROUND_LOCATION` Android 10 (API 29, Q) da joriy etilgan. Undan past versiyalarda old fon joylashuvi ruxsati fon uchun ham yetarli bo'lgan.

---

### 14-savol
**Kontekst:** Ilova beshta ruxsatnomani birga so'raydi: ikkita joylashuv, ikkita kontakt va bitta kamera. Talaba beshta dialog chiqadi deb kutgan edi, aslida uchtasi chiqdi.  
**Vazifa:** Permission group va dialoglar soni:  
1. **Permission group nima:** Mantiqan bir-biriga bog'liq bo'lgan xavfli ruxsatnomalar toplami.
2. **Dialoglar soni kamayishi:** Bitta guruhga kiruvchi ruxsatnomalardan birontasi berilsa, guruhdagi boshqa ruxsatnomalar alohida dialogsiz berilishi yoki bitta birlashtirilgan dialog ko'rsatilishi mumkin. (2 joylashuv = 1 dialog, 2 kontakt = 1 dialog, 1 kamera = 1 dialog, jami 3 dialog).
3. **3 ta guruh misoli:** `LOCATION`, `CONTACTS`, `CAMERA`.
4. **Nega guruhga tayanmaslik kerak:** Guruhlar va ularning xatti-harakati Android versiyalarida o'zgaradi. Kod har doim har bir ruxsatnomani alohida tekshirishi va so'rashi shart.

---

### 15-savol
**Kontekst:** Talaba `CAMERA` uchun dialog kodini yozdi, lekin manifestga `<uses-permission>` qo'shishni unutdi.  
**Vazifa:** Manifestda unutilgan ruxsatnoma va `maxSdkVersion`:  
1. **Nima bo'ladi:** Tizim dialog ko'rsatmasdan so'rovni darhol rad etadi yoki `SecurityException` beradi.
2. **E'lon qilish farqi:** Barcha ruxsatnomalar Manifest'da e'lon qilinishi shart. Runtime va Special ruxsatnomalar qo'shimcha ravishda kodda so'raladi.
3. **`maxSdkVersion`:** Ruxsatnoma ma'lum bir API versiyasigacha kerakligini bildiradi (masalan, `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />`). API 29+ da Scoped Storage tufayli ushbu ruxsatnoma kerak bo'lmaydi.

---

### 16-savol
**Kontekst:** Ilova kameradan ixtiyoriy foydalanadi. Do'kon ilovani kamerasiz qurilmalarda yashirib qo'ydi, jamoa esa ilova hamma qurilmaga o'rnatilishini xohlaydi.  
**Vazifa:** `<uses-feature>` va `required="false"`:  
1. **`required="true"` vs `false`:** `true` bo'lsa, Play Store kamerasi yo'q qurilmalarda ilovani yashiradi. `false` bo'lsa, ilova kamerasiz qurilmalarga ham o'rnatiladi.
2. **Kodda tekshirish:** `context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)`.
3. **`visibleOn` modelining 4 natijasi:**
   - Apparat bor + Ruxsat bor -> Kamera ishlaydi.
   - Apparat bor + Ruxsat yo'q -> So'rov tugmasi ko'rsatiladi.
   - Apparat yo'q + Ruxsat bor/yo'q -> Kamera UI o'chiriladi/yashiriladi.
   - Apparat required=true va apparat yo'q -> Ilova Play Store'da ko'rinmaydi.

---

### 17-savol
**Kontekst:** Dasturchi kamera holatini bir marta o'qib saqlaydi. Foydalanuvchi “Faqat bu safar” (Only this time) ni tanlagan edi. Ilova fonga o'tib, bir soatdan keyin qaytganda kamera tugmasi faol ko'rinadi, lekin ishlamaydi.  
**Vazifa:** Bir martalik ruxsatnoma va `ON_RESUME`:  
1. **Bir martalik ruxsatnoma (API 30):** User "Only this time" ni tanlaganda ruxsat vaqtincha beriladi. Ilova fonga o'tganda yoki biroz vaqtdan so'ng tizim ruxsatni avtomatik bekor qiladi.
2. **Nega keshlamaslik kerak:** `remember` orqali kesh xotirada saqlangan `granted` qiymati fonga o'tib qaytganda yangilanmaydi va ilova o'zini ruxsat bor deb o'ylaydi.
3. **Tuzatilgan kod (`ON_RESUME` yordamida):**
```kotlin
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
    // ...
}
```

---

### 18-savol
**Kontekst:** Foydalanuvchi ilovani bir necha oy ishlatmagan. Qaytgach, ilgari berilgan ruxsatnoma ishlamayapti. Boshqa tomondan, yangi versiyada ilovaga `READ_CONTACTS` endi kerak emas.  
**Vazifa:** Auto-reset va `revokeSelfPermissionOnKill`:  
1. **Auto-reset (Android 11+):** Oylab ishlatilmagan ilovaning ruxsatnomalarini tizim avtomatik nollaydi. Ilova buni odatiy rad etilgan holatdek so'rashi kerak. Foydalanuvchi sozlamalarda uni o'chirish uchun: `Intent(Intent.ACTION_AUTO_REVOKE_PERMISSIONS)`.
2. **`revokeSelfPermissionOnKill` (Android 13 / API 33):** Ilova o'ziga kerak bo'lmagan runtime ruxsatnomalardan voz kechishi uchun ishlatiladi. Bu foydalanuvchi maxfiyligi va xavfsizligini oshiradi.

---

### 19-savol
**Kontekst:** “Ruxsatnomalar Android versiyalari bo'yicha qanday o'zgargan” jadvali.  
**Vazifa:** API 23, API 30 va API 33 o'zgarishlari va `POST_NOTIFICATIONS`:  
1. **API 23:** Runtime ruxsatnomalar modeli joriy etildi.
2. **API 30:** Bir martalik ("Only this time") ruxsatnomalar va avtomatik bekor qilish (Auto-reset) kiritildi.
3. **API 33:** Bildirishnomalar runtime ruxsatnomaga aylantirildi (`POST_NOTIFICATIONS`) va media ruxsatnomalar maydalashtirildi.
4. **`POST_NOTIFICATIONS`:** API 33 dan pastda bildirishnomalar o'rnatish vaqtida avtomatik beriladi va runtime so'rov yo'q. Shuning uchun uni API 33 dan pastda so'ramaslik kerak (`Build.VERSION.SDK_INT >= 33` sharti bilan o'raladi).

---

## B Qism. Amaliy Topshiriqlar Hujjati

### 1-topshiriq: Pure Kotlin PermissionModel (`task1/Main.kt`)
- `PermissionModel` klassi yozildi: `isGranted`, `shouldShowRationale`, `dialogShows` xossalari bilan.
- `request(userAllows: Boolean)` funksiyasi va "Denied twice" simulyatsiyasi amalga oshirildi.
- console outputda 1, 2, 3 va 4-so'rovlar ketma-ketligi hamda ma'ruzadagi 13-slayd natijasi hosil qilindi.

### 2-topshiriq: Normal Ruxsatnomalar (`task2/NetworkScreen.kt`)
- `AndroidManifest.xml`ga `INTERNET` va `ACCESS_NETWORK_STATE` qo'shildi.
- `ConnectivityManager` yordamida real vaqt rejimida "Online" / "Offline" holati ko'rsatiladi.
- Runtime dialog yozilmaganligi sababi izoh sifatida yozildi.

### 3-topshiriq: Kamera Ekrani (`task3/CameraScreen.kt`)
- `CAMERA` ruxsatnomasi boshqariladi.
- 3 ta xolat: Granted, Rationale, Not Granted.
- Ikki marta rad etilganda Sozlamalarga o'tish (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`) tugmasi chiqadi.
- `LifecycleEventEffect(Lifecycle.Event.ON_RESUME)` orqali Sozlamalardan qaytganda holat avtomatik yangilanadi.

### 4-topshiriq: Special Permission Overlay (`task4/OverlayScreen.kt`)
- `SYSTEM_ALERT_WINDOW` ruxsati uchun `Switch` interfeysi yaratildi.
- Switch bosilganda `Settings.ACTION_MANAGE_OVERLAY_PERMISSION` ochiladi.
- `LifecycleEventEffect(Lifecycle.Event.ON_RESUME)` yordamida `Settings.canDrawOverlays(context)` holati qayta tekshirilib, Switch avtomatik yangilanadi.

### 5-topshiriq: Bir Martalik Joylashuv (`task5/LocationScreen.kt`)
- `RequestMultiplePermissions` orqali `ACCESS_FINE_LOCATION` va `ACCESS_COARSE_LOCATION` so'raldi.
- "Aniq", "Taxminiy" va "Yo'q" holatlari ekranda ko'rsatiladi.
- **Kuzatuv Natijasi:** Dialogda "Only this time" tanlanganda va ilova fonga olib chiqilib qaytilganda, `ON_RESUME` chaqiruvi tufayli UI ruxsat yo'qligini ("NONE") avtomatik ko'rsatadi.

### 6-topshiriq (Bonus): Bildirishnoma Ruxsati (`task6/NotificationScreen.kt`)
- `Build.VERSION.SDK_INT >= 33` (API 33+) versiyalarda `POST_NOTIFICATIONS` runtime so'raladi.
- API 32 va undan past versiyalarda so'rov o'tkazib yuboriladi va ruxsat berilgan hisoblanadi.
- Ruxsat berilganda `NotificationCompat.Builder` orqali test bildirishnomasi yuboriladi.

---

### Sinov O'tkazilgan API Darajalari
- **API 34 (Android 14):** Barcha runtime, one-time va notification ruxsatnomalari to'liq tekshirildi.
- **API 32 (Android 12L):** `POST_NOTIFICATIONS` so'rovisiz bildirishnoma darhol ishlashi va bir martalik joylashuv holati tekshirildi.
