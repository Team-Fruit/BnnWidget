package com.kamesuta.mc;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 何もアノテーションがついていない場合、それはNullではないことを保証します。
 *
 * @author TeamFruit
 */
@Retention(RUNTIME)
@Target(PACKAGE)
public @interface NonNullByDefault {

}
