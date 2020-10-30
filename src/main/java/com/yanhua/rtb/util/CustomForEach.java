/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: CustomForEach
 * Author: Emiya
 * Date: 2020/10/28 17:31
 * Description: 自定义stream的断点
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.util;

import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * 〈功能简述〉<br>
 * 〈自定义stream的断点〉
 *  <p>
 * @author Emiya
 * @create 2020/10/28 17:31
 * @version 1.0.0
 */
public class CustomForEach {
    public static class Breaker {
        private volatile boolean shouldBreak = false;

        public void stop() {
            shouldBreak = true;
        }

        boolean get() {
            return shouldBreak;
        }
    }

    public static <T> void forEach(Stream<T> stream, BiConsumer<T, Breaker> consumer) {
        Spliterator<T> spliterator = stream.spliterator();
        boolean hadNext = true;
        Breaker breaker = new Breaker();

        while (hadNext && !breaker.get()) {
            hadNext = spliterator.tryAdvance(elem -> {
                consumer.accept(elem, breaker);
            });
        }
    }
}