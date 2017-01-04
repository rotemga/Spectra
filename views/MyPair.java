package tau.smlab.syntech.dependencygraph.views;

public class MyPair<K, V> {

  private final K element0;
  private final V element1;

  public static <K, V> MyPair<K, V> createPair(K element0, V element1) {
      return new MyPair<K, V>(element0, element1);
  }

  public MyPair(K element0, V element1) {
      this.element0 = element0;
      this.element1 = element1;
  }

  public K getElement0() {
      return element0;
  }

  public V getElement1() {
      return element1;
  }

}