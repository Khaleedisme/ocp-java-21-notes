public class Vault {
    static int count = 1;
    static { count *= 2; }                      // Class yuklenende 1 defe isleyir (count = 2)

    { code = 10; }                              // OK: elandan evvel deyer yazmaq olar
    { System.out.print(this.code + "-"); }      // OK: 'this.' ile elandan evvel oxumaq olar

    private int code = 5;                       // Sonradan 10-un ustune 5 yazir

    public Vault() {
        System.out.println(code);               // En sonda isleyir
    }

    public static void main(String[] args) {
        new Vault();                            // Cap: 10-5
    }
}
