# Oracle OCP Java SE 21 (1Z0-830) — Chapter 1: Building Blocks

> **Source:** Exam study notes and quick-reference cheat sheet systematized from the Sybex *OCP Oracle Certified Professional Java SE 21 Developer Study Guide*.

---

## Table of Contents
1. [Java Components, Terminal Commands, and Comments](#1-java-components-terminal-commands-and-comments)
2. [Class Structure, Access Modifiers, and the `main()` Method](#2-class-structure-access-modifiers-and-the-main-method)
3. [File Structure (PIC), Packages, and `import` Rules](#3-file-structure-pic-packages-and-import-rules)
4. [Constructors and Order of Initialization](#4-constructors-and-order-of-initialization)
5. [Primitive Types, Number Systems, Underscores (`_`), and Wrapper Classes](#5-primitive-types-number-systems-underscores-_-and-wrapper-classes)
6. [Text Blocks (`"""`)](#6-text-blocks-)
7. [Identifiers, Variables, and Definite Assignment](#7-identifiers-variables-and-definite-assignment)
8. [Local Variable Type Inference (`var`)](#8-local-variable-type-inference-var)
9. [Scope, Shadowing, and Garbage Collection](#9-scope-shadowing-and-garbage-collection)
10. [Chapter 1 Cheat Sheet (Quick Review)](#10-chapter-1-cheat-sheet-quick-review)

---

## 1. Java Components, Terminal Commands, and Comments

Core JDK tools are used to compile and run Java programs. The exam frequently tests file extensions, package paths, and single-file execution rules.

| Tool / Command | Purpose | Exam Syntax and Traps |
| :--- | :--- | :--- |
| `javac` | Compiles a `.java` source file into a `.class` (bytecode) file. | The `.java` extension is mandatory: `javac Main.java`. File paths use `/`: `javac src/app/Main.java`. |
| `java` | Executes compiled bytecode on the JVM. | Do **not** include `.class`: `java Main` (OK), `java Main.class` (**Error**). Package names use dots: `java app.Main`. |
| `java Main.java` | Compiles a single source file in memory and runs it immediately (*Single-File Source-Code*). | Does not create a `.class` file on disk. **Trap:** If the file contains multiple classes, the JVM looks at the `main()` method of the **first** class declared in the file, not the filename. |
| `jar` | Bundles files into a `.jar` archive. | Primarily used in the modules chapter (`jar cvf`). |
| `javadoc` | Generates documentation from `/** ... */` comments. | Not tested as a CLI command, but you must recognize the comment syntax. |

### Terminal Options (`-cp` and `-d`)
* **`-cp <path>`, `-classpath <path>`, `--class-path <path>`:** Valid for both `javac` and `java`. Specifies where to find classes required for compilation or execution. **Watch out:** `--cp` and `-class-path` are invalid flags.
* **`-d <dir>`:** Valid only for `javac`. Specifies the destination directory where generated `.class` files and their package folders are written (`java -d` does not exist).

```bash
# Compile a packaged file (-d creates app/Main.class inside the out directory)
javac -d out src/app/Main.java

# Execute (-cp points to out; "Java 21" counts as a single argument)
java -cp out app.Main Hello "Java 21"   # args[0]="Hello", args[1]="Java 21"
```

### Comment Rules
Java supports three comment types: single-line (`//`), multi-line (`/* */`), and Javadoc (`/** */`).
* **Exam trap:** Multi-line comments cannot be nested — `/* /* inner */ outer */` closes at the first `*/`, leaving `outer */` to cause a compiler error. However, `// /*` is completely valid because `//` comments out the rest of the line.

---

## 2. Class Structure, Access Modifiers, and the `main()` Method

A class not enclosed inside another class's `{ }` is a **top-level class**; a class declared inside another class is a **nested class**.

| Access Modifier | Visibility | Top-Level Class | Nested Class |
| :--- | :--- | :---: | :---: |
| `public` | Accessible from all packages. | Allowed | Allowed |
| No modifier (*package-private*) | Visible only within the same package. | Allowed | Allowed |
| `protected` | Same package + subclasses in other packages. | **Forbidden** | Allowed |
| `private` | Visible only within the enclosing class. | **Forbidden** | Allowed |

* A `.java` file can have at most **1** `public` top-level class (it can also have 0).
* If a file contains a `public` class, the filename must match that class name exactly (case-sensitive). If there is no `public` class, the filename can be any valid name.
* Writing `default class Animal {}` causes a **Compiler Error**; use `class Animal {}` for *package-private*.

```java
// File: Zoo.java (filename can differ because there is no public class)
class Plant {
    private class Seed {}   // OK: Nested class can be private or protected
}

class Flower {}             // OK: Top-level package-private

// private class Tree {}    // DOES NOT COMPILE: Top-level cannot be private
// default class Grass {}   // DOES NOT COMPILE: 'default' is not a class modifier
```

### Method Signature and the `main()` Entry Point
* A **method signature** consists solely of the **method name + parameter list** (parameter types, count, and order). Parameter names, return type, access modifiers, `static`, and `final` are not part of the signature.
* The standard `main()` entry point must be `public`, `static`, and `void`, taking a `String` array or varargs (`...`). Violating any of these (for example, omitting `static` or using `String args`) still compiles as a regular method, but throws a **Runtime** error when executed.

> **Note:** Although Java 21 includes instance `main()` methods (JEP 445) under `--enable-preview`, preview features are not tested on the 1Z0-830 exam, so standard `public static void main` rules apply.

```java
public static void main(String[] args)        // Standard
static public void main(String options[])     // Modifier order can vary
public final static void main(String... names)// final and varargs (...) are valid
public static void main(String [] time)       // Parameter name and spacing can vary

// public void static main(String[] args)     // DOES NOT COMPILE: void must precede name
// public static void main(String...[] args)  // DOES NOT COMPILE: cannot combine ... and []
```

---

## 3. File Structure (PIC), Packages, and `import` Rules

Top-level file elements must follow the **PIC** order:
1. `package` (at most 1, optional)
2. `import` (any number, optional)
3. `class` (at least 1)

> **Note:** Comments can appear anywhere in the file (even before `package`). Inside a class, the order of fields, constructors, and methods is flexible.

### Wildcards (`*`) and Import Precedence
* `java.lang.*` (e.g., `String`, `System`) and all classes in the current package are automatically available. Re-importing them is redundant but does not cause an error.
* An `import` statement may contain at most **1 wildcard (`*`)**, and it must be at the end. A wildcard imports classes in that package only — it does not import subpackages or methods.
* **Precedence order:** Explicit import > Same package > Wildcard (`*`) import.

```java
import java.nio.*;              // ERROR (for Files): does not import subpackage java.nio.file
// import java.nio.*.*;         // DOES NOT COMPILE: 2 wildcards are forbidden
// import java.nio.file.Paths.*;// DOES NOT COMPILE: regular import cannot import methods

// Conflict scenarios (java.util.Date vs java.sql.Date):
// 1) import java.util.Date; + import java.sql.Date; -> COMPILER ERROR (even if unused!)
// 2) import java.util.*; + import java.sql.*;       -> COMPILER ERROR only when 'Date' is referenced
// 3) import java.util.Date; + import java.sql.*;    -> OK (explicit import wins over wildcard)
// 4) package b; import a.Item; (b also has Item)    -> OK (explicit import wins over same package!)
// 5) package y; import x.*; (y also has Tool)       -> OK (same package wins over wildcard!)
// 6) import util.*; (if util contains String)       -> ERROR when writing 'String s;' (conflicts with java.lang.*)
```

> **Exam reading rule:** If a snippet starts directly with `class`, a method, or code lines, or if line numbers do not start at `1:`, assume missing `package` and `import` statements are valid. If line numbers start at `1:`, check all imports carefully.

---

## 4. Constructors and Order of Initialization

A constructor must have the exact same name as the class and **must not have any return type (not even `void`)**. If you do not write any constructor, the compiler automatically generates a no-argument *default constructor*. Once you define any constructor with parameters, the default constructor is no longer generated.

```java
public class Chick {
    public void Chick() { // HAS RETURN TYPE (void) -> Regular method, NOT a constructor!
        System.out.println("Hello");
    }

    public static void main(String[] args) {
        Chick c = new Chick(); // Calls the compiler-generated default constructor; prints nothing!
    }
}
```

### Order of Initialization
1. **`static` fields and `static { }` blocks:** Execute only **once** when the class is loaded, in top-to-bottom order as written in the file.
2. **Instance fields and `{ }` (*instance initializer*) blocks:** Execute top-to-bottom every time an object is created with `new` (if no object is created, they never run).
3. **Constructor:** Runs **last**, after all instance fields and `{ }` blocks, even if written at the top of the class.

```java
public class Vault {
    static int count = 1;
    static { count *= 2; }                      // Runs once on class load (count = 2)

    { code = 10; }                              // OK: can WRITE before declaration
    { System.out.print(this.code + "-"); }      // OK: can READ before declaration via 'this.'
    // { int x = code; }                        // DOES NOT COMPILE: forward read without 'this.' is forbidden!

    private int code = 5;                       // Overwrites 10 with 5!

    public Vault() { System.out.print(code); }  // Runs last

    public static void main(String[] args) {
        new Vault();                            // Output: 10-5
    }
}
```

---

## 5. Primitive Types, Number Systems, Underscores (`_`), and Wrapper Classes

Java has 8 primitive types. Primitives can never be `null` and have no methods.
> **Watch out:** The *Default* column applies only to **instance** and **static (class)** variables — **local** variables never receive a default value!

| Type | Size | Min - Max Range | Default | Wrapper | Extends `Number`? |
| :--- | :--- | :--- | :--- | :--- | :---: |
| `boolean` | — | `true` / `false` | `false` | `Boolean` | No |
| `byte` | 8-bit | $-128 \dots 127$ | `0` | `Byte` | Yes |
| `short` | 16-bit (signed) | $-32,768 \dots 32,767$ | `0` | `Short` | Yes |
| `int` | 32-bit | $-2,147,483,648 \dots 2,147,483,647$ | `0` | `Integer` | Yes |
| `long` | 64-bit | $-2^{63} \dots 2^{63}-1$ | `0L` | `Long` | Yes |
| `float` | 32-bit | Floating-point | `0.0f` | `Float` | Yes |
| `double` | 64-bit | Floating-point | `0.0` | `Double` | Yes |
| `char` | 16-bit (unsigned) | $0 \dots 65,535$ | `'\u0000'` | `Character` | No |

### Number Systems, Literal Limits, and Underscore (`_`) Rules
* **`short` vs `char`:** `short` is signed (can be negative), whereas `char` is unsigned (starts at 0; `char c = 65;` (`'A'`) is valid, but `char c = -1;` does not compile).
* **Number systems (default type is `int`):** Binary `0b101` (5), octal `010` (8 — watch out: `08` and `09` do not compile!), hexadecimal `0x10` (16).
* **`L` and `f` rules:** `long l = 100;` and `float f = 0x10;` compile (widening conversion). `long big = 2_147_483_648;` (missing `L`) and `float d = 10.0;` (missing `f`) do not compile.
* **Underscore (`_`) restrictions:** Forbidden at the start (`_10`), at the end (`10_`), next to a decimal point (`10_.0`, `10._0`), right next to a prefix (`0x_10`, `0b_10` — though `0_10` in octal is **OK**!), and next to a type suffix (`5_000_L`). Multiple consecutive underscores between digits (`1__2`, `9_6`) are valid.

### Wrapper Classes, `parse...`, and `valueOf`
`Integer.parseInt(...)` returns a primitive `int`, while `Integer.valueOf(...)` returns a wrapper `Integer` object. Both accept a radix as an optional second parameter.

```java
var a = Integer.parseInt("10", 16);     // 16 (primitive int)
var b = Integer.valueOf("10", 2);       // 2 (wrapper Integer)
var c = Integer.parseInt("+12");        // 12 (leading '+' is valid!)
var d = Integer.parseInt("010");        // 10 (without radix, parsed as decimal, NOT octal!)

// Integer.valueOf("G", 16);            // Runtime: NumberFormatException (no 'G' in base 16)
// Integer.valueOf("1_0");              // Runtime: NumberFormatException ('_' not allowed in Strings!)

var t1 = Boolean.parseBoolean("True");  // true (case-insensitive)
var t2 = Boolean.parseBoolean(" true ");// false (whitespace returns false!)
var t3 = Boolean.valueOf("yes");        // false (does NOT throw, anything other than "true" is false)
```

---

## 6. Text Blocks (`"""`)

A Text Block requires a **line break immediately after the opening `"""`** — `String s = """doe""";` does not compile. Incidental whitespace on the left is stripped based on the leftmost content line or the position of the closing `"""`.

| Symbol / Format | In Regular String | In Text Block |
| :--- | :--- | :--- |
| `"` and `"""` | Must be escaped as `\"` | Written directly without escaping (`"Hi"`, `""Ok""`). At least one quote must be escaped for `"""`: `\"""` |
| Trailing whitespace | Preserved | Automatically stripped (*ignored*) |
| `\s` | 1 space (`" "`) | Adds 1 space and **preserves trailing spaces before it** |
| `\` (at end of line) | Invalid (Compiler Error) | Suppresses the newline (`\n`) at the end of that line |

```java
var text = """
    "Java" \
    21\s
    """; // Closing """ is indented 4 spaces (aligned with content): 4 spaces stripped

// Result: "\"Java\" 21 \n" -> 6 ("Java") + 1 (space) + 2 (21) + 1 (\s) + 1 (\n) = 11 chars!
// Note: If closing """ is at column 0 (leftmost), no leading spaces are stripped!
```

---

## 7. Identifiers, Variables, and Definite Assignment

* Java identifiers (variable, method, and class names) must start with a **letter, currency symbol (`$`, `€`), or underscore (`_`)**. They cannot start with a digit or contain symbols like `@`, `*`, `#`.
* A single `_` has been forbidden as an identifier since Java 9 (`short _;` → Compiler Error), though `_1` and `__` are valid. (JEP 443 unnamed variables are a preview feature in Java 21, so a standalone `_` is still invalid on the exam.)
* Reserved words (`public`, `true`, `false`, `null`, as well as unused keywords `const` and `goto`) cannot be used as identifiers. `Public` is valid because Java is case-sensitive.
* `var` and `yield` are *contextual/reserved type names*: valid as variable, method, or package names, but `class var {}` is forbidden.

### Multiple Declaration Rules
```java
int i1, i2, i3 = 0;         // OK: 3 variables declared, ONLY i3 is initialized to 0!
String s1 = "1", s2;        // OK: same type, separated by comma
int a = 1; int b = 2;       // OK: two separate statements (semicolon)

// double d1, double d2;    // DOES NOT COMPILE: cannot repeat type after comma
// int num, String val;     // DOES NOT COMPILE: cannot declare different types in one statement
// int i3; i4;              // DOES NOT COMPILE: i4 has no type
```

### Local Variables, `final`, and Definite Assignment
Local variables must be assigned a value along **every possible execution path** before they are read. An uninitialized local variable does not cause an error unless it is actually read.

```java
public void check(boolean flag) {
    final boolean always = true;    // Compile-time constant (final + initialized inline)
    final boolean blank;
    blank = true;                   // Blank final is OK, but NOT a compile-time constant!
    boolean maybe = true;           // Not final -> compiler ignores its runtime value

    int a, b, c;
    if (always) a = 1;              // Compiler treats this as 'if (true)'
    if (blank)  b = 2;              // Compiler is not certain!
    if (maybe)  c = 3;              // Compiler is not certain!

    System.out.println(a);          // OK
    // System.out.println(b);       // DOES NOT COMPILE!
    // System.out.println(c);       // DOES NOT COMPILE!

    final int x;                    // Blank final can be assigned once in each branch of if/else
    if (flag) { x = 1; } else { x = 2; }
    System.out.println(x);          // OK
}
```

---

## 8. Local Variable Type Inference (`var`)

`var` determines the variable's type at **compile-time**, and that type cannot change afterward. The compiler inspects only the initialization expression to infer the type.

| Valid (Compiles) | Forbidden (Compiler Error) |
| :--- | :--- |
| Only inside methods, constructors, and `{ }` blocks (*local variables*) | Class fields (instance / static), method/constructor parameters, return types |
| `var s = "Hello"; s = null;` (reference type can later hold `null`) | `var n = null;` (cannot infer type directly from `null`) |
| `var a = (String) null;` (cast provides `String` type) | `var num = 10; num = null;` (primitive `int` cannot be `null`) |
| `var silly`<br>`    = 1;` (single statement can span lines) | `var x; x = 1;` (declaration and assignment cannot be split) |
| `var arr = new int[] {1, 2};` | `var arr = {1, 2};` (array initializer shorthand not allowed with `var`) |
| `var var = "Java";` (valid variable/package/method name) | `var a = 1, b = 2;` (multiple declaration) and `class var {}` (type name) |
| `var b = 10; b = 'A';` (`char` widens to `int`) | `var f = 1.0f; f = 2.0;` (`double` does not fit in `float`) |

---

## 9. Scope, Shadowing, and Garbage Collection

* **Local variables:** From declaration to the end of their enclosing `{ }` block.
* **Method parameters:** Throughout the entire method body.
* **Instance variables:** From object creation until eligible for GC.
* **Class (`static`) variables:** From class loading until program termination.

### Shadowing vs. Redeclaration
Creating a local variable with the same name as a class field (**shadowing**) is valid. However, declaring a local variable with the same name as another local variable or method parameter already in scope (even inside a nested `{ }` block) causes a **Compiler Error**. Once a `for` loop ends, its loop variable goes out of scope and its name can be reused.

```java
public class ScopeDemo {
    int a = 1;                          // Instance field

    public void run(int b) {
        int a = 2;                      // OK: shadows the instance field
        for (int i = 0; i < 1; i++) {
            int j = i;
        }
        int j = 5;                      // OK: previous 'j' is out of scope
        {
            // int b = 3;               // DOES NOT COMPILE: parameter 'b' is still in scope!
        }
    }
}
```

### Garbage Collection (GC) Rules
* An object becomes **eligible for garbage collection** only when one of **2 conditions** is met:
  1. It no longer has any reachable references pointing to it;
  2. All references pointing to it go out of scope.
* **Island of Isolation:** Even if two objects reference each other (`n1.next = n2; n2.next = n1;`), if no live external reference can reach them (`n1 = null; n2 = null;`), both are eligible for GC.
* **Pass-by-value trap:** Passing an object to a method and setting `t = null;` inside the method does not null out the caller's original reference — the object stays alive.
* **`System.gc();`:** Only sends a *suggestion/request* to the JVM with no guarantees. GC does not run on a fixed schedule. The answer to *"When will the object be collected?"* is always **"Not guaranteed"**; we can only determine when it becomes *eligible*.

---

## 10. Chapter 1 Cheat Sheet (Quick Review)

> Highest-yield compiler rules for a 3-minute pre-exam review:

| Topic | Golden Rule and Exam Trap |
| :--- | :--- |
| **Terminal (`javac`/`java`)** | `javac -d out src/a/B.java` → `java -cp out a.B`. `java Main.class` is an **ERROR**. `java B.java` (*single-file*) runs the first class in the file. `-cp`, `-classpath`, `--class-path` OK; `--cp` and `java -d` **ERROR**. |
| **File & Class** | Order: **PIC** (`package` → `import` → `class`). Max 1 `public` top-level class (filename must match if present; can differ if 0 `public`). Top-level `private`/`protected`/`default` **FORBIDDEN**; nested `private`/`protected` **ALLOWED**. |
| **`main()` & Comments** | `public static void main(String[] args)` → `static public`, `final`, `String... x` OK; `String...[]` and `void static` **ERROR**. Invalid `main` compiles, fails at runtime. `/* /* */ */` **ERROR**; `// /*` OK. |
| **Import Precedence** | **Explicit > Same package > Wildcard (`*`)**. Max 1 `*` at the end; no subpackages, no methods. 2 explicit imports = always **ERROR**; 2 wildcards (or `util.*` + `java.lang.*`) = **ERROR** when used. |
| **Constructor & Order** | Any return type (even `void`) makes it a method! Order: `static` (once) → instance fields & `{ }` (top-to-bottom) → constructor (last). Forward reference: `x = 5` OK, `this.x` OK, reading `x` directly **ERROR**. |
| **Primitives & Literals** | `byte` ($-128\dots127$), `char` ($0\dots65535$, negative **FORBIDDEN**!). `08` octal **ERROR**. `float f = 10.0` (needs `f`) and `long l = 3_000_000_000;` (needs `L`) **ERROR**. `_` forbidden at start, end, next to `0x_`, `0b_`, `_L`/`_F` (`0_10` and `9__6` OK). |
| **Wrapper Parsing** | `parseInt` → primitive; `valueOf` → object. `"1_0"` → `NumberFormatException`; `"+12"` → `12`; `parseInt("010")` → `10` (decimal). `Boolean`: only `"true"` without spaces (*case-insensitive*) → `true`; everything else (`" true "`, `"yes"`) → `false`. |
| **Text Blocks** | Newline after opening `"""` is **MANDATORY**. `\` suppresses newline; `\s` adds 1 space and preserves preceding trailing spaces. Leftmost closing `"""` keeps all leading spaces. `"` and `""` need no escape. |
| **Identifiers** | Start with letter, `$`, `€`, `_` (no digits). Single `_`, `const`, `goto`, `true`/`false`/`null` **FORBIDDEN**. `var` valid as variable/package/method name, **NOT** class name. |
| **`var` Rules** | Local only + initialized in same statement. Field, parameter, return type, multiple declaration (`var a=1, b=2`), `var x = null`, `var arr = {1,2}` **ERROR**. `var s = (String) null;` and `var d = 1/0;` **COMPILE**. |
| **Default & Scope** | Local variables have **NO** default value! Instance/static: `0`, `0.0`, `false`, `'\u0000'`, `null`. `if` without `else` only trusts inline `final` constants. Field shadowing OK; duplicate local/parameter name **ERROR**. |
| **Garbage Collection** | Eligible = no reachable references or out of scope (*island of isolation* is eligible). `System.gc()` only *suggests* — collection timing is never guaranteed! |
