import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 1.8通过lambda表达式屏蔽接口方法
 *
 * @author N33
 * @date 2019/6/12
 */
public class SomeUseFullyInJdk8 {
    static class F {

        public void foo() {
            System.out.println("===foo===");
        }

        public void foo2() {
            System.out.println("===foo2===");
        }
    }

    /**
     * 多个方法接口（功能混杂）
     */
    interface Management {
        void read();

        void write();

        String state();

        void stop();
    }

    /**
     * 分离
     */
    interface Op {
        void read();

        void write();
    }

    interface Mgnt {
        String state();

        void stop();
    }

    static class A implements Op, Mgnt {

        @Override
        public void read() {

        }

        @Override
        public void write() {

        }

        @Override
        public String state() {
            return null;
        }

        @Override
        public void stop() {

        }
    }


    static class Readable {
        private Op op;

        Readable(Op op) {
            this.op = op;
        }

        public void delegateRead() {
            op.read();
        }
    }

    @Test
    public void testLambda() throws IOException {
        final List<F> fList = Arrays.asList(new F(), new F());
        final Iterator<Closeable> iterator = fList.stream().map(f -> (Closeable) f::foo).iterator();

        /**
         * f::foo
         * 匿名函数
         * 1.no arguments
         * 2.no return value
         * 可以随意转换类型(强转为没有参数没有返回值的任意接口*)
         * 用于屏蔽某些方法
         */
        iterator.next().close();


        /**
         * 1.8以前屏蔽方法
         */
        A a = new A();
        new Readable(a);
    }
}
