package opensource.capinfo.uploader;

import org.junit.Test;

import java.util.*;
import java.util.function.*;

public class TestCode {


    /**
     * Consumer 《T》：消费型接口，有参无返回值
     */
    @Test
    public void test(){
        changeStr("hello",(str) -> System.out.println(str));
    }

    /**
     *  Consumer<T> 消费型接口
     * @param str
     * @param con
     */
    public void changeStr(String str, Consumer<String> con){
        con.accept(str);
    }


    /**
     * Supplier 《T》：供给型接口，无参有返回值
     */
    @Test
    public void test2(){
        String value = getValue(() -> "hello");
        System.out.println(value);
    }

    /**
     *  Supplier<T> 供给型接口
     * @param sup
     * @return
     */
    public String getValue(Supplier<String> sup){
        return sup.get();
    }


    /**
     * Function 《T,R》：:函数式接口，有参有返回值
     */
    @Test
    public void test3(){
        /*Long result = changeNum(100L, (x) -> x + 200L);
        System.out.println(result);*/

        String result = changeTest(80, (x) -> x+100+"hello");
        System.out.println(result);

    }

    /**
     *  Function<T,R> 函数式接口
     * @param num
     * @param fun
     * @return
     */
    public Long changeNum(Long num, Function<Long, Long> fun){
        return fun.apply(num);
    }

    public String changeTest(int num, Function<Integer, String> fun) {
        return fun.apply(num);
    }



    /**
     * Predicate《T》： 断言型接口，有参有返回值，返回值是boolean类型
     */
    @Test
    public void test4(){
        boolean result = changeBoolean("hello", (str) -> str.length() > 5);
        System.out.println(result);
    }

    /**
     *  Predicate<T> 断言型接口
     * @param str
     * @param pre
     * @return
     */
    public boolean changeBoolean(String str, Predicate<String> pre){
        return pre.test(str);
    }





    @Test
    public void test5() {
        /**
         *注意：
         *   1.lambda体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致！
         *   2.若lambda参数列表中的第一个参数是实例方法的调用者，而第二个参数是实例方法的参数时，可以使用ClassName::method
         *
         */
        Consumer<Integer> con = (x) -> System.out.println(x);
        con.accept(100);

        // 方法引用-对象::实例方法
        Consumer<Integer> con2 = System.out::println;
        con2.accept(200);

        // 方法引用-类名::静态方法名
        BiFunction<Integer, Integer, Integer> biFun = (x, y) -> Integer.compare(x, y);
        BiFunction<Integer, Integer, Integer> biFun2 = Integer::compare;
        Integer result = biFun2.apply(100, 200);
        Integer res = biFun.apply(20,50);
        System.out.println(res);

        // 方法引用-类名::实例方法名
        BiFunction<String, String, Boolean> fun1 = (str1, str2) -> str1.equals(str2);
        BiFunction<String, String, Boolean> fun2 = String::equals;
        Boolean result2 = fun2.apply("hello", "world");
        System.out.println(result2);
    }



    @Test
    public void testLamda1(){
        List<String> list = Arrays.asList("1","6","9","2");
        Collections.sort(list, (Comparator<? super String>) (String a, String b)->{
                    //return b.compareTo(a);
                    return a.compareTo(b);
                }
        );
        /*for (String string : list) {
            System.out.println(string);
        }*/
        list.forEach((str) -> {
            System.out.println(str);
        });

        Runnable run1 = () -> {
            System.out.println("================111");
        };
        run1.run();
    }


    /**
     * Collections.sort(ruleList, new Comparator<CapInspectionRule>() {
     *                        @Override
     * 			public int compare(CapInspectionRule o1, CapInspectionRule o2) {
     * 				if (o1.getRuleWayOrder() > o2.getRuleWayOrder()) {
     * 					return 1;
     * 				}
     * 				if (o1.getRuleWayOrder() == o2.getRuleWayOrder()) {
     * 					return 0;
     * 				}
     * 				return -1;
     * 			}
     * 		});
     */
    @Test
    public void testComparatorLamda() {

        List<Integer> testList = new ArrayList<Integer>();
        testList.add(16);
        testList.add(87);
        testList.add(23);
        testList.add(56);
        testList.add(16);

        Collections.sort(testList, (Comparator<Integer>) (Integer a, Integer b) -> {
            if (a>b) {
                return 1;
            }
            if (a == b) {
                return 0;
            }
            return -1;
        });

        testList.forEach(i -> {
            System.out.print(i);
        });

    }


}
