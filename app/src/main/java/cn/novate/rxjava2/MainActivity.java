package cn.novate.rxjava2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.novate.rxjava2.demo.ChapterEight;
import cn.novate.rxjava2.demo.ChapterFour;
import cn.novate.rxjava2.demo.ChapterOne;
import cn.novate.rxjava2.demo.ChapterSeven;
import cn.novate.rxjava2.demo.ChapterThree;
import cn.novate.rxjava2.demo.ChapterTwo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 测试ChapterOne示例代码
        /*testChapterOne() ;*/

        // 测试ChapterTwo示例代码
        /*testChapterTwo() ;*/

        // 测试ChapterThree示例代码
        testChapterThree();

        // 测试ChapterFour示例代码
        testChapterFour();

        // 测试ChapterSeven示例代码
        testChapterSeven();

        // 测试ChapterSeven示例代码
        testChapterEight();
    }

    /**
     * 测试ChapterOne示例代码
     */
    private void testChapterOne() {
//        ChapterOne.demo1();
//        ChapterOne.demo2();
//        ChapterOne.demo3();
//        ChapterOne.demo4();
    }


    /**
     * 测试ChapterTwo示例代码
     */
    private void testChapterTwo() {
//        ChapterTwo.demo1();demo3
//        ChapterTwo.demo2();
//        ChapterTwo.login(MainActivity.this);
    }


    /**
     * 测试ChapterThree示例代码
     */
    private void testChapterThree() {
//        ChapterThree.map();
//          ChapterThree.flatMap();
    }


    /**
     * 测试ChapterFour示例代码
     */
    private void testChapterFour() {
//        ChapterFour.demo1();
    }

    /**
     * 测试ChapterSeven示例代码
     */
    private void testChapterSeven() {
//        ChapterSeven.demo1();
//        ChapterSeven.demo2();
//        ChapterSeven.demo3();
    }




    /**
     * 测试ChapterEight示例代码
     */
    private void testChapterEight() {
        ChapterEight.demo2();
    }


}
