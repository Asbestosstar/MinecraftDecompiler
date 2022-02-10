/*
 * MinecraftDecompiler. A tool/library to deobfuscate and decompile Minecraft.
 * Copyright (C) 2019-2022  MaxPixelStudios
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cn.maxpixel.mcdecompiler.util;

import java.util.function.Consumer;

public class LambdaUtil {
    @FunctionalInterface
    public interface Supplier_WithThrowable<T, E extends Throwable> {
        T get() throws E;
    }

    @FunctionalInterface
    public interface Runnable_WithThrowable<E extends Throwable> {
        void run() throws E;
    }

    @FunctionalInterface
    public interface Function_WithThrowable<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface Consumer_WithThrowable<T, E extends Throwable> {
        void accept(T t) throws E;
    }

    public static <E extends Throwable> void rethrowAsRuntime(E throwable) {
        throw Utils.wrapInRuntime(throwable);
    }

    public static <T, E extends Throwable> T trySupply(Supplier_WithThrowable<T, E> supplierWithThrowable) {
        try {
            return supplierWithThrowable.get();
        } catch(Throwable e) {
            throw Utils.wrapInRuntime(e);
        }
    }

    public static <E extends Throwable> Runnable unwrap(Runnable_WithThrowable<E> runnableWithThrowable) {
        return unwrap(runnableWithThrowable, LambdaUtil::rethrowAsRuntime);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> Runnable unwrap(Runnable_WithThrowable<E> runnableWithThrowable, Consumer<E> exceptionHandler) {
        return () -> {
            try {
                runnableWithThrowable.run();
            } catch(Throwable e) {
                exceptionHandler.accept((E) e);
            }
        };
    }

    public static <T, E extends Throwable> Consumer<T> unwrap(Consumer_WithThrowable<T, E> consumerWithThrowable) {
        return unwrap(consumerWithThrowable, LambdaUtil::rethrowAsRuntime);
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> Consumer<T> unwrap(Consumer_WithThrowable<T, E> consumerWithThrowable, Consumer<E> exceptionHandler) {
        return t -> {
            try {
                consumerWithThrowable.accept(t);
            } catch(Throwable e) {
                exceptionHandler.accept((E) e);
            }
        };
    }
}