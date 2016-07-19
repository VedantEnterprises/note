package xyz.hanks.note;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import xyz.hanks.note.util.PrettyDateUtils;

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

    @Test
    public void testDataUtils() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date0 = simpleDateFormat.parse("2016/07/11 00:02:22");
        Date date1 = simpleDateFormat.parse("2016/07/10 12:02:22");
        Date date2 = simpleDateFormat.parse("2016/07/08 23:12:52");
        Date date3 = simpleDateFormat.parse("2016/07/01 15:42:11");
        Date date4 = simpleDateFormat.parse("2016/06/11 10:52:22");
        Date date5 = simpleDateFormat.parse("2015/07/11 19:02:22");
        Date date6 = simpleDateFormat.parse("2016/01/11 16:12:22");
        Date date7 = simpleDateFormat.parse("2014/07/11 08:02:22");
        Date date8 = simpleDateFormat.parse("2016/07/11 11:02:22");
        System.out.println("date0 = " + PrettyDateUtils.format(date0.getTime()));
        System.out.println("date1 = " + PrettyDateUtils.format(date1.getTime()));
        System.out.println("date2 = " + PrettyDateUtils.format(date2.getTime()));
        System.out.println("date3 = " + PrettyDateUtils.format(date3.getTime()));
        System.out.println("date4 = " + PrettyDateUtils.format(date4.getTime()));
        System.out.println("date5 = " + PrettyDateUtils.format(date5.getTime()));
        System.out.println("date6 = " + PrettyDateUtils.format(date6.getTime()));
        System.out.println("date7 = " + PrettyDateUtils.format(date7.getTime()));
        System.out.println("date8 = " + PrettyDateUtils.format(date8.getTime()));

    }

    @Test
    public void testUUID() throws Exception {
        //generate random UUIDs
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        System.out.println("idOne = " + idOne);
        System.out.println("idTwo = " + idTwo);
    }
}