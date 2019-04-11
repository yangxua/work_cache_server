import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: allanyang
 * @Date: 2019/4/11 17:08
 * @Description:
 */

public class Test {


    @Autowired
    private TestMgr testMgr;

   /* public void test() {
        String value = testMgr.get();
        System.out.println(value);
    }*/

    public static void main(String[] args) throws InterruptedException {
        TestMgr testMgr = new TestMgr();
        String s = testMgr.get();
        System.out.println(s);

        String s1 = testMgr.get();
        System.out.println(s1);
    }
}