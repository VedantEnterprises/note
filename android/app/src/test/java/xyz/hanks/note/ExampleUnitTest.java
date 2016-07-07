package xyz.hanks.note;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String s = "dasdasd\n";
        System.out.println("s = " + s.trim());

        String s0 = "计算的一些规则";
        String s1 = "今天是晴天今天是晴天今天是晴天今天是晴天";
        String s2 = "一二三四五六七八九十一二三四五六七八九十";
        String s3 = "abcdefghijklmnopqrstuvwxyzabcdefghijklmn";
        String s4 = "，，，，，。。。。。，，，，，。。。。。";
        String s5 = ".....,,,,,.....,,,,,.....,,,,,.....,,,,,.....,,,,,.....,,,,,.....,,,,,.....,,,,,....";
        String s6 = "http://www.baidu.comhttp://www.baidu.co";

        System.out.println("s0 = " + s0.length());
        System.out.println("s1 = " + s1.length());
        System.out.println("s2 = " + s2.length());
        System.out.println("s3 = " + s3.length());
        System.out.println("s4 = " + s4.length());
        System.out.println("s5 = " + s5.length());
        System.out.println("s6 = " + s6.length());
    }
}