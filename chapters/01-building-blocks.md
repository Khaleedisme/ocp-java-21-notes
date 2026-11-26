# Oracle OCP Java SE 21 (1Z0-830) — Chapter 1: Building Blocks

> **Mənbə:** Sybex *OCP Oracle Certified Professional Java SE 21 Developer Study Guide* əsasında sistemləşdirilmiş imtahan konspekti və sürətli təkrar cədvəli.

---

## Mündəricat
1. [Java Komponentləri, Terminal Əmrləri və Şərhlər](#1-java-komponentləri-terminal-əmrləri-və-şərhlər)
2. [Class Strukturu, Access Modifiers və `main()` Metodu](#2-class-strukturu-access-modifiers-və-main-metodu)
3. [Fayl Strukturu (PIC), Paketlər və `import` Qaydaları](#3-fayl-strukturu-pic-paketlər-və-import-qaydaları)
4. [Constructor-lar və İşləmə Ardıcıllığı (Order of Initialization)](#4-constructor-lar-və-i̇şləmə-ardıcıllığı-order-of-initialization)
5. [Primitiv Tiplər, Ədəd Sistemləri, Alt-Xətt (`_`) və Wrapper Class-lar](#5-primitiv-tiplər-ədəd-sistemləri-alt-xətt-_-və-wrapper-class-lar)
6. [Text Blocks (`"""`)](#6-text-blocks-)
7. [İdentifikatorlar, Dəyişənlər və Definite Assignment](#7-i̇dentifikatorlar-dəyişənlər-və-definite-assignment)
8. [Local Variable Type Inference (`var`)](#8-local-variable-type-inference-var)
9. [Scope (Görünmə Sahəsi), Shadowing və Garbage Collection](#9-scope-görünmə-sahəsi-shadowing-və-garbage-collection)
10. [Chapter 1 Cheat Sheet (Sürətli Təkrar)](#10-chapter-1-cheat-sheet-sürətli-təkrar)

---

## 1. Java Komponentləri, Terminal Əmrləri və Şərhlər

Java proqramlarının hazırlanması və icrası üçün JDK-nın əsas alətləri istifadə olunur. İmtahanda əmrlərin fayl sonluqları, paket yolları və tək fayllı icra qaydaları tez-tez yoxlanılır.

| Alət / Əmr | Vəzifəsi | İmtahan Sintaksisi və Tələlər |
| :--- | :--- | :--- |
| `javac` | `.java` mənbə faylını `.class` (bytecode) faylına çevirir. | `.java` sonluğu mütləqdir: `javac Main.java`. Fayl yolunda `/` işlədilir: `javac src/app/Main.java`. |
| `java` | Kompilyasiya olunmuş bytecode-u JVM-də icra edir. | `.class` sonluğu **yazılmır**. `java Main` (OK), `java Main.class` (**Xəta**). Paket adı nöqtə ilə yazılır: `java app.Main`. |
| `java Main.java` | Tək mənbə faylını yaddaşda kompilyasiya edib dərhal işlədir (*Single-File Source-Code*). | Diskdə `.class` faylı yaratmır. **Tələ:** Faylda birdən çox class varsa, JVM faylın adına yox, faylda **ən birinci yazılmış** class-ın `main()` metoduna baxır. |
| `jar` | Faylları `.jar` arxiv paketində toplayır. | Əsasən modullar bölməsində (`jar cvf`) istifadə olunur. |
| `javadoc` | `/** ... */` şərhlərindən sənədləşmə yaradır. | İmtahanda əmr kimi düşmür, amma şərh sintaksisini tanımaq lazımdır. |

### Terminal Seçimləri (`-cp` və `-d`)
* **`-cp <path>`, `-classpath <path>`, `--class-path <path>`:** Həm `javac`, həm də `java` əmrində işlənir. Kompilyasiya və ya icra üçün lazım olan class-ların yerini göstərir. **Diqqət:** `--cp` və `-class-path` yazılışları yanlışdır.
* **`-d <dir>`:** Yalnız `javac` üçündür. Yaradılan `.class` fayllarının paket qovluqları ilə birlikdə hara yazılacağını təyin edir (`java -d` mövcud deyil).

```bash
# Paketli faylın kompilyasiyası (-d out qovluğunun içində app/Main.class yaradır)
javac -d out src/app/Main.java

# İcrası (-cp ilə out qovluğu göstərilir; "Java 21" tək arqument sayılır)
java -cp out app.Main Salam "Java 21"   # args[0]="Salam", args[1]="Java 21"
```

### Şərh (Comment) Qaydaları
Java-da üç şərh növü var: təksətirli (`//`), çoxsətirli (`/* */`) və Javadoc (`/** */`).
* **İmtahan tələsi:** Çoxsətirli şərhlər iç-içə (*nested*) olmur — `/* /* daxili */ xarici */` kodu ilk `*/` işarəsində bağlanır və qalan `xarici */` hissəsi kompilyasiya xətası verir. Lakin `// /*` tam keçərlidir, çünki `//` sətrin qalanını ləğv edir.

---

## 2. Class Strukturu, Access Modifiers və `main()` Metodu

Başqa class-ın `{ }` daxilində olmayan class **top-level class**, başqa class-ın içində yazılan class isə **nested class** adlanır.

| Access Modifier | Görünmə Sahəsi | Top-Level Class | Nested Class |
| :--- | :--- | :---: | :---: |
| `public` | Bütün paketlərdən əlçatandır. | İcazəlidir | İcazəlidir |
| Modifiersiz (*package-private*) | Yalnız eyni paket daxilində görünür. | İcazəlidir | İcazəlidir |
| `protected` | Eyni paket + digər paketdəki subclass-lar. | **Qadağandır** | İcazəlidir |
| `private` | Yalnız elan olunduğu class daxilində görünür. | **Qadağandır** | İcazəlidir |

* Bir `.java` faylında maksimum **1 dənə** `public` top-level class ola bilər (0 dənə də ola bilər).
* Faylda `public` class varsa, fayl adı həmin class-ın adı ilə böyük-kiçik hərf daxil olmaqla eyni olmalıdır. `public` class yoxdursa, fayl adı istənilən valid ad ola bilər.
* `default class Animal {}` yazmaq **Compiler Error** verir; *package-private* üçün sadəcə `class Animal {}` yazılır.

```java
// Fayl: Zoo.java (heç bir public class olmadığı üçün fayl adı fərqli ola bilər)
class Plant {
    private class Seed {}   // OK: Nested class private və ya protected ola bilər
}

class Flower {}             // OK: Top-level package-private

// private class Tree {}    // DOES NOT COMPILE: Top-level private ola bilməz
// default class Grass {}   // DOES NOT COMPILE: 'default' class modifier deyil
```

### Metod İmzası (Signature) və `main()` Giriş Nöqtəsi
* **Metod imzası** yalnız **metod adı + parametr siyahısı** (parametrlərin tipləri, sayı və sırası) deməkdir. Parametr adları, return type, access modifier-lər, `static` və `final` imzaya daxil deyil.
* Standart `main()` giriş nöqtəsi `public`, `static` və `void` olmalı, parametr kimi `String` massivi və ya varargs (`...`) qəbul etməlidir. Bu şərtlərdən biri pozulduqda (məsələn, `static` yazılmadıqda və ya `String args` olduqda) kod adi metod kimi kompilyasiya olunur, lakin işə salındıqda **Runtime** xətası verir.

> **Qeyd:** Java 21-də `--enable-preview` ilə instance `main()` (JEP 445) mövcud olsa da, 1Z0-830 imtahanında preview xüsusiyyətlər düşmədiyi üçün standart `public static void main` qaydaları əsas götürülür.

```java
public static void main(String[] args)        // Standart
static public void main(String options[])     // Modifier sırası dəyişə bilər
public final static void main(String... names)// final və varargs (...) keçərlidir
public static void main(String [] time)       // Parametr adı və boşluq fərqli ola bilər

// public void static main(String[] args)     // DOES NOT COMPILE: void addan əvvəl olmalıdır
// public static void main(String...[] args)  // DOES NOT COMPILE: ... və [] birlikdə olmaz
```

---

## 3. Fayl Strukturu (PIC), Paketlər və `import` Qaydaları

Üst səviyyə fayl elementləri mütləq **PIC** ardıcıllığı ilə düzülməlidir:
1. `package` (maksimum 1, istəyə bağlı)
2. `import` (istənilən sayda, istəyə bağlı)
3. `class` (ən azı 1)

> **Qeyd:** Şərhlər faylın istənilən yerində (hətta `package`-dən əvvəl) ola bilər. Class daxilindəki field, constructor və metodların sırası sərbəstdir.

### Wildcard (`*`) və İmport Üstünlüyü
* `java.lang.*` (məs., `String`, `System`) və cari paketdəki bütün class-lar avtomatik əlçatandır. Onları yenidən import etmək lazımsızdır, amma xəta vermir.
* Bir `import` sətrində yalnız **1 wildcard (`*`)** ola bilər və o, ən sonda durmalıdır. Wildcard yalnız həmin paketin class-larını gətirir — alt-paketləri (*subpackages*) və metodları gətirmir.
* **Üstünlük sırası:** Açıq (*explicit*) import > Eyni paket > Wildcard (`*`) import.

```java
import java.nio.*;              // XƏTA (Files üçün): alt-paket olan java.nio.file-ı gətirmir
// import java.nio.*.*;         // DOES NOT COMPILE: 2 wildcard qadağandır
// import java.nio.file.Paths.*;// DOES NOT COMPILE: adi import metodları gətirmir

// Konflikt halları (java.util.Date vs java.sql.Date):
// 1) import java.util.Date; + import java.sql.Date; -> COMPILER ERROR (istifadə olunmasa belə!)
// 2) import java.util.*; + import java.sql.*;       -> Yalnız 'Date' yazıldıqda COMPILER ERROR
// 3) import java.util.Date; + import java.sql.*;    -> OK (açıq import wildcard-ı üstələyir)
// 4) package b; import a.Item; (b-də də Item var) -> OK (açıq import eyni paketi üstələyir!)
// 5) package y; import x.*; (y-də də Tool var)    -> OK (eyni paket wildcard-ı üstələyir!)
// 6) import util.*; (util-də String class-ı varsa) -> 'String s;' yazanda ERROR (java.lang.* ilə konflikt!)
```

> **İmtahan oxuma qaydası:** Kod birbaşa `class`, metod və ya kod parçası ilə başlayırsa, yaxud sətir nömrələri `1:`-dən başlamırsa, çatışmayan `package` və `import`-ları düzgün yazılmış fərz edin. Kod `1:`-dən başlayırsa, bütün importları yoxlayın.

---

## 4. Constructor-lar və İşləmə Ardıcıllığı (Order of Initialization)

Constructor-un adı class adı ilə eyni olmalıdır və **heç bir return type-ı (hətta `void` belə) olmamalıdır**. Əgər class-da özünüz heç bir constructor yazmamısınızsa, kompilyator avtomatik parametrsiz boş *default constructor* yaradır. Özünüz istənilən parametrli constructor yazdıqda isə default constructor artıq yaradılmır.

```java
public class Chick {
    public void Chick() { // RETURN TYPE VAR (void) -> Adi metoddur, constructor DEYİL!
        System.out.println("Salam");
    }

    public static void main(String[] args) {
        Chick c = new Chick(); // Avtomatik default constructor işləyir; heç nə çap olunmur!
    }
}
```

### İşləmə Ardıcıllığı (Initialization Order)
1. **`static` field-lər və `static { }` blokları:** Class yaddaşa yüklənəndə yalnız **1 dəfə**, faylda yazıldığı sırayla yuxarıdan aşağıya işləyir.
2. **Instance field-lər və `{ }` (*instance initializer*) blokları:** Hər dəfə `new` ilə obyekt yaradıldıqda yuxarıdan aşağıya işləyir (obyekt yaradılmırsa, heç vaxt işləmir!).
3. **Constructor:** Kodda lap yuxarıda yazılsa belə, bütün instance field və `{ }` bloklarından **ən sonda** işləyir.

```java
public class Vault {
    static int count = 1;
    static { count *= 2; }                      // Class yüklənəndə 1 dəfə işləyir (count = 2)

    { code = 10; }                              // OK: elandan əvvəl dəyər YAZMAQ olar
    { System.out.print(this.code + "-"); }      // OK: 'this.' ilə elandan əvvəl OXUMAQ olar
    // { int x = code; }                        // DOES NOT COMPILE: tək adla irəli oxuma qadağandır!

    private int code = 5;                       // Sonradan 10-un üstünə 5 yazır!

    public Vault() { System.out.print(code); }  // Ən sonda işləyir

    public static void main(String[] args) {
        new Vault();                            // Çap: 10-5
    }
}
```

---

## 5. Primitiv Tiplər, Ədəd Sistemləri, Alt-Xətt (`_`) və Wrapper Class-lar

Java-da 8 primitiv tip mövcuddur. Primitiv tiplər heç vaxt `null` ola bilməz və onların metodu yoxdur.
> **Diqqət:** Cədvəldəki *Default* dəyər yalnız **instance** və **static (class)** dəyişənlərinə aiddir — **local** dəyişənlər heç vaxt default dəyər almır!

| Tip | Ölçü | Min - Max Həddi | Default | Wrapper | `Number`-dən törəyir? |
| :--- | :--- | :--- | :--- | :--- | :---: |
| `boolean` | — | `true` / `false` | `false` | `Boolean` | Xeyr |
| `byte` | 8-bit | $-128 \dots 127$ | `0` | `Byte` | Bəli |
| `short` | 16-bit (signed) | $-32,768 \dots 32,767$ | `0` | `Short` | Bəli |
| `int` | 32-bit | $-2,147,483,648 \dots 2,147,483,647$ | `0` | `Integer` | Bəli |
| `long` | 64-bit | $-2^{63} \dots 2^{63}-1$ | `0L` | `Long` | Bəli |
| `float` | 32-bit | Kəsr ədədi | `0.0f` | `Float` | Bəli |
| `double` | 64-bit | Kəsr ədədi | `0.0` | `Double` | Bəli |
| `char` | 16-bit (unsigned) | $0 \dots 65,535$ | `'\u0000'` | `Character` | Xeyr |

### Ədəd Sistemləri, Literal Hədləri və Alt-Xətt (`_`) Qaydaları
* **`short` vs `char`:** `short` işarəlidir (mənfi ola bilər), `char` isə işarəsizdir (0-dan başlayır; `char c = 65;` (`'A'`) keçərlidir, amma `char c = -1;` xətadır).
* **Ədəd sistemləri (default tipi `int`-dir):** İkilik `0b101` (5), səkkizlik `010` (8 — diqqət: `08` və `09` xətadır!), onaltılıq `0x10` (16).
* **`L` və `f` qaydaları:** `long l = 100;` və `float f = 0x10;` kompilyasiya olunur (tam ədəd avtomatik genişlənir). `long big = 2_147_483_648;` (`L` yoxdur) və `float d = 10.0;` (`f` yoxdur) kompilyasiya olunmur.
* **Alt-xətt (`_`) qadağaları:** Ədədin əvvəlində (`_10`), sonunda (`10_`), nöqtənin yanında (`10_.0`, `10._0`), prefiksin düz yanında (`0x_10`, `0b_10` — amma `0_10` səkkizlikdə **OK**-dir!) və tip hərfinin yanında (`5_000_L`) olmaz. Rəqəmlər arasında istənilən sayda ard-arda alt-xətt (`1__2`, `9_6`) keçərlidir.

### Wrapper Class-lar, `parse...` və `valueOf`
`Integer.parseInt(...)` primitiv `int`, `Integer.valueOf(...)` isə wrapper `Integer` obyekti qaytarır. Hər ikisi 2-ci parametr kimi say sistemi (*radix*) qəbul edə bilər.

```java
var a = Integer.parseInt("10", 16);     // 16 (primitiv int)
var b = Integer.valueOf("10", 2);       // 2 (wrapper Integer)
var c = Integer.parseInt("+12");        // 12 (üstəgəl işarəsi keçərlidir!)
var d = Integer.parseInt("010");        // 10 (radix verilməsə onluq sayılır, səkkizlik YOX!)

// Integer.valueOf("G", 16);            // Runtime: NumberFormatException (16-lıqda G yoxdur)
// Integer.valueOf("1_0");              // Runtime: NumberFormatException (String-də '_' tanınmır!)

var t1 = Boolean.parseBoolean("True");  // true (böyük-kiçik hərf fərqi yoxdur)
var t2 = Boolean.parseBoolean(" true ");// false (boşluq olduqda avtomatik false!)
var t3 = Boolean.valueOf("yes");        // false (exception ATMIR, "true" olmayan hər şey false)
```

---

## 6. Text Blocks (`"""`)

Text Block açılan `"""` işarəsindən dərhal sonra **mütləq yeni sətir (line break)** tələb edir — `String s = """doe""";` kompilyasiya olunmur. Soldakı ortaq boşluqlar (*incidental whitespace*) ən solda duran məzmun sətrinə və ya bağlanan `"""` işarəsinin mövqeyinə əsasən hesablanıb kəsilir.

| Simvol / Format | Adi String-də | Text Block-da |
| :--- | :--- | :--- |
| `"` və `"""` | `\"` ilə qaçırılmalıdır | Qaçırmadan (*escape*-siz) birbaşa yazılır (`"Hi"`, `""Ok""`). Ən azı bir dırnaq qaçırılmalıdır: `\"""` |
| Sətir sonu boşluğu | Qorunur | Avtomatik silinir (*ignored*) |
| `\s` | 1 boşluq (`" "`) | 1 boşluq əlavə edir və **ondan əvvəlki sətir sonu boşluqlarını qoruyur** |
| `\` (sətir sonunda) | Keçərsizdir (Compiler Error) | Həmin sətrin sonundakı yeni sətir (`\n`) keçidini ləğv edir |

```java
var text = """
    "Java" \
    21\s
    """; // Bağlanan """ 4 boşluq içəridədir (məzmunla eyni xətdə): ortaq 4 boşluq silinir

// Nəticə: "\"Java\" 21 \n" -> 6 ("Java") + 1 (boşluq) + 2 (21) + 1 (\s) + 1 (\n) = 11 simvol!
// Diqqət: Bağlanan """ ən solda (0 boşluqda) olarsa, məzmunun solundakı bütün boşluqlar qalır!
```

---

## 7. İdentifikatorlar, Dəyişənlər və Definite Assignment

* Java-da dəyişən, metod və class adları (*identifiers*) **hərf, valyuta simvolu (`$`, `€`) və ya alt-xətt (`_`)** ilə başlamalıdır. Rəqəmlə başlaya bilməz, içində `@`, `*`, `#` kimi işarələr ola bilməz.
* Tək `_` (Java 9-dan bəri) identifikator kimi qadağandır (`short _;` → Compiler Error), amma `_1` və ya `__` keçərlidir. (JEP 443 unnamed variable Java 21-də preview-dur, imtahanda tək `_` hələ də xəta sayılır.)
* Reserved sözlər (`public`, `true`, `false`, `null`, eləcə də Java-da istifadə olunmayan `const` və `goto`) ad kimi işlənə bilməz. `Public` böyük hərflə başladığı üçün keçərlidir.
* `var` və `yield` *contextual/reserved type* adlarıdır: dəyişən, metod və paket adı ola bilər, amma `class var {}` yazmaq qadağandır.

### Çoxlu Elan (Multiple Declaration) Qaydaları
```java
int i1, i2, i3 = 0;         // OK: 3 dəyişən elan olunub, YALNIZ i3 = 0 alıb!
String s1 = "1", s2;        // OK: eyni tip, vergüllə ayrılıb
int a = 1; int b = 2;       // OK: iki ayrı ifadə (nöqtəli vergüllə)

// double d1, double d2;    // DOES NOT COMPILE: vergüldən sonra tip təkrarlanmaz
// int num, String val;     // DOES NOT COMPILE: bir ifadədə iki fərqli tip olmaz
// int i3; i4;              // DOES NOT COMPILE: i4-ün tipi yoxdur
```

### Local Dəyişənlər, `final` və Definite Assignment
Local dəyişənlər istifadə edilməzdən əvvəl **bütün mümkün icra yollarında** mütləq başlanğıc dəyəri almalıdır. Dəyər verilməmiş dəyişən oxunmursa, xəta vermir; oxunduğu an kompilyasiya xətası yaranır.

```java
public void check(boolean flag) {
    final boolean always = true;    // Compile-time constant (final + eyni sətirdə dəyər)
    final boolean blank;
    blank = true;                   // Boş final (blank final) OK, amma compile-time constant DEYİL!
    boolean maybe = true;           // final deyil -> kompilyator qiymətinə baxmır

    int a, b, c;
    if (always) a = 1;              // Kompilyator bunu 'if (true)' kimi görür
    if (blank)  b = 2;              // Kompilyator əmin deyil!
    if (maybe)  c = 3;              // Kompilyator əmin deyil!

    System.out.println(a);          // OK
    // System.out.println(b);       // DOES NOT COMPILE!
    // System.out.println(c);       // DOES NOT COMPILE!

    final int x;                    // Boş final if/else-in hər iki qolunda 1 dəfə verilə bilər
    if (flag) { x = 1; } else { x = 2; }
    System.out.println(x);          // OK
}
```

---

## 8. Local Variable Type Inference (`var`)

`var` dəyişənin tipini **kompilyasiya vaxtında (compile-time)** müəyyən edir və həmin tip sonradan dəyişmir. Kompilyator tipi tapmaq üçün yalnız elan olunduğu ifadəyə baxır.

| Keçərli (Kompilyasiya Olunur) | Qadağan (Compiler Error) |
| :--- | :--- |
| Yalnız metod, constructor və `{ }` bloklarının içində (*local variable*) | Class field-i (instance / static), metod/constructor parametri, return type |
| `var s = "Salam"; s = null;` (reference tipə sonradan `null` olar) | `var n = null;` (birbaşa `null` ilə tip təyin olunmur) |
| `var a = (String) null;` (cast olduğu üçün tip `String` olur) | `var num = 10; num = null;` (primitiv `int` `null` ola bilməz) |
| `var silly`<br>`    = 1;` (tək ifadə iki sətirə bölünə bilər) | `var x; x = 1;` (elan və mənimsətmə ayrı ifadə ola bilməz) |
| `var arr = new int[] {1, 2};` | `var arr = {1, 2};` (qısa massiv sintaksisi `var`-la olmaz) |
| `var var = "Java";` (dəyişən/paket/metod adı olar) | `var a = 1, b = 2;` (çoxlu elan) və `class var {}` (tip adı) |
| `var b = 10; b = 'A';` (`char` avtomatik `int`-ə genişlənir) | `var f = 1.0f; f = 2.0;` (`double` `float`-a sığmır) |

---

## 9. Scope (Görünmə Sahəsi), Shadowing və Garbage Collection

* **Local variables:** Elan olunduğu sətirdən daxil olduğu `{ }` blokunun sonuna qədər.
* **Method parameters:** Metodun bütün gövdəsi boyunca.
* **Instance variables:** Obyekt yaranandan GC-yə uyğun (*eligible*) olana qədər.
* **Class (`static`) variables:** Class yüklənəndən proqram bitənə qədər.

### Shadowing vs Təkrar Elan
Metodun içində class-ın field-i ilə eyni adda local dəyişən yaratmaq (**shadowing**) keçərlidir. Lakin eyni metodun içində artıq scope-da olan başqa bir local dəyişən və ya metod parametri ilə eyni adda dəyişən yaratmaq (hətta daxili `{ }` blokunda belə!) **Compiler Error** verir. `for` dövrü bitdikdən sonra isə dövr daxilindəki dəyişən scope-dan çıxdığı üçün onun adını yenidən işlətmək olar.

```java
public class ScopeDemo {
    int a = 1;                          // Instance field

    public void run(int b) {
        int a = 2;                      // OK: field-i kölgələyir (shadowing)
        for (int i = 0; i < 1; i++) {
            int j = i;
        }
        int j = 5;                      // OK: əvvəlki 'j' for blokundan çıxıb
        {
            // int b = 3;               // DOES NOT COMPILE: metod parametri 'b' hələ scope-dadır!
        }
    }
}
```

### Garbage Collection (GC) Qaydaları
* Bir obyekt yalnız **2 haldan** biri baş verdikdə GC-yə uyğun (*eligible for garbage collection*) olur:
  1. Ona işarə edən heç bir əlçatan referans qalmadıqda;
  2. Ona işarə edən bütün referanslar scope-dan çıxdıqda.
* **Island of Isolation:** İki obyekt bir-birə işarə etsə belə (`n1.next = n2; n2.next = n1;`), kənardan onlara heç bir canlı referans qalmayıbsa (`n1 = null; n2 = null;`), hər ikisi GC-yə uyğun olur.
* **Pass-by-value tələsi:** Bir obyekti metoda ötürüb metodun içində `t = null;` etmək çağıran tərəfdəki orijinal referansı `null` etmir — obyekt canlı qalır.
* **`System.gc();`:** JVM-ə yalnız tövsiyə (*suggest/request*) göndərir, heç nəyə zəmanət vermir. GC sabit zaman intervalında işləmir. *"Obyekt nə vaxt silinəcək?"* sualının cavabı həmişə **"Zəmanət yoxdur"** olur; biz yalnız nə vaxt *eligible* olduğunu dəqiq deyə bilərik.

---

## 10. Chapter 1 Cheat Sheet (Sürətli Təkrar)

> İmtahandan əvvəl 3 dəqiqədə göz gəzdirmək üçün ən çox bal aparan kompilyator qaydaları:

| Mövzu | Qızıl Qayda və İmtahan Tələsi |
| :--- | :--- |
| **Terminal (`javac`/`java`)** | `javac -d out src/a/B.java` → `java -cp out a.B`. `java Main.class` **XƏTADIR**. `java B.java` (*single-file*) faylda ilk yazılan class-ı işə salır. `-cp`, `-classpath`, `--class-path` OK; `--cp` və `java -d` **XƏTA**. |
| **Fayl & Class** | Sıra: **PIC** (`package` → `import` → `class`). Maks. 1 `public` top-level class (varsa fayl adı eyni olmalıdır; 0 `public` varsa fərqli ola bilər). Top-level `private`/`protected`/`default` **OLMAZ**; nested class `private`/`protected` **OLAR**. |
| **`main()` & Şərhlər** | `public static void main(String[] args)` → `static public`, `final`, `String... x` OK; `String...[]` və `void static` **XƏTA**. Yanlış `main` kompilyasiya olunur, runtime xətası verir. `/* /* */ */` **XƏTA**; `// /*` OK. |
| **İmport Üstünlüyü** | **Açıq import > Eyni paket > Wildcard (`*`)**. Yalnız 1 `*` və sonda; alt-paketə enmir, metod gətirmir. 2 açıq import = həmişə **XƏTA**; 2 wildcard (və ya `util.*` + avtomatik `java.lang.*`) = istifadə edəndə **XƏTA**. |
| **Constructor & Sıra** | Return type (hətta `void`) varsa, o metoddur! Sıra: `static` (1 dəfə) → instance field & `{ }` (yuxarıdan aşağıya) → constructor (ən sonda). İrəli istinad: `x = 5` OK, `this.x` OK, tək `x` oxumaq **XƏTA**. |
| **Primitiv & Literal** | `byte` ($-128\dots127$), `char` ($0\dots65535$, mənfi **OLMAZ**!). `08` səkkizlik **XƏTA**. `float f = 10.0` (`f` lazımdır) və `long l = 3_000_000_000;` (`L` lazımdır) **XƏTA**. `_` əvvəldə, sonda, `0x_`, `0b_`, `_L`/`_F` yanında **OLMAZ** (`0_10` və `9__6` OK). |
| **Wrapper Parsing** | `parseInt` → primitiv; `valueOf` → obyekt. `"1_0"` → `NumberFormatException`; `"+12"` → `12`; `parseInt("010")` → `10` (onluq). `Boolean`: yalnız boşluqsuz `"true"` (*case-insensitive*) → `true`, qalan hər şey (`" true "`, `"yes"`) → `false`. |
| **Text Blocks** | `"""`-dən sonra yeni sətir **MƏCBURİDİR**. `\` yeni sətri silir; `\s` 1 boşluq qoyur və əvvəlki trailing boşluqları saxlayır. Bağlanan `"""` ən soldadırsa, sol boşluqlar kəsilmir. `"` və `""` escape-siz yazılır. |
| **İdentifikatorlar** | Başlanğıc: hərf, `$`, `€`, `_` (rəqəm **OLMAZ**). Tək `_`, `const`, `goto`, `true`/`false`/`null` **QADAĞANDIR**. `var` dəyişən/paket/metod adı **OLAR**, class adı **OLMAZ**. |
| **`var` Qaydaları** | Yalnız local + eyni ifadədə dəyər. Field, parametr, return type, çoxlu elan (`var a=1, b=2`), `var x = null`, `var arr = {1,2}` **XƏTADIR**. `var s = (String) null;` və `var d = 1/0;` **KOMPİLYASİYA OLUNUR**. |
| **Default & Scope** | Local dəyişənin default dəyəri **YOXDUR**! Instance/static: `0`, `0.0`, `false`, `'\u0000'`, `null`. `else`-siz `if` yalnız eyni sətirdə verilmiş `final` sabitə güvənir. Field shadowing OK; local/parametr təkrar adı **XƏTA**. |
| **Garbage Collection** | Eligible = əlçatan referans yoxdur və ya out of scope (*island of isolation* eligible-dir). `System.gc()` yalnız *suggests* — silinmə vaxtına heç vaxt zəmanət yoxdur! |
